package com.cashplus.callback.internal;

import com.cachplus.callBackHandler.ICallBackConfiguration;
import com.cachplus.callBackHandler.ICallBackHandler;
import com.cashplus.callback.internal.exceptions.DuplicateRequestIdException;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@Component("Callback4j")
class CallBackServiceImpl implements ICallBackHandler {

    private final WebClient webClient;
    @Autowired
    private CallBackRepository callBackRepository;
    @Autowired
    private JavaMailSender mailSender;
    CallBackServiceImpl(WebClient webClient, HttpCallback httpCallback) {
        this.webClient = webClient;
    }

    public CallBack addCallback(CallBack callback) {
        String serviceName= callback.getServiceName();
        int requestId=callback.getRequestId();
        if (!isRequestIdUnique(serviceName, requestId)) throw new DuplicateRequestIdException("Request ID already exists for the service");
        return callBackRepository.save(callback);
    }
    @Override
    public void sendEmailCallBack(String serviceName,int requestId,String fromEmail,String toEmail, String subject, String body){
        EmailCallback emailCallback=new EmailCallback();
        emailCallback.setServiceName(serviceName);
        emailCallback.setRequestId(requestId);
        emailCallback.setSenderEmail(fromEmail);
        emailCallback.setRecipientEmail(toEmail);
        emailCallback.setSubject(subject);
        emailCallback.setCallBackData(body);
        emailCallback.setCallbackState(CallbackState.Send);
        System.out.println(emailCallback);
        try{
            this.addCallback(emailCallback);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        sendEmail(fromEmail,toEmail,subject,body);
    }


    @Override
    public void sendHttpCallBack(String serviceName, int requestId,URI urlCallBack, JSONObject dataToSend, ICallBackConfiguration configuration) {
        HttpCallback httpCallback = new HttpCallback();
        httpCallback.setConfiguration((HttpConfigurationCallBack) configuration);
        httpCallback.setUrlCallback(urlCallBack);
        httpCallback.setCallBackData(dataToSend.toJSONString());
        httpCallback.setRequestId(requestId);
        httpCallback.setServiceName(serviceName);
        log.info("httpCallback:*****************" + httpCallback);
        httpCallback.setCallbackState(CallbackState.Pending);
        try{
            this.addCallback(httpCallback);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        sendRequestWithRetries(httpCallback);
    }

    @Override
    public List<ICallBackHandler> findAllHttpCallBack() {
        List<CallBack> httpCallbacks = callBackRepository.findAll();
        List<ICallBackHandler> callBackHandlers = httpCallbacks.stream()
                .map(entity -> new HttpCallBackAdapter((HttpCallback) entity))
                .collect(Collectors.toList());
        System.out.println(callBackHandlers);
        return callBackHandlers;
       // return  null;
    }
    @Override
    public List<ICallBackHandler> findAllEmailCallBack() {
        List<CallBack> emailCallbacks = callBackRepository.findAll();
        List<ICallBackHandler> callBackHandlers = emailCallbacks.stream()
                .map(entity -> new EmailCallBackAdapter((EmailCallback) entity))
                .collect(Collectors.toList());
        System.out.println(callBackHandlers);
        return callBackHandlers;
    }
    @Override
    public ICallBackHandler findCallBackByServieNameAndRequestId(String serviceName, int requestId) {
            CallBack callback = callBackRepository.findByServiceNameAndRequestId(serviceName, requestId);
            if (callback instanceof HttpCallback) {
                 return new HttpCallBackAdapter((HttpCallback) callback);
            }else if(callback instanceof EmailCallback){
                 return new EmailCallBackAdapter((EmailCallback) callback);
            }
                //ICallBackHandler callBackHandler = new HttpCallBackAdapter((HttpCallback) httpCallback);
            return null;
    }

    /* creer find all for service code et request id*/

    private void sendRequestWithRetries(HttpCallback httpCallback) {
        HttpCallbackAttempt httpCallbackAttempt =new HttpCallbackAttempt();
        httpCallbackAttempt.setHttpCallback(httpCallback);
        ArrayList<HttpCallbackAttempt> listOfAttempts=new ArrayList<>();
        listOfAttempts.add(httpCallbackAttempt);
        httpCallback.setHttpCallbackAttempts(listOfAttempts);
        URI urlCallback = httpCallback.getUrlCallback();
        JSONObject dataToSend=new JSONObject();
        try {
            dataToSend = new Gson().fromJson(httpCallback.getCallBackData(), JSONObject.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        int maxAttempts = httpCallback.getConfiguration().getMaxAttempts();
        log.info(urlCallback+"    "+dataToSend+"  "+maxAttempts);
        Mono<ClientResponse> responseMono = webClient
                .method(HttpMethod.POST)
                .uri(urlCallback)
                .bodyValue(dataToSend)
                .exchange();
        if (maxAttempts > 0) {
            responseMono.subscribe(response -> {

                        log.info("Status code: " + response.statusCode());
                        response.bodyToMono(String.class).subscribe(body -> {
                            log.info("Body response: " + body);
                            httpCallbackAttempt.setResponseBody(body);
                            httpCallbackAttempt.setResponseStatus(response.statusCode().value());
                            httpCallbackAttempt.setResponseDate(new Date());
                            httpCallback.setCallbackState(CallbackState.Send);
                            callBackRepository.save(httpCallback);
                        });
                    },
                    ex -> {

                        log.error("Request failed: " + ex.getMessage());
                        httpCallbackAttempt.setResponseBody("NOK");
                        httpCallbackAttempt.setResponseStatus(408);
                        httpCallback.getConfiguration().setMaxAttempts(maxAttempts - 1);
                        callBackRepository.save(httpCallback);
                        if(httpCallback.getConfiguration().getMaxAttempts()>0){
                            retryAfterDelay(httpCallback);
                        }
                    }
            );
        } else {
            log.info("Maximum retries exceeded. Giving up.");
            httpCallback.setCallbackState(CallbackState.failure);
        }
    }

    private void retryAfterDelay(HttpCallback httpCallback) {
        int attemptDelay = httpCallback.getConfiguration().getAttemptDelay();
        Duration delayDuration=Duration.ofSeconds(attemptDelay);
        Mono.delay(delayDuration)
                .subscribeOn(Schedulers.parallel())
                .subscribe(
                        delayResult -> {
                            sendRequestWithRetries( httpCallback);
                        },
                        ex -> {
                            log.error(ex.getMessage());
                        }
                );
    }
    private void sendEmail(String fromEmail,String toEmail, String subject, String body){
        SimpleMailMessage message=new SimpleMailMessage();
        System.out.println(message);
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
    }
    private boolean isRequestIdUnique(String serviceName, int requestId) {
        long count = callBackRepository.countByServiceNameAndRequestId(serviceName, requestId);
        return count == 0;
    }

}
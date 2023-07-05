package com.cashplus.callback.internal;

import com.cachplus.callBackHandler.ICallBackConfiguration;
import com.cachplus.callBackHandler.ICallBackHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URI;
import java.util.Collections;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
class HttpCallBackAdapter implements ICallBackHandler{
    @JsonProperty("httpCallback")
    private final HttpCallback httpCallback;

    public HttpCallBackAdapter(HttpCallback httpCallback) {
        this.httpCallback = httpCallback;
    }

    @Override
    public void sendHttpCallBack(String serviceName, int requestId, URI urlCallBack, JSONObject dataToSend, ICallBackConfiguration configuration) {

    }

    @Override
    public List<ICallBackHandler> findAllEmailCallBack() {
        return null;
    }

    @Override
    public List<ICallBackHandler> findAllHttpCallBack() {
        System.out.println("Collections.singletonList(this)"+Collections.singletonList(this));
        return Collections.singletonList(this);
    }

    @Override
    public ICallBackHandler findCallBackByServieNameAndRequestId(String serviceName, int requestId) {
        // Implement the logic to find the HttpCallBack by serviceName and requestId
        // For example:
        if (httpCallback.getServiceName().equals(serviceName) && httpCallback.getRequestId() == requestId) {
            return this;
        }
        return null;
    }
    @Override
    public void sendEmailCallBack(String serviceName, int requestId, String fromEmail, String toEmail, String subject, String body) {

    }
}

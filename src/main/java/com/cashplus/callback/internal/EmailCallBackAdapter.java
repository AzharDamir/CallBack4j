package com.cashplus.callback.internal;

import com.cachplus.callBackHandler.ICallBackConfiguration;
import com.cachplus.callBackHandler.ICallBackHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONObject;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class EmailCallBackAdapter implements ICallBackHandler {
    @JsonProperty("emailCallback")
    private final EmailCallback emailCallback;

    public EmailCallBackAdapter(EmailCallback emailCallback) {
        this.emailCallback = emailCallback;
    }

    @Override
    public void sendHttpCallBack(String serviceName, int requestId, URI urlCallBack, JSONObject dataToSend, ICallBackConfiguration configuration) {

    }

    @Override
    public List<ICallBackHandler> findAllHttpCallBack() {
        return null;
    }

    @Override
    public List<ICallBackHandler> findAllEmailCallBack() {
        System.out.println("Collections.singletonList(this)"+ Collections.singletonList(this));
        return Collections.singletonList(this);
    }

    @Override

    public ICallBackHandler findCallBackByServieNameAndRequestId(String serviceName, int requestId) {
        // Implement the logic to find the HttpCallBack by serviceName and requestId
        // For example:
        if (emailCallback.getServiceName().equals(serviceName) && emailCallback.getRequestId() == requestId) {
            return this;
        }
        return null;
    }

    @Override
    public void sendEmailCallBack(String serviceName, int requestId, String fromEmail, String toEmail, String subject, String body) {

    }
}

package com.cashplus.callback.internal;

import com.cachplus.callBackHandler.ICallBackConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Component("HttpConfigurationCallBack")
class HttpConfigurationCallBack implements ICallBackConfiguration {
    @Column(name = "max_attempts", updatable = false)
    private int maxAttempts;
    @Column(name = "attempt_delay")
    private int attemptDelay;
    @Column(name = "timeout")
    private int timeout;

}

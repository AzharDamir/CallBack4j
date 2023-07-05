package com.cashplus.callback.internal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ConfigurationApp {
    @Bean
    public EmailCallback EmailCallback() {
        // Use a factory method to create and initialize the HttpCallback instance
        return createEmailCallback();
    }

    private EmailCallback createEmailCallback() {
        // Create and initialize the HttpCallback instance here
        return new EmailCallback();
    }
    @Bean
    public HttpCallback httpCallback() {
        // Use a factory method to create and initialize the HttpCallback instance
        return createHttpCallback();
    }

    private HttpCallback createHttpCallback() {
        // Create and initialize the HttpCallback instance here
        return new HttpCallback();
    }

}

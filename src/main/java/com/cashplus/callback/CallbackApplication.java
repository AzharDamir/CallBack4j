package com.cashplus.callback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing
@SpringBootApplication
@ComponentScan({"com.cashplus.callback","com.cachplus.callBackHandler"})
public class CallbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallbackApplication.class, args);
	}

}

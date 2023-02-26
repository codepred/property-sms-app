package com.codepred.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={
		"com.codepred.sms.property.repository", "com.codepred.sms.property.service",
		"com.codepred.sms.property.dto", "com.codepred.sms.property.controller",
		"com.codepred.sms.property.model"})
@EnableJpaRepositories("com.codepred.sms.property.repository")
@EntityScan("com.codepred.sms")
public class SmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

}

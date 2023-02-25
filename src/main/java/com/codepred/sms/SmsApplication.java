package com.codepred.sms;

import com.codepred.sms.pages.repository.PropertyRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={
		"com.codepred.sms.pages.repository", "com.codepred.sms.pages.service",
		"com.codepred.sms.pages.dto", "com.codepred.sms.pages.controller",
		"com.codepred.sms.pages.model"})
@EnableJpaRepositories("com.codepred.sms.pages.repository")
@EntityScan("com.codepred.sms")
public class SmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

}

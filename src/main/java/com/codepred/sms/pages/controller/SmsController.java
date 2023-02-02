package com.codepred.sms.pages.controller;

import com.codepred.sms.pages.repository.SmsRepository;
import com.codepred.sms.pages.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    @Autowired
    SmsService smsService;

    @Autowired
    SmsRepository smsRepository;

    @GetMapping("/load/{url}")
    public ResponseEntity<Object> loadData(@PathVariable("url")String url){
        smsService.loadData(url);
        return ResponseEntity.status(200).body("DATA LOADED");
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllData(){
        return ResponseEntity.status(200).body(smsRepository.findAll());
    }


}
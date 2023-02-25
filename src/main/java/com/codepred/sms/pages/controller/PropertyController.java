package com.codepred.sms.pages.controller;

import com.codepred.sms.pages.repository.PropertyRepository;
import com.codepred.sms.pages.service.PropertyService;
import com.codepred.sms.pages.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    @Autowired
    SmsService smsService;

    @PostMapping("/loadProperties")
    public ResponseEntity<Object> loadData(@RequestParam("url")String url){
        propertyService.loadData(url);
        return ResponseEntity.status(200).body("DATA LOADED");
    }

    @PostMapping("/sendSms/{phone}")
    public ResponseEntity<Object> sendSms(@PathVariable("phone")String phone){
        smsService.sendSms(phone);
        return ResponseEntity.status(200).body("SMS WAS SENT");
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllData(){
        return ResponseEntity.status(200).body(propertyService.getAll());
    }

    @PostMapping("/addUrl")
    public ResponseEntity<Object> addProperty(@RequestParam("url") String url) throws InterruptedException {
        return ResponseEntity.status(200).body(propertyService.addProperty(url));
    }

    @DeleteMapping("/all")
    public ResponseEntity<Object> deleteAll(){
        return ResponseEntity.status(200).body(propertyService.deleteAll());
    }

    @PostMapping("/sendSmsToAll")
    public ResponseEntity<Object> sendSms(){
        propertyService.sendSmsToAll();
        return ResponseEntity.status(200).body("Sms to all was sent");
    }

}
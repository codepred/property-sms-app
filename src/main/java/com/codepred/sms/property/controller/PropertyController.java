package com.codepred.sms.property.controller;

import com.codepred.sms.property.service.OtoDomService;
import com.codepred.sms.property.service.PropertyService;
import com.codepred.sms.property.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    @Autowired
    SmsService smsService;

    @Autowired
    OtoDomService otoDomService;

    @PostMapping("/loadProperties")
    public ResponseEntity<Object> loadData(@RequestParam("url")String url,@RequestParam("area")String area){
        propertyService.loadData(url,area);
        return ResponseEntity.status(200).body("DATA LOADED");
    }

    @GetMapping("/fixnumbers")
    public ResponseEntity<Object> fixNumbers(){
        propertyService.updateDatabaseNumbers();
        return ResponseEntity.status(200).body("FIXED");
    }

    @PostMapping("/sendSms/{phone}")
    public ResponseEntity<Object> sendSms(@PathVariable("phone")String phone){
        return ResponseEntity.status(200).body(smsService.sendSms(phone));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllData(){
        return ResponseEntity.status(200).body(propertyService.getAll());
    }

    @GetMapping("/get/{phone}")
    public ResponseEntity<Object> getAllData(@PathVariable("phone")String phone){
        return ResponseEntity.status(200).body(propertyService.getByPhone(phone));
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


    @PostMapping("/loadPropertiesOto")
    public ResponseEntity<Object> addOtoDomProperty(@RequestParam("url")String url,@RequestParam("area")String area){
        otoDomService.loadData(url,area);
        return ResponseEntity.status(200).body("ADDED");
    }

    @GetMapping("/getOto")
    public ResponseEntity<Object> getPhoneNumberOto(@RequestParam("url")String url){
        return ResponseEntity.status(200).body(otoDomService.getPhoneNumber(url));
    }

}
package com.codepred.sms.pages.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${smsToken}")
    private String smsToken;

    public String sendSms(String phone){
        String url = "https://api.smsapi.pl/sms.do?";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer "+ smsToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity requestEntity = new HttpEntity<>(headers);



        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("from", "KupieMieszk");
        map.add("to", "48" + phone);
        map.add("message", "Dzien dobry, jestem zainteresowany zakupem Twojego mieszkania - bezposrednio i za gotowke. Jesli chcesz porozmawiac o cenie i terminie sprzedazy napisz lub zadzwon na moj numer: 506013306\n" +
                "Pozdrawiam\n" +
                "Mariusz");
        map.add("format","json");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);


        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );


        return response.toString();
    }


}

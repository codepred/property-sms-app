package com.codepred.sms.pages.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${smsToken}")
    private String smsToken;

    public String sendSms(String phone){
        String url = "https://api.smsapi.pl/sms.do";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer "+ smsToken);
        RestTemplate template = new RestTemplate();
        HttpEntity requestEntity = new HttpEntity<>(headers);


        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", "KupieMieszk");
        uriVariables.put("to", "48" + phone);
        uriVariables.put("message", "Dzien dobry, jestem zainteresowany zakupem Twojego mieszkania - bezposrednio i za gotowke. Jesli chcesz porozmawiac o cenie i terminie sprzedazy napisz lub zadzwon na moj numer: 506013306\n" +
                "Pozdrawiam\n" +
                "Mariusz");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, requestEntity, String.class, uriVariables);
        return response.getBody();
    }


}

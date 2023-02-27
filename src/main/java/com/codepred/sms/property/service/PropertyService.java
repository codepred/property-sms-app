package com.codepred.sms.property.service;

import com.codepred.sms.property.dto.ResponseObject;
import com.codepred.sms.property.model.PropertyEntity;
import com.codepred.sms.property.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.*;

@Service
public class PropertyService {

    @Value("${token}")
    private String token;

    @Autowired
    SmsService smsService;


    @Autowired
    PropertyRepository propertyRepository;

    public void loadData(String url, String area){
        Set<String> pages = getPostUrl(url);
        for(String page: pages){
            try {
                PropertyEntity propertyEntity = new PropertyEntity();
                propertyEntity.setPageUrl(page);
                String pageData = getPageData(page);
                String id = getPageId(pageData);
                propertyEntity.setPageId(id);
                propertyEntity.setWasSent(false);
                propertyEntity.setArea(area);
                propertyRepository.save(propertyEntity);
            }catch (Exception e){
                System.out.println("PROPERTY ALREADY IN DATABASE");
            }
        }
    }

    public Iterable<PropertyEntity> getAll(){
        return propertyRepository.findAll();
    }

    public static String getPageData(String url){
        Document document = getDocument(url);
        return document.outerHtml();
    }

    public static Document getDocument(String url) {
        Connection conn = Jsoup.connect(url);
        Document document = null;
        try {
            document = conn.get();
        } catch (Exception e) {
            System.out.println("SITE IS PROTECTED");
            return null;
        }
        return document;
    }

    public String deleteAll(){
        propertyRepository.deleteAll();
        return "WAS DELETED";
    }

    public static Set<String> getPostUrl(String url){
        String pattern = "d/oferta/";
        Set<String> result = new HashSet<>();
        String page = getPageData(url);
        for(int i=0; i<page.length()-100; i++){
            String search = page.substring(i,i+9);
            String flat = "";
            if(search.equals(pattern)){
                int j =i;
                while(page.charAt(j)!='"'){
                    flat+=page.charAt(j);
                    j++;
                }
                result.add("https://www.olx.pl/" + flat);
            }
        }
        return result;
    }

    public Set<String> getPhoneList(Set<String> posts) throws InterruptedException {
        System.out.println(posts.size());
        Set<String> phones = new HashSet<>();
        for(String post: posts){
            String page = getPageData(post);
            String id = getPageId(page);
            System.out.println(getPhone(id,post));
            phones.add(id);
        }
        return phones;
    }

    public static String getPageId(String page){
        String id = "";
        for(int i=0; i<page.length()-100; i++){
            if(page.substring(i,i+29).equals("window.__PRERENDERED_STATE__=")){
                int j = i+29+26;
                while(page.charAt(j)!=','){
                    id+=page.charAt(j);
                    j++;
                }
            }
        }
        return id;
    }

    public String getPhone(String id, String pageUrl) throws InterruptedException {
        String url = "https://www.olx.pl/api/v1/offers/" + id + "/limited-phones/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        headers.set("Authorization", "Bearer "+ token);
        headers.set("User-Agent","PostmanRuntime/7.30.0");
        headers.set("Cookie","PHPSESSID=mg9643dj27puq8rdav0jpvb3hc; newrelic_cdn_name=CF");

        System.out.println("I AM SLEEPING");

        try {
            TimeUnit.SECONDS.sleep(1);
//            ResponseEntity<String> response = restTemplate.exchange(pageUrl, HttpMethod.GET, entity, String.class);
            ResponseEntity<ResponseObject> responseObject = restTemplate.exchange(url, HttpMethod.GET, entity, ResponseObject.class);
            return responseObject.getBody().getData().getPhones().get(0).replaceAll("\\s+","");
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return "test";
    }

    public PropertyEntity addProperty(String url) throws InterruptedException {
        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setPageUrl(url);
        propertyEntity.setWasSent(false);
        String page = getPageData(url);
        String id = getPageId(page);
        String phone = getPhone(id,url);
        propertyEntity.setPageId(id);
        propertyEntity.setPhoneNumber(phone);
        return propertyRepository.save(propertyEntity);
    }

    public void sendSmsToAll(){
        List<PropertyEntity> propertyEntityList = propertyRepository.getAllToSent();
        propertyEntityList.stream().forEach(x -> smsService.sendSms(x.getPhoneNumber()));
        propertyEntityList.stream().forEach(x -> x.setWasSent(true));
        propertyEntityList.stream().forEach(x -> propertyRepository.save(x));
    }

    public PropertyEntity getByPhone(String phone){
        return propertyRepository.getByPhone(phone);
    }



}
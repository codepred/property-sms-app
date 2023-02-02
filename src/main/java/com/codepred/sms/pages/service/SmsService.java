package com.codepred.sms.pages.service;

import com.codepred.sms.pages.dto.ResponseObject;
import com.codepred.sms.pages.model.SmsEntity;
import com.codepred.sms.pages.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class SmsService {

    @Autowired
    SmsRepository smsRepository;

//    public static void main(String[] args) throws InterruptedException {
//        String url = "https://www.olx.pl/d/nieruchomosci/mieszkania/sprzedaz/oswiecim/?page=1&search%5Bfilter_enum_market%5D%5B0%5D=secondary&search%5Bfilter_float_price%3Ato%5D=400000";
//        Set<String> ids = getPhoneList(getPostUrl(url));
//    }

    public void loadData(String url){
        Set<String> pages = getPostUrl(url);
        for(String page: pages){
            SmsEntity smsEntity = new SmsEntity();
            smsEntity.setPageUrl(page);
            String id = getPageId(page);
            smsEntity.setPageId(id);
            smsEntity.setWasSent(false);
            smsRepository.save(smsEntity);
        }
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

    public static Set<String> getPostUrl(String url){
        String pattern = "https://www.olx.pl/d/oferta/";
        int size = 28;
        Set<String> result = new HashSet<>();
        String page = getPageData(url);
        for(int i=0; i<page.length()-100; i++){
            String search = page.substring(i,i+28);
            String flat = "";
            if(search.equals(pattern)){
                int j =i;
                while(page.charAt(j)!='"'){
                    flat+=page.charAt(j);
                    j++;
                }
                result.add(flat);
            }
        }
        return result;
    }

    public static Set<String> getPhoneList(Set<String> posts) throws InterruptedException {
        System.out.println(posts.size());
        posts.remove("https://www.olx.pl/d/oferta/kawalerka-po-generalnym-remoncie-umeblowana-i-wyposazona-w-rtv-i-agd-CID3-IDQDTTZ.html#e9ed41427f");
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

    public static String getPhone(String id, String pageUrl) throws InterruptedException {
        String token = "eyJraWQiOiJSTWxVTFJrZXlcLzFXUzQ5eTVwSXdLTFdpakFZUjd5UWVNZ00rdktEemVBYz0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiRnhFalBRcTUtS3VXTjY2Rzh2NWY0USIsInN1YiI6IjFiZjY1ZmI5LTBjMWEtNGM5OS1hZGE4LTcyMDE3OGM0YjI2ZCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtd2VzdC0xLmFtYXpvbmF3cy5jb21cL2V1LXdlc3QtMV9kVWpGdXZUZjQiLCJjb2duaXRvOnVzZXJuYW1lIjoiOTA1ZGNkNWItNDdmNC00NzhlLTkzODItYmVhZjY0ZTEwOTEwIiwib3JpZ2luX2p0aSI6IjVhYWY4MDM2LTRmNTItNDZhZS05ZWQ1LWVhMDEyMWUzZjk3MCIsImF1ZCI6ImIwbGNuYnNuODJrdnJ0azc2N25uOHBnMWsiLCJpZGVudGl0aWVzIjpbeyJ1c2VySWQiOiIxMDk2NTA0ODM2MDIxNzUzNjQ5NzIiLCJwcm92aWRlck5hbWUiOiJHb29nbGUiLCJwcm92aWRlclR5cGUiOiJHb29nbGUiLCJpc3N1ZXIiOm51bGwsInByaW1hcnkiOiJmYWxzZSIsImRhdGVDcmVhdGVkIjoiMTY1OTg4MzM3ODY4NyJ9LHsidXNlcklkIjoiMTE1MDA3MTkwMDIzNjc1MjY1OTQ4IiwicHJvdmlkZXJOYW1lIjoiR29vZ2xlIiwicHJvdmlkZXJUeXBlIjoiR29vZ2xlIiwiaXNzdWVyIjpudWxsLCJwcmltYXJ5IjoiZmFsc2UiLCJkYXRlQ3JlYXRlZCI6IjE2NzIzNjUyNjI1MzIifV0sInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjc1MjYxMDE0LCJjdXN0b206aWRwX2xhc3RfZW1haWwiOiJpbmZpbml0ZS50ZWNoLnBvem5hbkBnbWFpbC5jb20iLCJleHAiOjE2NzUyNzE3MjgsImlhdCI6MTY3NTI3MDgyOCwianRpIjoiZjk0ODZlNTAtZWE2ZC00MTJlLWE0YmMtNDNlNGMxZDM3YmVlIiwiZW1haWwiOiJjb2RlcHJlZHBvem5hbkBnbWFpbC5jb20ifQ.RnVMTHMfzIdJCRFYbL8VqdpjtvwDbQaqqky0ZDPKDj9iE_JxcsocWJmDTeL1yWHxUw7ND_Na2UOOEIasmuF22wKN_hHWzOwT_SlaRhfOga4YK4v49nr_KiJepR31ftxejoCe_CCs18zGTQSHodDUH8XZsBfdUeHMVgSSuBvcl_-qKgAKsx5Zo3JA9spdL9ViNq-zEDLuYzuiy1dO1yyOezf1DX48CeXZOcKPhlDRd9Qjz86rZCV1EHu9NA93NTGNQ5JDrzr-ytltMNZunxjAkbhPUfpBakyMfvKFwDv99crraHDFTJp205xdgK4U0kTydHgLB1hN4FPL-bUa1nuJ2Q";
        String url = "https://www.olx.pl/api/v1/offers/" + id + "/limited-phones/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        headers.set("Authorization", "Bearer "+token);
        headers.set("User-Agent","PostmanRuntime/7.30.0");
        headers.set("Cookie","PHPSESSID=mg9643dj27puq8rdav0jpvb3hc; newrelic_cdn_name=CF");

        System.out.println("I AM SLEEPING");

        try {
            TimeUnit.SECONDS.sleep(35);
            ResponseEntity<String> response = restTemplate.exchange(pageUrl, HttpMethod.GET, entity, String.class);
            response = restTemplate.exchange(pageUrl, HttpMethod.GET, entity, String.class);
            response = restTemplate.exchange(pageUrl, HttpMethod.GET, entity, String.class);
            ResponseEntity<ResponseObject> responseObject = restTemplate.exchange(url, HttpMethod.GET, entity, ResponseObject.class);
            return responseObject.getBody().getData().getPhones().get(0).replaceAll("\\s+","");
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return "test";
    }

}
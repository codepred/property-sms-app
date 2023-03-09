package com.codepred.sms.property.service;


import com.codepred.sms.property.model.PropertyEntity;
import com.codepred.sms.property.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OtoDomService {

    @Autowired
    PropertyService propertyService;

    @Autowired
    PropertyRepository propertyRepository;

    public Set<String> getPages(String url){
        String page = propertyService.getPageData(url);
        String pattern = "\"slug\":\"";
        Set<String> result = new HashSet<>();
        for(int i=0; i<page.length()-100; i++){
            String search = page.substring(i,i+8);
            String flat = "";
            if(search.equals(pattern)){
                int j =i+8;
                while(page.charAt(j)!='"'){
                    flat+=page.charAt(j);
                    j++;
                }
                result.add("https://www.otodom.pl/pl/oferta/" + flat);
            }
        }
        return result;
    }

    public String getPhoneNumber(String url){
        String page = propertyService.getPageData(url);
        String pattern = "\"phones\":[\"+48";
        for(int i=0; i<page.length()-100; i++){
            String search = page.substring(i,i+14);
            String flat = "";
            if(search.equals(pattern)){
                int j =i+14;
                while(page.charAt(j)!='"'){
                    flat+=page.charAt(j);
                    j++;
                }
                return flat;
            }
        }
        return "000000000";

    }

    public void loadData(String url, String area){
        Set<String> pages = getPages(url);
        for(String page: pages){
            PropertyEntity propertyEntity = new PropertyEntity();
            propertyEntity.setPageUrl(page);
            propertyEntity.setWasSent(false);
            propertyEntity.setPhoneNumber(getPhoneNumber(page));
            propertyEntity.setArea(area);
            propertyRepository.save(propertyEntity);
        }
    }

}

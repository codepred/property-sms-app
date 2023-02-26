package com.codepred.sms.property.controller;

import com.codepred.sms.property.model.PropertyEntity;
import com.codepred.sms.property.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HtmlController {

    @Autowired
    PropertyRepository propertyRepository;

    @GetMapping(value = "/welcome", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String welcomeAsHTML() {
        return "<html>\n" + "<header><title>Aplikacja nieruchomości sms</title></header>\n" +
                "<body>\n"
                + "<h1>Wyszukaj ogłoszenie po numerze telefonu</h1>" +
                "<form action=\"http:localhost/phone/{number}\">\n" +
                "  <label for=\"number\">Telefon:</label>\n" +
                "  <input type=\"text\" id=\"number\" name=\"number\"><br><br>" +
                "    <input type=\"submit\" value=\"Pobierz dane\" />\n" +
                "</form>" +
                "\n" + "</body>\n" + "</html>";
    }

    @GetMapping(value = "/{number}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String phoneData(@PathVariable String number) {
        PropertyEntity propertyEntity = propertyRepository.getByPhone(number);
        if(propertyEntity == null){
            return "<html><h1>NUMER NIE ISTNIEJE</h1></hmtl>";
        }

        return "<html>\n" + "<header><title>Dane numeru</title></header>\n" +
                "<body>\n" +
                "<h2>Dane ogłoszenia</h2><br><br><br>\n" +
                "\n" +
                "<table style='font-family:\"Courier New\", Courier, monospace; font-size:350%'>\n" +
                "  <tr>\n" +
                "    <th>Nazwa właściwości</th>\n" +
                "    <th>Dane ogłoszenia</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Czas dodania do bazy danych</td>\n" +
                "    <td>" + propertyEntity.getCreatedAt() + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Numer telefonu</td>\n" +
                "    <td>" + propertyEntity.getPhoneNumber() + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Link do ogłoszenia</td>\n" +
                "    <td>" + "<a href=" + propertyEntity.getPageUrl() + ">Ogłoszenie olx</a></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Obszar</td>\n" +
                "    <td>"  + propertyEntity.getArea() + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Czy został wysłany sms do klienta?</td>\n" +
                "    <td>" + propertyEntity.isWasSent() + "</td>\n" +
                "  </tr>\n" +
                "</table>" +
                "\n" + "</body>\n" + "</html>";
    }

    @GetMapping(value = "/test", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String test() {
        return "<html>\n" + "<h1>Wyszukaj numer telefonu</h1><form style=\"font-size: 100px;width: 800px;\">" +
                "    <input style=\"font-size: 100px;width: 800px;\" id=\"projectId\" maxlength=\"9\">\n" +
                "    <input style=\"font-size: 100px;width: 800px;\" onclick=\"return findProject()\" type=\"submit\" value=\"Wyszukaj\">\n" +
                "</form>\n" +
                "\n" +
                "<script>\n" +
                "    function findProject(){\n" +
                "        window.location = document.getElementById(\"projectId\").value;\n" +
                "        return false;\n" +
                "    }\n" +
                "</script>" + "</html>";
    }


}

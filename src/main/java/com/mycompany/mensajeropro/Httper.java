/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mensajeropro;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.net.http.HttpRequest.BodyPublishers;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 *
 * @author andres
 */
public class Httper {

    // one instance, reuse
    private HttpClient httpClient;
    private final String basicUrl = "https://api.backendless.com/9176FE65-2FB5-2B00-FFED-BEB6A480BC00/0397420A-AA65-4BA2-9A1F-D4C9583099C8";
//    Exception wrongCredentials = new Exception();
    private String token = "";

    public Httper() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public void logIn(String email, String password) throws Exception, WrongCredentials {

        // form parameters
        Map<Object, Object> data = new HashMap<>();
        data.put("login", email);
        data.put("password", password);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(requestBody))
                .uri(URI.create(basicUrl + "/users/login"))
                .setHeader("content-Type", "application/json") // add request header
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 401) {
            throw new WrongCredentials();
        } else if (response.statusCode() != 200) {
            throw new Exception();
        }

        JsonNode jsonNode = objectMapper.readTree(response.body());

        String id = jsonNode.get("ownerId").asText();
        token = jsonNode.get("user-token").asText();

        //Update ip
        String ip = Util.getMyIP();
        ip = ip.substring(1, ip.length());
        System.out.println("ip to send"+ ip);
                
        Map<Object, Object> data2 = new HashMap<>();
        data2.put("last_ip", ip);
        String requestBody2 = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(data2);

        HttpRequest request2 = HttpRequest.newBuilder()
                .PUT(BodyPublishers.ofString(requestBody2))
                .uri(URI.create(basicUrl + "/data/Users/" + id))
                .setHeader("content-Type", "application/json")
                .header("user-token", token)
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        if (response2.statusCode() != 200) {
            throw new Exception();
        }

    }

    public String getIp(String email) throws Exception {

        String url = basicUrl + "/data/Users?where=email%3D%27" + email + "%27";
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("content-Type", "application/json")
                .header("user-token", token)// add request header
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        
        String ip = jsonNode.get(0).get("last_ip").asText();

        return ip;
    }

    public String getToken() {
        return token;
    }
}

package com.Applemango_Backend.search.service;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private String server_url = "https://dapi.kakao.com/v2/local/search/keyword.JSON?category_group_code=FD6&size=10&query=";
    private String key = "KakaoAK 3820639f4f307d1a67eb4f32690596f0";

    public String categorization(String keyword,String classify) throws Exception{
        String result = "";
        //Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", key);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        URI url = URI.create(server_url + URLEncoder.encode(keyword,"UTF-8"));
        RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, url);
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
            JSONArray documents = (JSONArray) jsonObject.get("documents");
            for(int i = 0; i < 10; i++) {
                JSONObject tmp = (JSONObject) documents.get(i);
                String categorys = String.valueOf(tmp.get("category_name"));
                String category = categorys.split(" > ")[1];
                System.out.println(category);
                if(category == classify) {
                    System.out.println(tmp);
                    result += tmp.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print(result);
        return result;
    }
}

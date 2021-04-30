package com.zhouxiaoge.dag.restful;

import com.alibaba.fastjson.JSONObject;
import com.zhouxiaoge.dag.models.Task;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestfulTest {


    @Test
    public void startRestfulDag() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/dag/start/restful";
        ResponseEntity<?> forEntity = restTemplate.getForEntity(url, List.class);
        System.out.println(forEntity.getBody());
    }

    @Test
    public void invokingRestfulDag() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/restful/invoking";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dagKey", "restful");
        Map<String, Object> map = new HashMap<>();
        map.put("ID", "1");
        map.put("NAME", "1");
        map.put("AGE", "1");
        map.put("THREAD_NAME", "1");
        map.put("SEX", "1");
        jsonObject.put("parameterMap", map);
        String s = jsonObject.toString();
        System.out.println(s);
        HttpEntity<String> requestEntity = new HttpEntity<>(s, headers);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        System.out.println(exchange);
    }
}

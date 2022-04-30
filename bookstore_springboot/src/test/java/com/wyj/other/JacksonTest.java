package com.wyj.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Map;


@Data
public class JacksonTest {
    private String x1;
    private Test test;
    @Data
    public static class Test{
        @JsonProperty("x3")
        private String x2;
    }


    public static void main(String[] args) throws JsonProcessingException {
//        ObjectMapper objectMapper=new ObjectMapper();
//        String x="{\"x1\":\"123\",\"test\":{\"x3\":\"567\"}}";
//        JacksonTest jacksonTest = objectMapper.readValue(x, JacksonTest.class);
//        System.out.println(jacksonTest);
        Map<Long,String> map=Map.of(1L,"3");
        Map<Integer,String> map2=Map.of(1,"3");
        System.out.println(map.get(1));
        System.out.println(map.get(1L));
        System.out.println(map2.get(1));
        System.out.println(map2.get(1L));
    }
}

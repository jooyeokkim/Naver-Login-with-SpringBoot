package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetUserInfoService {
    private final String HTTP_REQUEST = "https://kapi.kakao.com/v2/user/me";

    public String getUserInfo(String accessToken){
        try {
            String jsonData = "";

            // URI를 URL객체로 저장
            URL url = new URL(HTTP_REQUEST + "?access_token=" + accessToken);

            // 버퍼 데이터(응답 메세지)를 한 줄씩 읽어서 result에 저장
            // result에는 XML 형식의 응답 데이터가 String으로 저장되어 있음
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while((line = bf.readLine()) != null){
                jsonData+=line;
            }
            return jsonData;

        } catch(Exception e) {
            return "success";
        }
    }
}

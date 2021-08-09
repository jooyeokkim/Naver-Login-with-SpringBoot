package com.example.demo.controller;

import com.example.demo.service.GetUserInfoService;
import com.example.demo.service.RestJsonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String state = generateState();
        String encodedState="";
        try {
            encodedState = URLEncoder.encode(state, "UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        session.setAttribute("state", encodedState);
        session.setMaxInactiveInterval(60*60);

        model.addAttribute("state", encodedState);
        return "home";
    }

    @GetMapping("/receiveAC")
    public String receiveAC(@RequestParam("code") String code, Model model) {
        RestJsonService restJsonService = new RestJsonService();

        //access_token이 포함된 JSON String을 받아온다.
        String accessTokenJsonData = restJsonService.getAccessTokenJsonData(code);
        if(accessTokenJsonData=="error") return "error";

        //JSON String -> JSON Object
        JSONObject accessTokenJsonObject = new JSONObject(accessTokenJsonData);

        //access_token 추출
        String accessToken = accessTokenJsonObject.get("access_token").toString();


        //유저 정보가 포함된 JSON String을 받아온다.
        GetUserInfoService getUserInfoService = new GetUserInfoService();
        String userInfo = getUserInfoService.getUserInfo(accessToken);

        //JSON String -> JSON Object
        JSONObject userInfoJsonObject = new JSONObject(userInfo);

        //유저의 Email 추출
        JSONObject responseJsonObject = (JSONObject)userInfoJsonObject.get("response");
        String email = responseJsonObject.get("email").toString();

        model.addAttribute("email", email);

        return "success";
    }

    public String generateState()
    {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}

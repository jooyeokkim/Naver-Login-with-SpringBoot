package com.example.demo.controller;

import com.example.demo.service.CalendarListService;
import com.example.demo.service.EventListService;
import com.example.demo.service.RestJsonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONObject;

@Controller
public class HomeController {
    private final String ENCODED_HOLIDAY_CALENDAR_ID = "ko.south_korea%23holiday%40group.v.calendar.google.com";
    private final String ENCODED_BIRTHDAY_CALENDAR_ID = "addressbook%23contacts%40group.v.calendar.google.com";

    @GetMapping("/home")
    public String home(Model model){
        return "home";
    }

    @GetMapping("/receiveAC")
    public String receiveAC(@RequestParam("code") String code, Model model){
        RestJsonService restJsonService = new RestJsonService();
        String accessTokenJsonData = restJsonService.getAccessTokenJsonData(code);

        JSONObject accessTokenjsonObject = new JSONObject(accessTokenJsonData);

        String accessToken = accessTokenjsonObject.get("access_token").toString();
        System.out.println(accessToken);

        // Get CalendarList
        CalendarListService calendarListService = new CalendarListService();
        String calendarListJsonData = calendarListService.getCalendarList(accessToken);
        if(calendarListJsonData=="error") return "error";
        else System.out.println("CalendarList Json Data : " + calendarListJsonData);


        // Get EventList
        // Get Primary Calendar
        EventListService eventListService = new EventListService();
        String userJsonData = eventListService.getMyCalendar(accessToken, "primary");
        if(userJsonData=="error") return "error";
        else System.out.println("User Json Data : " + userJsonData);

        // Get Holiday Calendar
        String holidayJsonData = eventListService.getMyCalendar(accessToken, ENCODED_HOLIDAY_CALENDAR_ID);
        if(holidayJsonData=="error") return "error";
        else System.out.println("Holiday Json Data : " + holidayJsonData);

        // Get birthday Calendar
        String birthdayJsonData = eventListService.getMyCalendar(accessToken, ENCODED_BIRTHDAY_CALENDAR_ID);
        if(calendarListJsonData=="error") return "error";
        else System.out.println("Birthday Json Data : " + birthdayJsonData);

        return "success";
    }
}

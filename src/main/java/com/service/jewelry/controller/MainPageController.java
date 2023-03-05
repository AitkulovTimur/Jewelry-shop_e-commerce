package com.service.jewelry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main-page")
public class MainPageController {
    private static final String HTML_MAIN_NAME = "index";

    @GetMapping
    public String getMainPage() {
        return HTML_MAIN_NAME;
    }
}

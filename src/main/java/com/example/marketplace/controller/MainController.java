package com.example.marketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ankurmundra
 * October 16, 2021
 */
@Controller
public class MainController {

    @GetMapping("")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

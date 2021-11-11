package com.example.marketplace.controller;

import com.example.marketplace.util.ApplicationDao;
import com.example.marketplace.util.QueryDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

/**
 * @author ankurmundra
 * October 16, 2021
 */
@Controller
public class MainController {

    @GetMapping("")
    public String showHomePage(Model model) {
        Set<String> queries = ApplicationDao.queries.keySet();
        model.addAttribute("queries",queries);
        model.addAttribute("queryDto",new QueryDTO());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

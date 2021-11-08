package com.example.marketplace.controller;

import com.example.marketplace.model.Brands;
import com.example.marketplace.model.Customer;
import com.example.marketplace.model.MarketPlaceUser;
import com.example.marketplace.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;

/**
 * @author ankurmundra
 * October 22, 2021
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    public RegistrationController(UserService userService, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public String registrationForm(Model model) {
        model.addAttribute("marketUser", new MarketPlaceUser());
        return "register";
    }

    @PostMapping
    public String registerAccount(@ModelAttribute MarketPlaceUser user) {
        return user.getRole().equals("BRAND") ? "redirect:/register/brand" : "redirect:/register/customer";
    }

    @GetMapping("/customer")
    public String registrationForCustomer(Model model) {
        model.addAttribute("marketUser", new MarketPlaceUser());
        model.addAttribute("customer", new Customer());
        return "customer_register";
    }

    @PostMapping("/customer")
    public String registerCustomer(@ModelAttribute MarketPlaceUser user, @ModelAttribute Customer customer) {
        user.setRoleId(customer.getCustomerId());
        user.setRole("CUSTOMER");
        userService.save(user);
        jdbcTemplate.update("INSERT INTO CUSTOMER(CUSTOMER_ID, NAME, PHONE, ADDRESS,WALLET_ID) VALUES(?, ?, ?, ?,?)",
                customer.getCustomerId(), customer.getName(),customer.getPhone(),
                customer.getAddress(), customer.getWalletId());
        return "redirect:/register/customer?success";
    }

    @GetMapping("/brand")
    public String registrationForBrand(Model model) {
        model.addAttribute("marketUser", new MarketPlaceUser());
        model.addAttribute("brand", new Brands());
        return "brand_register";
    }

    @PostMapping("/brand")
    public String registerBrand(@ModelAttribute MarketPlaceUser user, @ModelAttribute Brands brand) {
        user.setRoleId(brand.getBrandId());
        user.setRole("BRAND");
        userService.save(user);
        jdbcTemplate.update("INSERT INTO BRANDS(BRAND_ID, BRAND_NAME, ADDRESS, JOIN_DATE) VALUES(?, ?, ?, ?)",
                brand.getBrandId(), brand.getBrandName(), brand.getAddress(), new Date(System.currentTimeMillis()));
        return "redirect:/register/brand/?success";
    }
}

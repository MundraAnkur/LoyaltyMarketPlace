package com.example.marketplace.controller;

import com.example.marketplace.model.ActivityCategory;
import com.example.marketplace.model.Brands;
import com.example.marketplace.model.Customer;
import com.example.marketplace.model.CustomerProgramStatus;
import com.example.marketplace.model.LoyaltyProgram;
import com.example.marketplace.model.RERules;
import com.example.marketplace.model.RRRules;
import com.example.marketplace.model.RewardCategory;
import com.example.marketplace.model.Tiers;
import com.example.marketplace.model.Wallet;
import com.example.marketplace.repository.ApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ankurmundra
 * November 06, 2021
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final JdbcTemplate jdbcTemplate;

    public AdminController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("")
    public String admin() {
        return "admin_landing";
    }

    @GetMapping("/customer_info")
    public String getCustomerInfo(Model model)
    {
        List<Customer> customerList = ApplicationDao.getAllCustomers(jdbcTemplate);
        model.addAttribute("customerList",customerList);
        model.addAttribute("customer",new Customer());
        return "customer_info";
    }

    @PostMapping("/customer_info")
    public String showCustomerInfo(@ModelAttribute("customer") Customer customer, Model model)
    {
        List<Wallet> walletData = ApplicationDao.viewWalletData(jdbcTemplate,customer.getCustomerId());
        List<CustomerProgramStatus> customerProgramStatusList =
                ApplicationDao.getCustomerStatus(jdbcTemplate,customer.getCustomerId());
        Customer customerInfo = ApplicationDao.getCustomer(jdbcTemplate,customer.getCustomerId());
        model.addAttribute("customerInfo",customerInfo);
        model.addAttribute("walletData",walletData);
        model.addAttribute("customerProgramData",customerProgramStatusList);
        return "customer_info";
    }

    @GetMapping("/brand_info")
    public String getBrandInfo(Model model)
    {
        List<Brands> brandList = ApplicationDao.getAllBrands(jdbcTemplate);
        model.addAttribute("brandList",brandList);
        model.addAttribute("brand",new Brands());
        return "brand_info";
    }

    @PostMapping("/brand_info")
    public String showBrandInfo(@ModelAttribute("brand") Brands brands, Model model)
    {
        Brands brandInfo = ApplicationDao.getBrand(jdbcTemplate,brands.getBrandId());
        LoyaltyProgram program = ApplicationDao.getLoyaltyProgram(jdbcTemplate,brands.getBrandId());
        List<Tiers> tiersList = new ArrayList<>();
        if(program.getIsTiered() == 1)
            tiersList = ApplicationDao.getAllTiersForProgram(jdbcTemplate,program.getCode());
        List<RERules> reRulesList = ApplicationDao.getAllRERulesForProgram(jdbcTemplate,program.getCode());
        List<RRRules> rrRulesList = ApplicationDao.getAllRRRulesForProgram(jdbcTemplate,program.getCode());

        model.addAttribute("brandInfo",brandInfo);
        model.addAttribute("program",program);
        model.addAttribute("tiersList",tiersList);
        model.addAttribute("reRulesList",reRulesList);
        model.addAttribute("rrRulesList",rrRulesList);
        return "brand_info";
    }

    @GetMapping("/add_activity")
    public String addActivity(Model model) {
        ActivityCategory activityCategory = new ActivityCategory();
        model.addAttribute("activityCategory", activityCategory);
        return "add_activity_type";
    }

    @PostMapping("/add_activity")
    public String submitForm(@ModelAttribute("activityCategory") ActivityCategory activityCategory)
    {
        ApplicationDao.saveActivityCategory(jdbcTemplate,activityCategory);
        return "redirect:/admin/add_activity?success";
    }

    @GetMapping("/add_reward")
    public String addReward(Model model) {
        model.addAttribute("rewardCategory", new RewardCategory());
        return "add_reward_type";
    }

    @PostMapping("/add_reward")
    public String submitForm(@ModelAttribute("rewardCategory") RewardCategory rewardCategory)
    {
        ApplicationDao.saveRewardCategory(jdbcTemplate,rewardCategory);
        return "redirect:/admin/add_reward?success";
    }
}

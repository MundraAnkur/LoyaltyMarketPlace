package com.example.marketplace.controller;

import com.example.marketplace.model.Activity;
import com.example.marketplace.model.LoyaltyProgram;
import com.example.marketplace.model.MarketPlaceUser;
import com.example.marketplace.model.RERules;
import com.example.marketplace.model.RRRules;
import com.example.marketplace.model.Reward;
import com.example.marketplace.model.Tiers;
import com.example.marketplace.repository.UserJpaRepository;
import com.example.marketplace.util.ApplicationDao;
import com.example.marketplace.util.TiersSetupDTO;
import com.example.marketplace.util.ValidateLoyaltyProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {
    private final JdbcTemplate jdbcTemplate;
    private String addedLoyaltyProgram;

    @Autowired
    private UserJpaRepository userService;

    public BrandController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public String brand() {
        return "brand_landing";
    }

    @GetMapping("/add_loyalty_program")
    public String addLoyaltyProgram(Model model) {
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram();
        model.addAttribute("loyaltyProgram", loyaltyProgram);
        return "add_loyalty_program";
    }

    @PostMapping("/add_loyalty_program")
    public String submitForm(@ModelAttribute("loyaltyProgram") LoyaltyProgram loyaltyProgram) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        loyaltyProgram.setBrandId(brandId);
        addedLoyaltyProgram = loyaltyProgram.getCode();
        ApplicationDao.saveLoyaltyProgram(jdbcTemplate, loyaltyProgram);
        return "add_loyalty_program_tier_status";

    }

    @GetMapping("/add_is_tiered_regular")
    public String addLoyaltyProgramTierStatus() {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);
        jdbcTemplate.update("UPDATE LOYALTY_PROGRAM set IS_TIERED = 0 WHERE CODE = ?", code);
        return "activity_reward_menu";
    }

    @GetMapping("/add_is_tiered_tier")
    public String addLoyaltyProgramTierStatusTier() {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);
        jdbcTemplate.update("UPDATE LOYALTY_PROGRAM set IS_TIERED = 1 WHERE CODE = ?", code);
        return "add_tier_menu";
    }

    @GetMapping("/tier_menu")
    public String getTierMenu() {
        return "add_tier_menu";
    }

    @GetMapping("/add_number_of_tiers")
    public String addNumTiers(Model model) {
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram();
        List<Integer> options = new ArrayList<>();
        options.add(2);
        options.add(3);
        model.addAttribute("loyaltyProgram", loyaltyProgram);
        model.addAttribute("options", options);
        return "add_num_tiers";
    }

    @PostMapping("/add_number_of_tiers")
    public String submitAddNumTiers(@ModelAttribute("loyaltyProgram") LoyaltyProgram loyaltyProgram, Model model) {
        int option = loyaltyProgram.getIsTiered();
        TiersSetupDTO tiersSetupDTO = new TiersSetupDTO();
        for (int i = 0; i < option; i++)
            tiersSetupDTO.addTier(new Tiers());
        model.addAttribute("tiersDto", tiersSetupDTO);
        return "tiers";
    }

    @GetMapping("/add_tier_info")
    public String addTiers(Model model)
    {
        model.addAttribute("tiersDto", new TiersSetupDTO());
        return "tiers";
    }

    @PostMapping("/add_tier_info")
    public String submitAddTierInfo(@ModelAttribute("tiersDto") TiersSetupDTO tiersSetupDTO) {
        tiersSetupDTO.getTiersList().forEach(tier -> tier.setLpCode(addedLoyaltyProgram));
        ApplicationDao.addTiers(jdbcTemplate, tiersSetupDTO.getTiersList());
        return "redirect:/brand/add_tier_info?success";
    }

    @GetMapping("/add_activity_brand_regular")
    public String addActivity(Model model) {
        Activity activity = new Activity();
        model.addAttribute("activityType", activity);
        List<String> activityList = ApplicationDao.getAllActivities(jdbcTemplate);
        System.out.println("In add brand activity : " + activityList);
        model.addAttribute("activityList", activityList);
        return "add_brand_activity_type";
    }


    @PostMapping("/add_activity_brand_regular")
    public String submitForm(@ModelAttribute("activityType") Activity activityType) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);

        String[] activities = activityType.getActivityName().split("[,]");
        for (String activityName : activities) {
            String activityId = ApplicationDao.getActivityId(jdbcTemplate, activityName);
            ApplicationDao.enrollBrandActivities(jdbcTemplate, activityId, activityName, code);
        }
        return "activity_reward_menu";
    }

    @GetMapping("/add_reward_brand_regular")
    public String addReward(Model model) {
        List<String> rewardsList = ApplicationDao.getAllRewards(jdbcTemplate);
        Reward reward = new Reward();
        model.addAttribute("rewardType", reward);
        model.addAttribute("rewardsList", rewardsList);
        return "add_brand_reward_type";
    }

    @PostMapping("/add_reward_brand_regular")
    public String submitFormReward(@ModelAttribute("rewardType") Reward rewardType) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);
        String[] rewards = rewardType.getRewardName().split("[,]");
        for (String rewardName : rewards) {
            String rewardId = ApplicationDao.getRewardId(jdbcTemplate, rewardName);
            ApplicationDao.enrollBrandRewards(jdbcTemplate, rewardId, rewardName, code);
        }
        return "activity_reward_menu";
    }

    @GetMapping("/add_RE_rules")
    public String addRERules(Model model) {
        RERules reRules = new RERules();
        model.addAttribute("reRules", reRules);
        return "add_re_rules";
    }

    @PostMapping("/add_RE_rules")
    public String submitAddRERules(@ModelAttribute("reRules") RERules reRules) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);
        reRules.setLpCode(code);
        reRules.setVersion(1);
        ApplicationDao.addRERule(jdbcTemplate, reRules);
        return "redirect:/brand/add_RE_rules?success";
    }

    @GetMapping("/add_RR_rules")
    public String addRRRules(Model model) {
        RRRules rrRules = new RRRules();
        model.addAttribute("rrRules", rrRules);
        return "add_rr_rules";
    }

    @PostMapping("/add_RR_rules")
    public String submitAddRRRules(@ModelAttribute("rrRules") RRRules rrRules) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);
        rrRules.setLpCode(code);
        rrRules.setVersion(1);
        ApplicationDao.addRRRule(jdbcTemplate, rrRules);
        return "redirect:/brand/add_RR_rules?success";
    }

    @GetMapping("/update_RR_rules")
    public String updateRRRules(Model model) {
        RRRules rrRules = new RRRules();
        model.addAttribute("rrRules", rrRules);
        return "update_rr_rules";
    }

    @PostMapping("/update_RR_rules")
    public String submitUpdateRRRules(@ModelAttribute("rrRules") RRRules rrRules) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);
        int version = ApplicationDao.getLatestVersionRR(jdbcTemplate, rrRules.getRuleCode()) + 1;
        rrRules.setLpCode(code);
        rrRules.setVersion(version);
        ApplicationDao.addRRRule(jdbcTemplate, rrRules);
        return "redirect:/brand/update_RE_rules?success";

    }

    @GetMapping("/update_RE_rules")
    public String updateRERules(Model model) {
        RERules reRules = new RERules();
        model.addAttribute("reRules", reRules);
        return "update_re_rules";
    }

    @PostMapping("/update_RE_rules")
    public String submitUpdateRERules(@ModelAttribute("reRules") RERules reRules) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        String code = ApplicationDao.getLoyaltyProgramCodeFromBrandId(jdbcTemplate, brandId);
        int version = ApplicationDao.getLatestVersion(jdbcTemplate, reRules.getRuleCode()) + 1;
        reRules.setLpCode(code);
        reRules.setVersion(version);
        ApplicationDao.addRERule(jdbcTemplate, reRules);
        return "redirect:/brand/update_RE_rules?success";
    }

    @GetMapping("/validate_loyalty_program")
    public String validateLoyaltyProgram() {
        return "validate_loyalty_program";
    }

    @PostMapping("/validate_loyalty_program")
    public String validateProgram(Model model) {
        String brandId = ApplicationDao.getLoggedInCustomerOrBrand(jdbcTemplate);
        ValidateLoyaltyProgram validateLoyaltyProgram = new ValidateLoyaltyProgram(jdbcTemplate, brandId);
        List<String> defects = validateLoyaltyProgram.validate();
        if(defects.isEmpty())
            jdbcTemplate.update("UPDATE LOYALTY_PROGRAM SET IS_VALIDATED = 1 WHERE CODE = ?",validateLoyaltyProgram.getLpCode());
        model.addAttribute("defects", defects);
        return "validate_loyalty_program";
    }
}

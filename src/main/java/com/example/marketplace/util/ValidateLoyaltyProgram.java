package com.example.marketplace.util;

import com.example.marketplace.model.LoyaltyProgram;
import com.example.marketplace.model.RERules;
import com.example.marketplace.model.RRRules;
import com.example.marketplace.model.Tiers;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ankurmundra
 * November 08, 2021
 */
public class ValidateLoyaltyProgram {
    private final JdbcTemplate jdbcTemplate;
    private final LoyaltyProgram program;
    private final List<String> programValidationFlows;

    public ValidateLoyaltyProgram(JdbcTemplate jdbcTemplate, String brandId) {
        this.jdbcTemplate = jdbcTemplate;
        program = ApplicationDao.getLoyaltyProgram(jdbcTemplate,brandId);
        programValidationFlows = new ArrayList<>();
    }

    public String getLpCode()
    {
        return program.getCode();
    }

    public List<String> validate()
    {
        if(program.getIsValidated()==1)
            programValidationFlows.add("Loyalty Program " + program.getName() + " is already Validated");
        else {
            validateTierStatus();
            validateTiers();
            validateRewardAndRewardRedeemRules();
            validateActivityAndRewardEarningRules();
        }
        return programValidationFlows;
    }

    private void validateTierStatus()
    {
        if(program.getIsTiered() == -1)
            programValidationFlows.add("Tier specification is not provided");
    }

    private void validateTiers()
    {
        if(program.getIsTiered() == 1)
        {
            Set<String> tierNames = new HashSet<>();
            List<Integer> level = new ArrayList<>();
            List<Tiers> tiersList = ApplicationDao.getAllTiersForProgram(jdbcTemplate,program.getCode());
            if(tiersList.isEmpty())
                programValidationFlows.add("Tiers are not added!");

            tiersList.forEach(tiers -> tierNames.add(tiers.getTierName()));

            if(tierNames.size() != tiersList.size())
                programValidationFlows.add("Tiers names are not unique");

            tiersList.forEach(tiers -> level.add(tiers.getLevel()));
            if(!sequentialLevels(level))
                programValidationFlows.add("Levels are not ordered");
        }
    }

    private void validateRewardAndRewardRedeemRules()
    {
        List<String> rewards = ApplicationDao.getProgramRewards(jdbcTemplate,program.getCode());
        Set<String> rewardSet = new HashSet<>();
        if(rewards.isEmpty())
            programValidationFlows.add("Rewards for the program were not selected");
        else
        {
            List<RRRules> rrRules = ApplicationDao.getAllRRRulesForProgram(jdbcTemplate, program.getCode());

            rrRules.forEach(rule -> rewardSet.add(rule.getRewardName()));
            rewardSet.forEach(rewards::remove);
            String remainingRules = String.join(",",rewards);

            if(!rewards.isEmpty())
                programValidationFlows.add("Reward Redeeming Rule for "+ remainingRules +" are not defined");
        }
    }

    private void validateActivityAndRewardEarningRules()
    {
        List<String> activities = ApplicationDao.getProgramActivities(jdbcTemplate,program.getCode());
        Set<String> activitySet = new HashSet<>();
        if(activities.isEmpty())
            programValidationFlows.add("Rewards for the program were not selected");
        else
        {
            List<RERules> reRules = ApplicationDao.getAllRERulesForProgram(jdbcTemplate, program.getCode());
            reRules.forEach(rule -> activitySet.add(rule.getActivityName()));
            activitySet.forEach(activities::remove);
            String remainingRules = String.join(",",activities);

            if(!activities.isEmpty())
                programValidationFlows.add("Reward Earning Rule for "+ remainingRules +" are not defined");
        }
    }

    private static boolean sequentialLevels(List<Integer> list) {
        Collections.sort(list);
        Integer prev = null;
        int seq = 0;
        for(Integer i : list) {
            if(prev != null && prev+1 == i)
                seq = seq == 0 ? 2 : seq+1;
            prev = i;
        }
        return seq >= 3;
    }
}

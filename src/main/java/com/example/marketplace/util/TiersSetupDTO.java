package com.example.marketplace.util;

import com.example.marketplace.model.Tiers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ankurmundra
 * November 07, 2021
 */
public class TiersSetupDTO {
    private final List<Tiers> tiersList;

    public TiersSetupDTO()
    {
        tiersList = new ArrayList<>();
    }

    public void addTier(Tiers tiers)
    {
        tiersList.add(tiers);
    }

    public List<Tiers> getTiersList() {
        return tiersList;
    }
}

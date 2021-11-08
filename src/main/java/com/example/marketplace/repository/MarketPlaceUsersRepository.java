package com.example.marketplace.repository;

import com.example.marketplace.model.MarketPlaceUser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ankurmundra
 * October 20, 2021
 */
public interface MarketPlaceUsersRepository extends CrudRepository<MarketPlaceUser,Long> {
}

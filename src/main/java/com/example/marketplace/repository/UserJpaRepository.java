package com.example.marketplace.repository;

import com.example.marketplace.model.MarketPlaceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ankurmundra
 * October 22, 2021
 */
public interface UserJpaRepository extends JpaRepository<MarketPlaceUser,Long> {
    Optional<MarketPlaceUser> findByUserName(String userName);
}


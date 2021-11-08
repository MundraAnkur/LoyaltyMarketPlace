package com.example.marketplace.service;

import com.example.marketplace.model.MarketPlaceUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author ankurmundra
 * October 22, 2021
 */
@Service
public interface UserService extends UserDetailsService {

    MarketPlaceUser save(MarketPlaceUser userRegistrationDto);
}

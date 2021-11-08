package com.example.marketplace.service;

import com.example.marketplace.model.MarketPlaceUserDetails;
import com.example.marketplace.model.MarketPlaceUser;
import com.example.marketplace.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author ankurmundra
 * October 22, 2021
 */
@Service
public class MarketPlaceUserDetailsService implements UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MarketPlaceUser> marketPlaceUser =  userJpaRepository.findByUserName(username);
        marketPlaceUser.orElseThrow(() -> new UsernameNotFoundException(username + " not Found"));
        return marketPlaceUser.map(MarketPlaceUserDetails::new).get();
    }

    @Override
    public MarketPlaceUser save(MarketPlaceUser user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userJpaRepository.save(user);
    }
}

package com.example.marketplace;

import com.example.marketplace.repository.UserJpaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ServletComponentScan
@EnableJpaRepositories(basePackageClasses = UserJpaRepository.class)
public class LoyaltyMarketPlaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoyaltyMarketPlaceApplication.class, args);
    }
}

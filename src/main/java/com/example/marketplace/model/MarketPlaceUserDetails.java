package com.example.marketplace.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author ankurmundra
 * October 22, 2021
 */
public class MarketPlaceUserDetails implements UserDetails {
    private final String userName;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public MarketPlaceUserDetails(MarketPlaceUser user) {
        userName = user.getUserName();
        password = user.getPassword();
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

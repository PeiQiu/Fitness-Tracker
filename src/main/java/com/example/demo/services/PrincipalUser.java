package com.example.demo.services;

import com.example.demo.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class PrincipalUser {//extends User{//implements UserDetails {
    private String userId;

    public PrincipalUser(){
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    //private User user;

//    public PrincipalUser( User user ) {
//        this.user = user;
//    }

//    public String getUsername() {
//        return user.getId();
//    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public String getPassword() {
//        return user.getPassword();
//    }

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return user.getAuthorities();
//    }
}

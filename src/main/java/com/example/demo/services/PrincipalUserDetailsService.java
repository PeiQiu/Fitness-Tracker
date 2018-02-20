package com.example.demo.services;

import com.example.demo.domain.User;
import com.example.demo.repositories.User.UserRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Created by peiqiutian on 15/11/2017.
 */
@Service
public class PrincipalUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepositoryMongo userRepository;

    public UserDetails loadUserByUsername(String username ) {
        User user = userRepository.findByUsername(username);
//        return new PrincipalUser(user);
        return null;
    }
}

package com.Task1.Task.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    UserDAO userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username);
        if(user == null) throw new UsernameNotFoundException("Invalid username or password");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
    }
}

package com.security.SecureApp.Service;

import com.security.SecureApp.modal.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MyUserDetailsService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void saveUserDetails(User user);

    User updateUserDetails(User user);

    User findUserById(long id);
    User findReferenceById(long id);

    User findUserByUsername(String username);

    void deleteUserDetails();

    void setUser(String username);

    User getUser();

    User updateWalletAmount(User user);
}

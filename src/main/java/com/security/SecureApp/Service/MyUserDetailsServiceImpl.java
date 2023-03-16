package com.security.SecureApp.Service;

import com.security.SecureApp.modal.User;
import com.security.SecureApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {
    @Autowired
    private UserRepository repo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Override
    public void saveUserDetails(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        repo.save(user);
    }
    @Override
    public User updateUserDetails(User user){
        return repo.save(user);

    }
    @Override
    public User findUserById(long id){
        return repo.findById(id).get();    }

    @Override
    public User findReferenceById (long id){
        return repo.findById(id).get();
    }

    @Override
    public User findUserByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public void deleteUserDetails() {
        repo.deleteById(user.getId());
    }

    @Override
    public void setUser(String username) {
        user = repo.findByUsername(username);
    }

    @Override
    public User getUser() {
        return user;
    }



    @Override
    public User updateWalletAmount(User user) {
        return repo.save(user);

    }


}

package com.Task1.Task.service;

import com.Task1.Task.model.User;
import com.Task1.Task.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends UserInfoService {
    @Autowired
    UserRepo userRepo;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    public User getById(Long id) {
        return userRepository.getReferenceById(id);
    }

}

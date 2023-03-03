package com.Task1.Task.repository;


import com.Task1.Task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByUserName(String userName);
}

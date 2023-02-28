package com.Task1.Task.repository;

import com.BoardGame.Game.model.User;

import java.util.List;

public interface UserDAO {
    public List<User> getAll();
    public User findById(int id);
    public User findByUserName(String userName);}

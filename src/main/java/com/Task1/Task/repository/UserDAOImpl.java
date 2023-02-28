package com.Task1.Task.repository;

import com.BoardGame.Game.model.User;
import com.BoardGame.Game.utils.NoUserFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
public class UserDAOImpl implements UserDAO{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String RETRIEVE_ALL = "SELECT * FROM userdetails";
    private static final String INSERT = "INSERT INTO userdetails (username, name, password, wallet_amt) VALUES (?,?,?,?)";
    private static final String RETRIEVE_BY_ID = "SELECT id, username, name, password, wallet_amt FROM userdetails WHERE id = ?";
    private static final String UPDATE = "UPDATE userdetails SET username = ?, name = ?, password = ?, wallet_amt = ? WHERE id = ?";
    private static final String RETRIEVE_BY_USERNAME = "SELECT id, username, name, password, wallet_amt FROM userdetails WHERE username = ?";

    public List<User> getAll(){
        return jdbcTemplate.query(RETRIEVE_ALL, new BeanPropertyRowMapper<>(User.class));
    }

    public User findById(int id) {
        List<User> users = jdbcTemplate.query(RETRIEVE_BY_ID, pst -> pst.setInt(1, id), new BeanPropertyRowMapper<>(User.class));
        if (users.size() == 0) throw new NoUserFoundException();
        return users.get(0);
    }

    public User findByUserName(String userName){
        List<User> users = jdbcTemplate.query(RETRIEVE_BY_USERNAME, pst -> pst.setString(1, userName), new BeanPropertyRowMapper<>(User.class));
        if (users.size() == 0) throw new NoUserFoundException();
        return users.get(0);
    }

    public Boolean newUser(String username, String name, String password, int wallet_amt){
        int s = jdbcTemplate.update(INSERT, username, name, password, wallet_amt);
        return s > 0;
    }
}
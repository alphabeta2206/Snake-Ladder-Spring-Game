package com.spring.game.dto;

import com.spring.game.model.Role;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class UserRegistrationDTO {
    private String name;
    private String username;
    private String password;
    private String confirm_password;
    private double wallet_amt;
    private String currency;
    private List<String> role;

}
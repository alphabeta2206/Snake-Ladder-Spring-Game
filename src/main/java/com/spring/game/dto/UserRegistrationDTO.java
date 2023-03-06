package com.spring.game.dto;

import com.spring.game.model.Role;
import lombok.Data;

import java.util.Collection;

@Data
public class UserRegistrationDTO {
    private String name;
    private String username;
    private String password;
    private String confirm_password;
    private double wallet_amt;
    private String currency;
    private String role;

}
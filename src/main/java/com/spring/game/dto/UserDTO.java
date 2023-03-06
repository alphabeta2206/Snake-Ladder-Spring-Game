package com.spring.game.dto;

import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String name;
    private String username;
    private double wallet_amt;
    private String currencyCode;

}

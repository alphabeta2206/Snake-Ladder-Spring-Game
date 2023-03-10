package com.Task1.Task.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String username;
    private String name;
    private String password;
    private Long walletAmt;
    private String currencyCode;
}

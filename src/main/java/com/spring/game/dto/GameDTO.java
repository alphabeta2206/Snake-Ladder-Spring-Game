package com.spring.game.dto;


import lombok.Data;


@Data
public class GameDTO {
    private long gameTypeId;
    private String assignGameName;
    private double betAmount;
}

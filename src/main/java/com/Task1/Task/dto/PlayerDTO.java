package com.Task1.Task.dto;

import lombok.Data;

@Data
public class PlayerDTO {
    private long id;
    private int position;
    private int moves;
    private int prevRoll;
    private double payout;
    private boolean twoSixes;
    private boolean threeSixes;

    public PlayerDTO(long id){
        this.id = id;
        this.position = 1;
        this.moves = 0;
        this.payout = 0;
        this.prevRoll = -1;
        this.twoSixes = false;
        this.threeSixes = false;
    }
}

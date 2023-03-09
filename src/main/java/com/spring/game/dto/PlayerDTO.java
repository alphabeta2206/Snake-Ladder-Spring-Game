package com.spring.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private long id;
    private int position;
    private int moves;
    private int prev_roll;
    private boolean two_sixes;
    private boolean three_sixes;
    private double payout;


    public PlayerDTO(long id){
        this.id = id;
        this.position = 1;
        this.moves = 0;
        this.prev_roll = -1;
        this.two_sixes = false;
        this.three_sixes = false;
    }
}

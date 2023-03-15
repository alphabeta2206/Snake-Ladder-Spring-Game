package com.spring.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class MoveDTO {
    private PlayerDTO player;
    private int prevPos;
    private int currentPos;
    private int dieRoll;
    private HashMap<Integer, Integer> ladderPos;
    private HashMap<Integer, Integer> snakePos;

    public MoveDTO() {
        this.ladderPos = new HashMap<>();
        this.snakePos = new HashMap<>();
    }
}

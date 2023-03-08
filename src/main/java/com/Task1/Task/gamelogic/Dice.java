package com.Task1.Task.gamelogic;

import java.util.Random;

public class Dice {
    private int SIDES;

    public Random rand;

    public Dice(int SIDES) {
        rand = new Random();
        this.SIDES = SIDES;
    }
    public int getValue() {
        return rand.nextInt(SIDES)+1;
    }
}


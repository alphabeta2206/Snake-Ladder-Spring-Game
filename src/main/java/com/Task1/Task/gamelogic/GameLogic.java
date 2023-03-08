package com.Task1.Task.gamelogic;

import com.Task1.Task.dto.PlayerDTO;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public abstract class GameLogic {
    private List<PlayerDTO> players;
    private int playerTurn;
    private Dice dice;
    private int totalMoves;
    private double pricePool;

    public GameLogic(List<PlayerDTO> players, double pricePool) {
        this.players = players;
        this.pricePool = pricePool;
        Collections.shuffle(this.players);
        dice = new Dice(6);
        playerTurn = 0;
    }

    public abstract void rollDie();
    public abstract void calculatePayout();
    public abstract void playerExit(PlayerDTO player);
    public void updateGameState(PlayerDTO player){
        players.removeIf(p -> p.equals(player));
    }
}

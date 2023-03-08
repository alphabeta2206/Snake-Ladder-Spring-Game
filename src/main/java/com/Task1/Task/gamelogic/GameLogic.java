package com.Task1.Task.gamelogic;

import com.Task1.Task.dto.PlayerDTO;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class GameLogic {
    private List<PlayerDTO> players;
    private int playerTurn;
    private Dice dice;
    private int totalMoves;
    private double pricePool;
    private int playersWon;
    private Map<PlayerDTO, Integer> winnerList;

    public GameLogic(List<PlayerDTO> players, double pricePool) {
        this.players = players;
        this.pricePool = pricePool;
        this.winnerList = new HashMap<>();
        Collections.shuffle(this.players);
        dice = new Dice(6);
        playerTurn = 0;
    }

    public abstract void rollDie();
    public abstract void calculatePayout();
    public abstract double getPayoutMultiplier(int winNum);
    public abstract void updateWinnerList(PlayerDTO player);
    public void playerExit(PlayerDTO player) { players.remove(player); }
    public void updateGameState(PlayerDTO player){
        players.removeIf(p -> p.equals(player));
    }
}

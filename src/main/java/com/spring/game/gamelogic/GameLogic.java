package com.spring.game.gamelogic;

import com.spring.game.dto.PlayerDTO;
import lombok.Data;

import java.util.*;

@Data
public abstract class GameLogic {
    private List<PlayerDTO> players;
    private int playerTurn;
    private Dice dice;
    private int totalMoves;
    private double pricePool;
    private int playersWon;
    private int round;
    private LinkedHashMap<PlayerDTO, Integer> winnerList;

    public GameLogic(List<PlayerDTO> players, double pricePool) {
        this.players = players;
        this.pricePool = pricePool;
        this.winnerList = new LinkedHashMap<>();
        Collections.shuffle(this.players);
        dice = new Dice(6);
        playerTurn = 0;
        this.round = 1;
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
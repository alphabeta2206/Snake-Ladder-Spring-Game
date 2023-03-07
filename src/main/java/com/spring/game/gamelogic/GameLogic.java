package com.spring.game.gamelogic;

import com.spring.game.dto.PlayerDTO;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public abstract class GameLogic {
    private List<PlayerDTO> players;
    private int playerTurn;
    private Dice dice;

    public GameLogic(List<PlayerDTO> players) {
        this.players = players;
        Collections.shuffle(this.players);
        dice = new Dice(6);
        playerTurn = 0;
    }

    public abstract void rollDie();
    public abstract PlayerDTO calculatePayout();
    public void updateGameState(PlayerDTO player){
        players.removeIf(p -> p.equals(player));
    }

}

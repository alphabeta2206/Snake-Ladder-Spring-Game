package com.Task1.Task.gamelogic;

import com.Task1.Task.dto.PlayerDTO;
import com.Task1.Task.model.Game;
import com.Task1.Task.model.User;

import java.util.*;

public class GamePlayer {
    private int round;
    private SNL snlGame;
//    private Ludo ludoGame;
    public GamePlayer(Game game){
        this.round = 0;
        if ( game.getGametype().getGameName().equals("SNL") ) {
            Set<User> playerList = game.getPlayers();
            ArrayList<PlayerDTO> snlPlayers = new ArrayList<>();
            for(User player: playerList){
                PlayerDTO snlPlayer = new PlayerDTO(player.getId());
                snlPlayers.add(snlPlayer);
            }
            this.snlGame = new SNL(snlPlayers, game.getBetAmount() * snlPlayers.size());
        }
        else{
            startLudo();
        }
    }

    public LinkedHashMap<PlayerDTO, Integer> startSNL(){
        int playerCount = this.snlGame.getPlayers().size();
        while( this.snlGame.getPlayersWon() < playerCount - 1){
            this.snlGame.rollDie();
        }
        this.snlGame.calculatePayout();
        return this.snlGame.getWinnerList();
    }

    public void startLudo(){

    }
}

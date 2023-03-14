package com.Task1.Task.events.listeners;

import com.Task1.Task.dto.PlayerDTO;
import com.Task1.Task.events.StartGameEvent;
import com.Task1.Task.events.RollDieEvent;
import com.Task1.Task.gamelogic.GameLogic;
import com.Task1.Task.gamelogic.SNL;
import com.Task1.Task.model.User;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.stream.Collectors;

public class GameEventListener {
    GameLogic gameLogic;

    @EventListener(StartGameEvent.class)
    @Transactional
    public void handleStartGame(StartGameEvent event) {
        List<PlayerDTO> players = event.getGame().getPlayers().stream().map(this::convertUserToPlayer).collect(Collectors.toList());
        System.out.println(players);
        double prizePool = event.getGame().getBetAmount()*players.size();
        gameLogic = new SNL(players, prizePool);
    }

    @EventListener(RollDieEvent.class)
    @Transactional
    public void handleRollDie(RollDieEvent event) {
        int playerTurn = gameLogic.getPlayerTurn();
        if (gameLogic.getPlayers().get(playerTurn).getId() == event.getUserId()) {
            gameLogic.rollDie();
        } else {
            System.out.println("NOT YOUR TURN PLEASE WAIT!!!!!");
        }
    }

    public PlayerDTO convertUserToPlayer(User user) {
        return new PlayerDTO(user.getId());
    }
}

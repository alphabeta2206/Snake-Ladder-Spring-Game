package com.spring.game.events.listeners;

import com.spring.game.dto.PlayerDTO;
import com.spring.game.events.RollDieEvent;
import com.spring.game.events.StartGameEvent;
import com.spring.game.gamelogic.GameLogic;
import com.spring.game.gamelogic.SNLGame;
import com.spring.game.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GameEventListener{
    GameLogic gameLogic;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEventListener.class);

    @EventListener(StartGameEvent.class)
    @Transactional
    public void handleStartGame(StartGameEvent event) {
        LOGGER.info("Event Details are - " + event);
        List<PlayerDTO> players = event.getGame().getPlayers().stream().map(this::convertUserToPlayer).collect(Collectors.toList());
        System.out.println(players);
        double prizePool = event.getGame().getBetAmount()*players.size();
        gameLogic = new SNLGame(players, prizePool);
    }

    @EventListener(RollDieEvent.class)
    @Transactional
    public void handleRollDie(RollDieEvent event) {
        int playerTurn = gameLogic.getPlayerTurn();
        if (gameLogic.getPlayers().get(playerTurn).getId() == event.getUserId()) {
            gameLogic.rollDie();
        } else {
            System.out.println("---------------------------------IIIIIIIIIIIIIIIIIII_______________________________");
        }
    }

    public PlayerDTO convertUserToPlayer(User user) {
        return new PlayerDTO(user.getId());
    }
}

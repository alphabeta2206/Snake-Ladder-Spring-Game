package com.spring.game.events.publishers;

import com.spring.game.Controller.GameController;
import com.spring.game.events.*;
import com.spring.game.model.Bet;
import com.spring.game.model.Game;
import com.spring.game.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class EventPublisher {
    @Autowired
    ApplicationEventPublisher appEventPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);

    public void publishStartGame(Game game) {
        StartGameEvent event = new StartGameEvent(this, game);
        appEventPublisher.publishEvent(event);
    }
    public void publishRollDie(long userId, long gameId) {
        RollDieEvent event = new RollDieEvent(userId, gameId);
        appEventPublisher.publishEvent(event);
    }
    public void publishSimulateGame(Game game, HashMap<Long, Bet> bets) {
        SimulateGameEvent event = new SimulateGameEvent(game, bets);
        appEventPublisher.publishEvent(event);
    }
}

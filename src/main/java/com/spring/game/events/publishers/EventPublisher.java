package com.spring.game.events.publishers;

import com.spring.game.Controller.GameController;
import com.spring.game.events.RollDieEvent;
import com.spring.game.events.StartGameEvent;
import com.spring.game.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    @Autowired
    ApplicationEventPublisher appEventPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);

    public void publishStartGame(Game game) {
        StartGameEvent event = new StartGameEvent(this, game);
        appEventPublisher.publishEvent(event);
    }

    public void publishRollDie(long userId) {
        RollDieEvent event = new RollDieEvent(userId);
        appEventPublisher.publishEvent(event);
    }
}

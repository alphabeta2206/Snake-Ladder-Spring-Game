package com.Task1.Task.events.publishers;

import com.Task1.Task.events.RollDieEvent;
import com.Task1.Task.events.StartGameEvent;
import com.Task1.Task.events.TransactionEvent;
import com.Task1.Task.model.Game;
import com.Task1.Task.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void publishTransaction(User user, double amount) {
        TransactionEvent event = new TransactionEvent(user, amount);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishStartGame(Game game) {
        StartGameEvent event = new StartGameEvent(this, game);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishRollDie(long userId) {
        RollDieEvent event = new RollDieEvent(userId);
        applicationEventPublisher.publishEvent(event);
    }
}

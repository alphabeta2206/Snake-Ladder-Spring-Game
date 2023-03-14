package com.Task1.Task.events;

import com.Task1.Task.model.Game;
import org.springframework.context.ApplicationEvent;
public class StartGameEvent extends ApplicationEvent {
    private Game game;

    public StartGameEvent(Object source, Game game) {
        super(source);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

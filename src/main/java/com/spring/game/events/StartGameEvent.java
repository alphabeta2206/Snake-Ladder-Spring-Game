package com.spring.game.events;

import com.spring.game.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

package com.Task1.Task.events;

import com.Task1.Task.model.Game;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class StartGameEvent{
    private Game game;

    public StartGameEvent(Game game) {
        this.game = game;
    }
}

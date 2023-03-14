package com.Task1.Task.events;

import com.Task1.Task.model.Game;
import lombok.Data;

@Data
public class SimulateGameEvent {
    private Game game;
}

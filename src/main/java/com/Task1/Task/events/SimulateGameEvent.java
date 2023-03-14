package com.Task1.Task.events;

import com.Task1.Task.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimulateGameEvent {
    private Game game;
}

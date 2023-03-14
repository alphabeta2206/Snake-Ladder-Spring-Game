package com.Task1.Task.events;

import com.Task1.Task.model.Bet;
import com.Task1.Task.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class SimulateGameEvent {
    private Game game;
    private HashMap<Long, Bet> bets;
}

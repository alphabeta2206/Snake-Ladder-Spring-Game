package com.spring.game.events;

import com.spring.game.model.Bet;
import com.spring.game.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulateGameEvent {
    private Game game;
    private HashMap<Long, Bet> bets;
}

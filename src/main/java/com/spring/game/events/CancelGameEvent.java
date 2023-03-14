package com.spring.game.events;

import com.spring.game.model.Bet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelGameEvent {
    private List<Bet> bets;
}

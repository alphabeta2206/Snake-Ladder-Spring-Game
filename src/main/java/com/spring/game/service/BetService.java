package com.spring.game.service;

import com.spring.game.model.Bet;
import com.spring.game.repository.BetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetService {
    @Autowired
    private BetRepo betRepo;

    public List<Bet> getBets() {
        return betRepo.findAll();
    }

    public List<Bet> getBetHistory(long userId) {
        return betRepo.findAllByUserId(userId);
    }

    public void saveBet(Bet bet) {
        betRepo.save(bet);
    }

    public Bet getOneBet(long userId, long gameId) {
        return betRepo.findOneBet(userId, gameId);
    }
}

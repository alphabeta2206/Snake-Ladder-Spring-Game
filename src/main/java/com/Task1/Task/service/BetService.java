package com.Task1.Task.service;

import com.Task1.Task.model.Bet;
import com.Task1.Task.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BetService {
    @Autowired
    BetRepository betRepository;

    public void saveBet(Bet bet) {
        betRepository.save(bet);
    }

    public Bet getByBetId(Long betId) {
        return betRepository.getReferenceById(betId);
    }

    public Bet getByUserId(Long userId) { return betRepository.findByUserId(userId); }

}

package com.Task1.Task.service;

import com.Task1.Task.events.publishers.EventPublisher;
import com.Task1.Task.model.Bet;
import com.Task1.Task.model.User;
import com.Task1.Task.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetService {
    @Autowired
    BetRepository betRepository;
    @Autowired
    EventPublisher eventPublisher;

    public void saveBet(Bet bet) { betRepository.save(bet); }

    public void saveBet(Bet bet, User user, double multiplier) {
        double amount = user.getWalletAmt() - bet.getAmount() / multiplier;
        eventPublisher.publishTransaction(user, amount); // publish event
//        betRepository.save(bet);
    }
    public void saveBets(List<Bet> bets){
        betRepository.saveAll(bets);
    }

    public Bet getByBetId(Long betId) {
        return betRepository.getReferenceById(betId);
    }

    public Bet getByUserId(Long userId) { return betRepository.findByUserId(userId); }

}

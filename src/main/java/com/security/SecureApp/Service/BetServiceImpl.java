package com.security.SecureApp.Service;

import com.security.SecureApp.modal.Bet;
import com.security.SecureApp.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BetServiceImpl{

    @Autowired
    BetRepository betRepository;
    public void saveBet(Bet bet) {
        betRepository.save(bet);

    }
}

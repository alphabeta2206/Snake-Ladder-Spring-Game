package com.spring.game.events.listeners;

import com.spring.game.events.TransactionEvent;
import com.spring.game.model.User;
import com.spring.game.service.CurrencyService;
import com.spring.game.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class BetListner {
    @Autowired
    UserService userService;
    @Autowired
    CurrencyService currencyService;

    @EventListener(TransactionEvent.class)
    @Transactional
    public void handleMyEvent(TransactionEvent event) {
        User user = event.getUser();
        double amount = event.getAmount();
        user.setWallet_amt(amount);
        userService.updateUser(user);
    }
}

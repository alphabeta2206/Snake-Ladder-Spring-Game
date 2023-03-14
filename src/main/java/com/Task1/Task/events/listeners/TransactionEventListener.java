package com.Task1.Task.events.listeners;

import com.Task1.Task.events.TransactionEvent;
import com.Task1.Task.model.User;
import com.Task1.Task.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class TransactionEventListener {
    @Autowired
    UserService userService;

    @EventListener(TransactionEvent.class)
    @Transactional
    public void handleMyEvent(TransactionEvent event) {
        User user = event.getUser();
        double amount = event.getAmount();

        user.setWalletAmt(amount);
        userService.saveUser(user);
    }
}

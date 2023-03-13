package com.Task1.Task.events;

import com.Task1.Task.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionEvent {
    private User user;
    private double amount;
}

package com.Task1.Task.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RollDieEvent {
    private long userId;
    private long gameId;
}
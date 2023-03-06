package com.spring.game.model;

import com.spring.game.enums.BetStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private long gameId;
    @Column(nullable = false)
    private long userId;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private Timestamp placeTime;
    private double payoff;
    private Timestamp settleTime;
    private BetStatus status;

}

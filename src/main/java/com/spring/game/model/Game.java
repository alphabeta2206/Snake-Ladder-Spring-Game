package com.spring.game.model;

import com.spring.game.enums.CancelReason;
import com.spring.game.enums.GameStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_type_id", referencedColumnName = "id")
    private GameType gameType;
    private String assignGameName;
    private Timestamp gameStartTime;
    private GameStatus gameStatus;
    private double betAmount;
    @Column(nullable = true)
    private CancelReason cancelReason;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User creator;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<User> players = new HashSet<>();
}

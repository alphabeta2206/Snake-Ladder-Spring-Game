package com.Task1.Task.model;

import com.Task1.Task.enums.GameStatus;
import com.Task1.Task.enums.CancelReason;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.repository.cdi.Eager;

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
    private Long id;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    private GameType gametype;
    private String assignGameName;
    private boolean flag;
    private Timestamp gameStartTime;
    private GameStatus gameStatus;
    private double betAmount;
    @Column(nullable = true)
    private CancelReason cancelReason;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private User creator;

    @OneToMany(
            cascade = CascadeType.PERSIST
    )
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Set<User> players = new HashSet<>();
}
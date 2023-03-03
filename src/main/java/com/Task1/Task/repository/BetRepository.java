package com.Task1.Task.repository;

import com.Task1.Task.model.Bet;
import com.Task1.Task.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet,Long> {
    public Bet findByUserId(Long userId);

}

package com.spring.game.repository;

import com.spring.game.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepo extends JpaRepository<Bet, Long> {
    public List<Bet> findAllByUserId(long userId);

    @Query(value = "SELECT * FROM bet WHERE user_id = ? and game_id = ?", nativeQuery = true)
    public Bet findOneBet(long userId, long gameId);
}

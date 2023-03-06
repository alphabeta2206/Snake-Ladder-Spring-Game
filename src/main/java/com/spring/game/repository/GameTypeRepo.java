package com.spring.game.repository;

import com.spring.game.model.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameTypeRepo extends JpaRepository<GameType, Long> {
}

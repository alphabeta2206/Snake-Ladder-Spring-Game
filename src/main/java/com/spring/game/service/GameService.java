package com.spring.game.service;


import com.spring.game.Controller.GameController;
import com.spring.game.enums.GameStatus;
import com.spring.game.model.Game;
import com.spring.game.model.GameType;
import com.spring.game.repository.GameRepo;
import com.spring.game.repository.GameTypeRepo;
import com.spring.game.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class GameService {

    @Autowired
    private GameRepo gameRepo;

    @Autowired
    private GameTypeRepo gameTypeRepo;

    @Autowired
    private UserRepo userRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    public void createGameType(GameType gameType){
        gameTypeRepo.save(gameType);
    }

    public Game findById(long id) {
        return gameRepo.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Game createGame(Game game){

        game.setGameStatus(GameStatus.NEW);
        game.setGameStartTime(Timestamp.from(Instant.now()));
        return gameRepo.save(game);
    }

    public void updateGame(Game game) {
        gameRepo.save(game);
    }

}

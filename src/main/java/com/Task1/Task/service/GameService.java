package com.Task1.Task.service;

import com.Task1.Task.model.Game;
import com.Task1.Task.model.User;
import com.Task1.Task.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    public Game getById(Long id) {
        return gameRepository.getReferenceById(id);
    }

}

package com.Task1.Task.service;

import com.Task1.Task.model.Game;
import com.Task1.Task.model.User;
import com.Task1.Task.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public Game getById(Long id) {
        try { return gameRepository.getReferenceById(id); }
        catch (EntityNotFoundException e) { return null; }
    }

    public List<Game> gameList() {
        return gameRepository.findAll();
    }

}

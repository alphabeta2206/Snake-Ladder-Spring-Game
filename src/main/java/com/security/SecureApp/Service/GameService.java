package com.security.SecureApp.Service;

import com.security.SecureApp.modal.CancelFinishedGame;
import com.security.SecureApp.repository.GameRepository;
import com.security.SecureApp.modal.Games;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GameService implements GameServiceImpl {
    @Autowired
    GameRepository gameRepository;

    private Games game;

    @Override
    public Games findbyId(long id) {

        return gameRepository.findById(id).orElseThrow(EntityNotFoundException::new);

    }

    @Override
    public void deleteGame(Games games) {
        gameRepository.deleteById(games.getId());
    }
    @Override
    public Games createGame(Games game) {
        return gameRepository.save(game);
    }
    @Override
    public Object getAllGames() {
        return gameRepository.findAll();
    }

    public List<Games> findAllByStatus( String status ) {
        return gameRepository.findAllByStatus(status);
    }

    @Override
    public Games saveGame(Games game) {
        return gameRepository.save(game);
    }

    @Override
    public Games cancelGame(Games game) {
        return gameRepository.save(game);
    }
}

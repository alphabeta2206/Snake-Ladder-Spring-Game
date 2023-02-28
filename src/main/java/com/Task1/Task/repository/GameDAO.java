package com.Task1.Task.repository;

import com.BoardGame.Game.model.Game;

import java.util.List;

public interface GameDAO {
    List<Game> getAll();

    Boolean createGame(int bid,String gameType,int playFlag,int playerCount);
}

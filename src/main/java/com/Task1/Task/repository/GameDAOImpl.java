package com.Task1.Task.repository;

import com.BoardGame.Game.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class GameDAOImpl implements GameDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final String RETRIEVE_ALL = "SELECT gid, gname, min_amt, max_amt  FROM game_details";
    private static final String NEW_GAME = "SELECT gid, gname, min_amt, max_amt  FROM game_details";

    @Override
    public List<Game> getAll(){
        return jdbcTemplate.query(RETRIEVE_ALL, new BeanPropertyRowMapper<>(Game.class));
    }

    @Override
    public Boolean createGame(int bid, String gameType, int playFlag, int playerCount) {
        int s = jdbcTemplate.update(NEW_GAME, bid, gameType, playFlag, playerCount);
        return s > 0;
    }
}

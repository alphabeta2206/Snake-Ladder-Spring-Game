package com.Task1.Task.service;


import com.Task1.Task.repository.GameDAOImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameServices extends GameDAOImpl {
    @Override
    public List<Game> getAll() {
        return super.getAll();
    }

    public String getGames(){
        List<Game> games = this.getAll();
        StringBuilder res = new StringBuilder();
        for(Game game : games){
            res.append(game.getGname());
            res.append(",");
        }
        return res.toString();
    }
}

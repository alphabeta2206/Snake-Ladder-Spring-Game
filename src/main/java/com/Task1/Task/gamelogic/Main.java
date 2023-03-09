package com.Task1.Task.gamelogic;

import com.Task1.Task.dto.PlayerDTO;
import com.Task1.Task.model.Game;
import com.Task1.Task.model.GameType;
import com.Task1.Task.model.Role;
import com.Task1.Task.model.User;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

class thread1 implements Runnable{
    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        game.setGametype(new GameType("SNL"));
        game.setBetAmount(100);
        User player1 = new User(12L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        User player2 = new User(13L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        User player3 = new User(14L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        User player4 = new User(15L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        Set<User> players = Set.of(player1, player2, player3, player4);
        game.setPlayers(players);

        GamePlayer player = new GamePlayer(game);
        while(true) {
            Thread.sleep(1000);
            LinkedHashMap<PlayerDTO, Integer> winners = player.startSNL();
            System.out.println(winners);
        }
    }
}

package com.spring.game.gamelogic;

import com.spring.game.dto.PlayerDTO;
import com.spring.game.model.Game;
import com.spring.game.model.GameType;
import com.spring.game.model.Role;
import com.spring.game.model.User;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.setGameType(new GameType("SNL"));
        game.setBetAmount(100);
        User player1 = new User(12L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        User player2 = new User(13L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        User player3 = new User(14L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        User player4 = new User(15L, "A", "B", "C", 12, List.of(new Role("USER")), "USD");
        Set<User> players = Set.of(player1, player2, player3, player4);
        game.setPlayers(players);

        GamePlayer player = new GamePlayer(game);
        LinkedHashMap<PlayerDTO, Integer> winners = player.startSNL();
        System.out.println(winners);
    }
}

package com.Task1.Task.events.listeners;

import com.Task1.Task.dto.PlayerDTO;
import com.Task1.Task.events.SimulateGameEvent;
import com.Task1.Task.events.StartGameEvent;
import com.Task1.Task.gamelogic.GamePlayer;
import com.Task1.Task.model.Bet;
import com.Task1.Task.model.Game;
import com.Task1.Task.model.User;
import com.Task1.Task.service.BetService;
import com.Task1.Task.service.CurrencyService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class GameEventListener {

    @Autowired
    CurrencyService currencyService;

    @Autowired
    BetService betService;

    GamePlayer gamePlayer;

    @EventListener(StartGameEvent.class)
    @Transactional
    public void handleStartGame(StartGameEvent event) {
        this.gamePlayer = new GamePlayer(event.getGame());
//        List<PlayerDTO> players = event.getGame().getPlayers().stream().map(this::convertUserToPlayer).collect(Collectors.toList());
//        System.out.println(players);
//        double prizePool = event.getGame().getBetAmount()*players.size();
//        gameLogic = new SNL(players, prizePool);
    }

//    @EventListener(RollDieEvent.class)
//    @Transactional
//    public void handleRollDie(RollDieEvent event) {
//        int playerTurn = gameLogic.getPlayerTurn();
//        if (gameLogic.getPlayers().get(playerTurn).getId() == event.getUserId()) {
//            gameLogic.rollDie();
//        } else {
//            System.out.println("NOT YOUR TURN PLEASE WAIT!!!!!");
//        }
//    }

    @EventListener(SimulateGameEvent.class)
    @Transactional
    public void simulateGame(SimulateGameEvent event, HttpSession session){
        Game game = event.getGame();
        HashMap<Long, Bet> bets = (HashMap<Long, Bet>) session.getAttribute("playerBets");
        LinkedHashMap<PlayerDTO, Integer> winnerList = gamePlayer.startSNL();
        Set<PlayerDTO> winners = winnerList.keySet();
        List<Bet> betList = new ArrayList<>();
        winners.forEach(player -> {
            Bet bet = bets.get(player.getId());
            User user = game.getPlayers().stream().filter(user1 -> user1.getId() == player.getId()).collect(Collectors.toList()).get(0);
            double multiplier = currencyService.getMultiplier(user.getCurrencyCode());
            bet.setPayOff(player.getPayout());
            bet.setSettleTime(Timestamp.from(Instant.now()));
            bet.setStatus('S');
            betService.saveBet(bet, user, multiplier);
            betList.add(bet);
        });
        betService.saveBets(betList);
        System.out.println(winners);
    }

    public PlayerDTO convertUserToPlayer(User user) {
        return new PlayerDTO(user.getId());
    }
}

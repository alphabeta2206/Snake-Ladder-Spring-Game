package com.spring.game.events.listeners;

import com.spring.game.dto.PlayerDTO;
import com.spring.game.enums.BetStatus;
import com.spring.game.events.RollDieEvent;
import com.spring.game.events.SimulateGameEvent;
import com.spring.game.events.StartGameEvent;
import com.spring.game.events.publishers.EventPublisher;
import com.spring.game.gamelogic.GameLogic;
import com.spring.game.gamelogic.GamePlayer;
import com.spring.game.gamelogic.SNLGame;
import com.spring.game.model.Bet;
import com.spring.game.model.Game;
import com.spring.game.model.User;
import com.spring.game.service.BetService;
import com.spring.game.service.CurrencyService;
import com.spring.game.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class GameEventListener{
    HashMap<Long, GameLogic> games = new HashMap<>();
    HashMap<Long, GamePlayer> gamePlayers = new HashMap<>();
    @Autowired
    BetService betService;
    @Autowired
    UserService userService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    EventPublisher eventPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEventListener.class);

    @EventListener(StartGameEvent.class)
    @Transactional
    public void handleStartGame(StartGameEvent event) {
        LOGGER.info("Event Details are - " + event);
        List<PlayerDTO> players = event.getGame().getPlayers().stream().map(this::convertUserToPlayer).collect(Collectors.toList());
        System.out.println(players);
        double prizePool = event.getGame().getBetAmount()*players.size();
        GameLogic gameLogic = new SNLGame(players, prizePool);
        GamePlayer gamePlayer = new GamePlayer(event.getGame());
        games.put(event.getGame().getId(), gameLogic);
        gamePlayers.put(event.getGame().getId(), gamePlayer);
        event.getGame().getPlayers().forEach(user -> {
            user.setWallet_amt(user.getWallet_amt() - currencyService.convertFromEuro(event.getGame().getBetAmount(), user.getCurrencyCode()));
        });
    }

    @EventListener(SimulateGameEvent.class)
    @Transactional
    public void simulateGame(SimulateGameEvent event){
        Game game = event.getGame();
        HashMap<Long, Bet> bets = event.getBets();
        LinkedHashMap<PlayerDTO, Integer> winnerList = gamePlayers.get(game.getId()).startSNL();
        Set<PlayerDTO> winners = winnerList.keySet();
        List<Bet> betList = new ArrayList<>();
        winners.forEach(player -> {
            Bet bet = bets.get(player.getId());
            User user = game.getPlayers().stream().filter(user1 -> user1.getId() == player.getId()).collect(Collectors.toList()).get(0);
            bet.setPayoff(player.getPayout());
            bet.setSettleTime(Timestamp.from(Instant.now()));
            bet.setStatus(BetStatus.S);
            betList.add(bet);
            user.setActive(false);
            user.setWallet_amt(user.getWallet_amt() + currencyService.convertFromEuro(bet.getPayoff(),user.getCurrencyCode()));
            userService.updateUser(user);
        });
        betService.saveAllBets(betList);
        LOGGER.info(winners.toString());
        LOGGER.info(gamePlayers.get(game.getId()).toString());
        gamePlayers.remove(game.getId());
    }
    @EventListener(RollDieEvent.class)
    @Transactional
    public void handleRollDie(RollDieEvent event) {
        GameLogic gameLogic = games.get(event.getGameId());
        int playerTurn = gameLogic.getPlayerTurn();
        if (gameLogic.getPlayers().get(playerTurn).getId() == event.getUserId()) {
            LOGGER.info(gameLogic.getPlayers().toString());
            gameLogic.rollDie();
            LOGGER.info(gameLogic.getPlayers().toString());
            games.put(event.getGameId(), gameLogic);
        } else {
            LOGGER.info("Not the Players turn!");
        }
    }

    public PlayerDTO convertUserToPlayer(User user) {
        return new PlayerDTO(user.getId());
    }
}

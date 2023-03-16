package com.Task1.Task.events.listeners;

import com.Task1.Task.dto.PlayerDTO;
import com.Task1.Task.enums.GameStatus;
import com.Task1.Task.events.RollDieEvent;
import com.Task1.Task.events.SimulateGameEvent;
import com.Task1.Task.events.StartGameEvent;
import com.Task1.Task.events.publishers.EventPublisher;
import com.Task1.Task.gamelogic.GameLogic;
import com.Task1.Task.gamelogic.GamePlayer;
import com.Task1.Task.gamelogic.SNL;
import com.Task1.Task.model.Bet;
import com.Task1.Task.model.Game;
import com.Task1.Task.model.User;
import com.Task1.Task.service.BetService;
import com.Task1.Task.service.CurrencyService;
import com.Task1.Task.service.GameService;
import com.Task1.Task.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameEventListener {
    HashMap<Long, GameLogic> games = new HashMap<>();
    HashMap<Long, GamePlayer> gamePlayers = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEventListener.class);

    @Autowired
    CurrencyService currencyService;

    @Autowired
    BetService betService;

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    EventPublisher eventPublisher;

    @EventListener(StartGameEvent.class)
    @Transactional
    public void handleStartGame(StartGameEvent event) {
        Game game = event.getGame();
        game.setGameStatus(GameStatus.IN_PROGRESS);
        List<PlayerDTO> players = game.getPlayers().stream().map(user -> new PlayerDTO(user.getId())).collect(Collectors.toList());
        LOGGER.info("Player List" + players);
        double prizePool = event.getGame().getBetAmount()*players.size();
        GameLogic gameLogic = new SNL(players, prizePool);
        GamePlayer gamePlayer = new GamePlayer(event.getGame());
        games.put(event.getGame().getId(), gameLogic);
        gamePlayers.put(event.getGame().getId(), gamePlayer);
        event.getGame().getPlayers().forEach(user -> {
            user.setWalletAmt(user.getWalletAmt() - currencyService.convertFromEuro(user.getCurrencyCode(), event.getGame().getBetAmount()));
            userService.saveUser(user); // wallet update at start game
        });
        gameService.saveGame(game);
    }

    @EventListener(RollDieEvent.class)
    @Transactional
    public void handleRollDie(RollDieEvent event) {
        GameLogic gameLogic = games.get(event.getGameId());
        int playerTurn = gameLogic.getPlayerTurn();
        if (gameLogic.getPlayers().get(playerTurn).getId() == event.getUserId()) {
            gameLogic.rollDie();
            games.put(event.getGameId(), gameLogic);
        } else {
            System.out.println("NOT YOUR TURN");
        }
    }

    @EventListener(SimulateGameEvent.class)
    @Transactional
    public void simulateGame(SimulateGameEvent event){
        Game game = event.getGame();
        HashMap<Long, Bet> bets = event.getBets();
        LinkedHashMap<PlayerDTO, Integer> winnerList = gamePlayers.get(game.getId()).startSNL();
        LOGGER.info(winnerList.toString());
        Set<PlayerDTO> winners = winnerList.keySet();
        List<Bet> betList = new ArrayList<>();
        winners.forEach(player -> {
            Bet bet = bets.get(player.getId());
            User user = game.getPlayers().stream().filter(user1 -> user1.getId() == player.getId()).collect(Collectors.toList()).get(0);
            bet.setPayOff(player.getPayout());
            bet.setSettleTime(Timestamp.from(Instant.now()));
            bet.setStatus('S');
            betList.add(bet);
            user.setWalletAmt(user.getWalletAmt() + currencyService.convertFromEuro(user.getCurrencyCode(), bet.getPayOff()));
            userService.saveUser(user); // wallet update at end of simulation
        });
        betService.saveBets(betList);
        gamePlayers.remove(game.getId());
        game.setGameStatus(GameStatus.COMPLETED);
        game.setPlayers(new HashSet<>());
        gameService.saveGame(game);
    }
}
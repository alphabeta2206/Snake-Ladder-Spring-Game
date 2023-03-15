package com.spring.game.Controller;

import com.spring.game.dto.GameDTO;
import com.spring.game.dto.MoveDTO;
import com.spring.game.dto.PlayerDTO;
import com.spring.game.enums.BetStatus;
import com.spring.game.enums.CancelReason;
import com.spring.game.enums.GameStatus;
import com.spring.game.exceptions.GameException;
import com.spring.game.gamelogic.GameLogic;
import com.spring.game.gamelogic.GamePlayer;
import com.spring.game.gamelogic.SNLGame;
import com.spring.game.model.Bet;
import com.spring.game.model.Game;
import com.spring.game.model.GameType;
import com.spring.game.model.User;
import com.spring.game.service.BetService;
import com.spring.game.service.CurrencyService;
import com.spring.game.service.GameService;
import com.spring.game.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private UserService userService;
    @Autowired
    private BetService betService;
    @Autowired
    private CurrencyService currencyService;

    private HashMap<Long, GameLogic> games = new HashMap<>();
    private HashMap<Long, GamePlayer> gamePlayers = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    // Create a Game Type, Only admin can access
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/game/create/game_type")
    public String createGameType(@RequestParam String game_name){
        gameService.createGameType(new GameType(game_name));
        return "redirect:/lobby";
    }

    // Create a new game
    @RequestMapping("/create")
    @ResponseBody
    public Game createGame(@RequestBody GameDTO gameDTO, Principal principal) {
        Game game = new Game();
        User creator = userService.findByUsername(principal.getName());
        double userBalance = currencyService.convertToEuro(creator.getWallet_amt(), creator.getCurrencyCode());
        // Checking if user can create game with given bet amount
        if(userBalance > gameDTO.getBetAmount() && !creator.isActive()) {
            // Creating a new game
            creator.setActive(true);
            userService.updateUser(creator);
            game.setAssignGameName(gameDTO.getAssignGameName());
            game.setBetAmount(gameDTO.getBetAmount());
            game.setGameType(gameService.getGameType(gameDTO.getGameTypeId()));
            game.setCreator(creator);
            game.getPlayers().add(creator);
            userService.updateUser(creator);
            return gameService.createGame(game);
        }else throw new GameException("Can't Create Game with given bet amount! Insufficient balance!");
    }

    // Join the Game

    @RequestMapping("/{id}/join")
    @ResponseBody
    public Game joinGame(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Game game = gameService.findById(id);
        if(game.getGameStatus().equals(GameStatus.NEW) && !user.isActive()){
            //Checking if user can join game with given betAmount
            double betAmount = currencyService.convertToEuro(game.getBetAmount(), game.getCreator().getCurrencyCode());
            double userBalance = currencyService.convertToEuro(user.getWallet_amt(), user.getCurrencyCode());
            if(betAmount < userBalance) {
                // Adding User to game
                user.setActive(true);
                game.getPlayers().add(userService.findByUsername(principal.getName()));
                gameService.updateGame(game);
                userService.updateUser(user);
                return game;
            }else throw new GameException("Cannot join this game! Insufficient balance");
        }else throw new GameException("Cannot join this game! Game already started or User already playing a game!");
    }

    @RequestMapping("/{id}/start")
    @ResponseBody
    public Game startGame(@PathVariable Long id, Principal principal, HttpSession session) {
        User creator = userService.findByUsername(principal.getName());
        Game game = gameService.findById(id);
        HashMap<Long, Bet> bets = new HashMap<>();
        if (game.getCreator().equals(creator) && game.getPlayers().size() > 1) {
            game.setGameStatus(GameStatus.IN_PROGRESS);
            Set<User> playerList = game.getPlayers();
            playerList.forEach(user -> {
                // Creating a Bet
                Bet bet = new Bet();
                bet.setUserId(user.getId());
                bet.setGameId(game.getId());
                bet.setPlaceTime(Timestamp.from(Instant.now()));
                bet.setAmount(currencyService.convertToEuro(game.getBetAmount(), creator.getCurrencyCode()));
                bets.put(user.getId(),bet);
            });
            session.setAttribute("playerBets", bets);
            gameService.updateGame(game);
            List<PlayerDTO> players = game.getPlayers().stream().map(user -> new PlayerDTO(user.getId())).collect(Collectors.toList());
            double prizePool =game.getBetAmount()*players.size();
            GameLogic gameLogic = new SNLGame(players, prizePool);
            GamePlayer gamePlayer = new GamePlayer(game);
            games.put(game.getId(), gameLogic);
            gamePlayers.put(game.getId(), gamePlayer);
            game.getPlayers().forEach(user -> {
                user.setWallet_amt(user.getWallet_amt() - currencyService.convertFromEuro(game.getBetAmount(), user.getCurrencyCode()));
            });
            userService.updateAll(game.getPlayers().stream().toList());
            return game;
        }
        else throw new GameException("Only Creator can start the game");
    }


    @RequestMapping("/{id}/cancelGame")
    @ResponseBody
    public Game cancelGame(@PathVariable long id, @RequestBody String reason, Principal principal, HttpSession session){
        Game game = gameService.findById(id);
        User creator = userService.findByUsername(principal.getName());
        HashMap<Long, Bet> bets = (HashMap<Long, Bet>) session.getAttribute("playerBets");
        List<Bet> betList = new ArrayList<>();
        if(game.getCreator().equals(creator)) {
            if(game.getGameStatus().equals(GameStatus.NEW)) {
                game.setCancelReason(CancelReason.valueOf(reason));
                game.setGameStatus(GameStatus.COMPLETED);
                gameService.updateGame(game);
                return game;
            }
            else if (!game.getGameStatus().equals(GameStatus.COMPLETED)) {
                // Updating Game status
                game.setCancelReason(CancelReason.valueOf(reason));
                game.setGameStatus(GameStatus.COMPLETED);
                // Updating bet status for every user
                Set<User> playerList = game.getPlayers();
                playerList.forEach(user -> {
                    Bet bet = bets.get(user.getId());
                    bet.setStatus(BetStatus.C);
                    bet.setPayoff(game.getBetAmount());
                    bet.setSettleTime(Timestamp.from(Instant.now()));
                    betList.add(bet);
                    // Set user status
                    user.setActive(false);
                    userService.updateUser(user);
                });
                betService.saveAllBets(betList);
                gameService.updateGame(game);
                return game;
            }
            else throw new GameException("Game Already Ended!");
        }
        else throw new GameException("Only creator can end!");
    }

    @RequestMapping("/{id}/simulate-game")
    @ResponseBody
    public void simulateGame(@PathVariable long id, Principal principal, HttpSession session){
        Game game = gameService.findById(id);
        if(game.getCreator().getUsername().equals(principal.getName())){
            if(game.getGameStatus().equals(GameStatus.IN_PROGRESS)) {
                HashMap<Long, Bet> bets = (HashMap<Long, Bet>) session.getAttribute("playerBets");
                LinkedHashMap<PlayerDTO, Integer> winnerList = gamePlayers.get(game.getId()).startSNL();
                Set<PlayerDTO> winners = winnerList.keySet();
                List<Bet> betList = new ArrayList<>();
                updateBets(winners, bets, game);
                LOGGER.info(winners.toString());
                LOGGER.info(gamePlayers.get(game.getId()).toString());
                gamePlayers.remove(game.getId());
                // Updating Game status
                game.setGameStatus(GameStatus.COMPLETED);
                gameService.updateGame(game);
            }else throw new GameException("Can't Simulate the Game, It has not Started or Already ended!");
        }else throw new GameException("Only Creator can simulate the game!");
    }

    @RequestMapping("/{id}/roll-die")
    @ResponseBody
    public String rollDie(@PathVariable long id, Principal principal, HttpSession session){
        Game game = gameService.findById(id);
        if(game.getGameStatus().equals(GameStatus.IN_PROGRESS)) {
            long userId = userService.findByUsername(principal.getName()).getId();
            GameLogic gameLogic = games.get(game.getId());
            int playerTurn = gameLogic.getPlayerTurn();
            if(gameLogic.getPlayers().size() > 0) {
                PlayerDTO player = gameLogic.getPlayers().get(playerTurn);
                if (gameLogic.getPlayers().get(playerTurn).getId() == userId) {
                    MoveDTO move = gameLogic.rollDie();
                    System.out.println(gameLogic.getPlayers().toString());
                    System.out.println(gameLogic.getWinnerList().toString());
                    games.put(game.getId(), gameLogic);
                    for(PlayerDTO players : gameLogic.getWinnerList().keySet()){
                        if(players.getId() == player.getId())
                            return "Player " + players.getId() +" finished the game";
                    }
                    return "players details are - " + move.toString();
                } else {
                    for(PlayerDTO players : gameLogic.getWinnerList().keySet()){
                        if(players.getId() == player.getId()) {
                            return "Player " + players.getId() + " already finished the game";
                        }
                    }
                    return "Not the Players turn!";
                }
            }
            else if(gameLogic.getPlayers().size() == 0){
                return "Game has ended !";
            }
            else return "Exception Occurred";
        }else throw new GameException("Can't Play the game in its current status!");
    }
    @RequestMapping("/{id}/endGame")
    @ResponseBody
    public void endGame(@PathVariable long id, Principal principal, HttpSession session) {
        User user = userService.findByUsername(principal.getName());
        Game game = gameService.findById(id);
        if(user.equals(game.getCreator())) {
            GameLogic gameLogic = games.get(game.getId());
            gameLogic.calculatePayout();
            LinkedHashMap<PlayerDTO, Integer> winners = gameLogic.getWinnerList();
            updateBets(winners.keySet(), (HashMap<Long, Bet>) session.getAttribute("playerBets"), game);
            game.setGameStatus(GameStatus.COMPLETED);
            gameService.updateGame(game);
            games.remove(game.getId());
        }
    }

    public void updateBets(Set<PlayerDTO> winners, HashMap<Long, Bet> bets, Game game) {
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
    }
}

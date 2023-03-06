package com.spring.game.Controller;

import com.spring.game.dto.GameDTO;
import com.spring.game.enums.BetStatus;
import com.spring.game.enums.CancelReason;
import com.spring.game.enums.GameStatus;
import com.spring.game.exceptions.GameException;
import com.spring.game.model.Bet;
import com.spring.game.model.Game;
import com.spring.game.model.GameType;
import com.spring.game.model.User;
import com.spring.game.service.BetService;
import com.spring.game.service.CurrencyService;
import com.spring.game.service.GameService;
import com.spring.game.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

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
        if(userBalance > gameDTO.getBetAmount()) {
            // Creating a new game
            game.setAssignGameName(gameDTO.getAssignGameName());
            game.setBetAmount(gameDTO.getBetAmount());
            game.setGameTypeId(gameDTO.getGameTypeId());
            game.setCreator(creator);
            game.getPlayers().add(creator);
            return gameService.createGame(game);
        }else throw new GameException("Can't Create Game with given bet amount! Insufficient balance!");

    }

    // Join the Game

    @RequestMapping("/{id}/join")
    @ResponseBody
    public Game joinGame(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Game game = gameService.findById(id);
        if(game.getGameStatus().equals(GameStatus.NEW)){
            //Checking if user can join game with given betAmount
            double betAmount = currencyService.convertToEuro(game.getBetAmount(), game.getCreator().getCurrencyCode());
            double userBalance = currencyService.convertToEuro(user.getWallet_amt(), user.getCurrencyCode());
            if(betAmount < userBalance) {
                // Adding User to game
                game.getPlayers().add(userService.findByUsername(principal.getName()));
                gameService.updateGame(game);
                return game;
            }else throw new GameException("Cannot join this game! Insufficient balance");
        }else throw new GameException("Cannot join this game! Game already started!");
    }

    @RequestMapping("/{id}/start")
    @ResponseBody
    public Game startGame(@PathVariable Long id, Principal principal) {
        User creator = userService.findByUsername(principal.getName());
        Game game = gameService.findById(id);
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
                //Storing bet info
                betService.saveBet(bet);
                //Updating user wallet
                user.setWallet_amt(user.getWallet_amt() - currencyService.convertFromEuro(bet.getAmount(), user.getCurrencyCode()));
                userService.updateUser(user);
            });
            gameService.updateGame(game);
            return game;
        }
        else throw new GameException("Only Creator can start the game");

    }

    @RequestMapping("/{id}/cancelGame")
    @ResponseBody
    public Game cancelGame(@PathVariable long id, @RequestBody String reason, Principal principal){
        Game game = gameService.findById(id);
        User creator = userService.findByUsername(principal.getName());
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
                    Bet bet = betService.getOneBet(user.getId(), game.getId());
                    bet.setStatus(BetStatus.C);
                    bet.setPayoff(0);
                    bet.setSettleTime(Timestamp.from(Instant.now()));
                    betService.saveBet(bet);
                    //Updating user wallet
                    user.setWallet_amt(user.getWallet_amt() + currencyService.convertFromEuro(bet.getAmount(), user.getCurrencyCode()));
                    userService.updateUser(user);
                });
                gameService.updateGame(game);
                return game;
            }
            else throw new GameException("Game Already Ended!");
        }
        else throw new GameException("Only creator can end!");
    }
}

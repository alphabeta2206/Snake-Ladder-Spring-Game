package com.Task1.Task.controller;

import com.Task1.Task.enums.CancelReason;
import com.Task1.Task.enums.GameStatus;
import com.Task1.Task.model.*;
import com.Task1.Task.service.BetService;
import com.Task1.Task.service.GameService;
import com.Task1.Task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Controller
public class GameController {
    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    BetService betService;

    @RequestMapping("/lobby")
    @ResponseBody
    public List<Game> lobby(){
        return gameService.gameList();
    }

    @RequestMapping("/creategame/{gametype}/{gamename}")
    @ResponseBody
    public String createGame(@PathVariable String gametype, @PathVariable String gamename, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Role role = new Role("ROLE_ADMIN");
        user.getRoles().add(role);
        user.setRoles(user.getRoles());
        Game game = new Game();
        game.setGametype(new GameType(gametype));
        game.setGameStartTime(new Timestamp(System.currentTimeMillis()));
        game.setCreator(user);
        game.setAssignGameName(gamename);
        game.setGameStatus(GameStatus.NEW);
        gameService.saveGame(game);
        return "Game Created";
    }

    @RequestMapping("/startgame/{gid}")
    @ResponseBody
    public String startGame(@PathVariable Long gid){
        Game game = gameService.getById(gid);
        Set<User> playerList = game.getPlayers();
        if(playerList.size()>1){
            game.setGameStatus(GameStatus.IN_PROGRESS);
            gameService.saveGame(game);
            playerList.forEach(user -> {
                Bet bet = new Bet();
                bet.setAmount(game.getBetAmount());
                user.setWalletAmt(user.getWalletAmt() - game.getBetAmount());
                bet.setPlaceTime(Timestamp.from(Instant.now()));
                bet.setGameId(game.getId());
                bet.setUserId(user.getId());
                betService.saveBet(bet);
                userService.saveUser(user);
            });
        }
        return "Minimum of Two Players required to start game";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/cancelgame/{gid}")
    @ResponseBody
    public String deleteGame(@PathVariable long gid) {
        Game game = gameService.getById(gid);
        Set<User> playerList = game.getPlayers();
        if(game.getGameStatus() == GameStatus.IN_PROGRESS){
            gameService.saveGame(game);
            playerList.forEach(user -> {
                Bet bet = betService.getByUserId(user.getId());
                bet.setPayOff(0);
                bet.setStatus('C');
                betService.saveBet(bet);
            });
        }
        game.setGameStatus(GameStatus.COMPLETED);
        game.setCancelReason(CancelReason.USER_CANCELLED);
        gameService.saveGame(game);
        return "deleted successfully";
    }

    @GetMapping("/joingame/{gid}")
    @ResponseBody
    public String joinGame(@PathVariable Long gid, Principal principal) {
        Game game = gameService.getById(gid);
        if (game.getGameStatus() == GameStatus.NEW) {
            User user = userService.getByUsername(principal.getName());
            game.getPlayers().add(user);
        }
        gameService.saveGame(game);
        return "Successfully Joined Game";
    }

    @RequestMapping("/endgame/{gid}/{payout}")
    @ResponseBody
    public String endGame(@PathVariable Long gid, @PathVariable double payout){
        Game game = gameService.getById(gid);
        Set<User> playerList = game.getPlayers();
        if(game.getGameStatus() == GameStatus.IN_PROGRESS){
            gameService.saveGame(game);
            playerList.forEach(user -> {
                Bet bet = betService.getByUserId(user.getId());
                bet.setPayOff(payout);
                user.setWalletAmt(user.getWalletAmt() + payout); // add logic for payout
                bet.setSettleTime(Timestamp.from(Instant.now()));
                bet.setStatus('S');
                betService.saveBet(bet);
            });
        }
        game.setGameStatus(GameStatus.COMPLETED);
        gameService.saveGame(game);
        return "Game End";
    }
}

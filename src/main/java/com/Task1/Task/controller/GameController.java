package com.Task1.Task.controller;

import com.Task1.Task.dto.GameDTO;
import com.Task1.Task.enums.CancelReason;
import com.Task1.Task.enums.GameStatus;
import com.Task1.Task.exceptions.GameException;
import com.Task1.Task.model.*;
import com.Task1.Task.service.BetService;
import com.Task1.Task.service.CurrencyService;
import com.Task1.Task.service.GameService;
import com.Task1.Task.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class GameController {

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    BetService betService;

    @Autowired
    CurrencyService currencyService;

    @RequestMapping("/lobby")
    @ResponseBody
    public List<Game> lobby() {
        return gameService.gameList();
    }

    @RequestMapping("/creategame/{gametype}/{gamename}/{betamount}")
    @ResponseBody
    public String createGame(@PathVariable String gametype, @PathVariable String gamename, @PathVariable String betamount , Principal principal) {
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
        game.getPlayers().add(user);
        game.setBetAmount( Double.parseDouble( betamount ) );
        gameService.saveGame(game);
        return "Game Created";
    }

//    @RequestMapping("/creategame")
//    @ResponseBody
//    public String createGame(@RequestBody GameDTO gameDTO, Principal principal){
//        User user = userService.getByUsername(principal.getName());
//        Role role = new Role("ROLE_ADMIN");
//        user.getRoles().add(role);
//        user.setRoles(user.getRoles());
//        Game game = new Game();
//        game.setGametype(new GameType(gameDTO.getGameType()));
//        game.setGameStartTime(new Timestamp(System.currentTimeMillis()));
//        game.setCreator(user);
//        game.setAssignGameName(gameDTO.getGameName());
//        game.setGameStatus(GameStatus.NEW);
//        game.getPlayers().add(user);
//        game.setBetAmount(gameDTO.getBetAmount());
//        gameService.saveGame(game);
//        return "Game Created";
//    }

    @RequestMapping("/startgame/{gid}")
    @ResponseBody
    public String startGame(@PathVariable Long gid, HttpSession session , Principal principal ) {

        Game game = gameService.getById(gid);
        // game can only be started by the user who created it
        System.out.print( game.getCreator().getUserName()+ " "+ principal.getName() +"\n" );
        if( ! game.getCreator().getUserName().equalsIgnoreCase( principal.getName() ) )return "You are not authorized to start the game" ;

        if( game.getGameStatus() == GameStatus.IN_PROGRESS )return "Game already started" ;
        Set<User> playerList = game.getPlayers();
        HashMap<Long, Bet> bets = new HashMap<>();
        if (playerList.size() > 1) {
            game.setGameStatus(GameStatus.IN_PROGRESS);
            gameService.saveGame(game);
            playerList.forEach(user -> {
                double multiplier = currencyService.getMultiplier(user.getCurrencyCode());
                Bet bet = new Bet();
                bet.setAmount(game.getBetAmount());
                // It should be divide by multiplier because multiplier is to convert any currency to euro
                // but here , converting euro to other currency
                //user.setWalletAmt(user.getWalletAmt() - (game.getBetAmount() * multiplier));
                bet.setPlaceTime(Timestamp.from(Instant.now()));
                bet.setGameId(game.getId());
                bet.setUserId(user.getId());
                bets.put(user.getId(), bet);
                //userService.saveUser(user);
                betService.saveBet( bet );

            });
            session.setAttribute("playerBets", bets);
            session.setAttribute("betAmount" , game.getBetAmount() );
        }else throw new GameException("Minimum of Two Players required to start game");
        return "Game Started";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/cancelgame/{gid}")
    public ResponseEntity<String > deleteGame(@PathVariable long gid) {
        Game game = gameService.getById(gid);
        Set<User> playerList = game.getPlayers();
        if (game.getGameStatus() == GameStatus.IN_PROGRESS || game.getGameStatus() == GameStatus.NEW) {
            gameService.saveGame(game);
            playerList.forEach(user -> {
                Bet bet = betService.getByUserId(user.getId());
                // if game is cancelled before starting the game then bet will be null
                // because bet is saved in startgame controller
                if( bet != null ) {
                    bet.setPayOff(0);
                    bet.setStatus('C');
                    betService.saveBet(bet);
                }
            });
        }else throw new GameException("Game Already Cancelled");
        game.setGameStatus(GameStatus.COMPLETED);
        game.setCancelReason(CancelReason.USER_CANCELLED);
        gameService.saveGame(game);
        return new ResponseEntity<String>( "Game Cancelled" ,HttpStatus.OK ) ;
    }

    @GetMapping("/joingame/{gid}")
    @ResponseBody
    public String joinGame(@PathVariable Long gid, Principal principal) {
        Game game = gameService.getById(gid);
        if (game.getGameStatus() == GameStatus.NEW) {
            User user = userService.getByUsername(principal.getName());
            game.getPlayers().add(user);
        }else if (game.getGameStatus() == GameStatus.IN_PROGRESS) throw new GameException("Game Already In Progress");
        else throw new GameException("Game Already Cancelled");
        gameService.saveGame(game);
        return "Joined Game";
    }

    @RequestMapping("/endgame/{gid}/{payout}")
    @ResponseBody
    @Transactional
    public String endGame(@PathVariable Long gid, @PathVariable double payout, HttpSession session) {
        Game game = gameService.getById(gid);
        Set<User> playerList = game.getPlayers();
        if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
            gameService.saveGame(game);
            HashMap<Long, Bet> bets = (HashMap<Long, Bet>) session.getAttribute("playerBets");

            playerList.forEach(user -> {
                Bet bet = bets.get(user.getId());
                double multiplier = currencyService.getMultiplier(user.getCurrencyCode());
                bet.setPayOff(payout);
                user.setWalletAmt(user.getWalletAmt() - (Double)( session.getAttribute("betAmount") ) / multiplier );
                user.setWalletAmt(user.getWalletAmt() + payout / multiplier);
                bet.setSettleTime(Timestamp.from(Instant.now()));
                bet.setStatus('S');
                betService.saveBet(bet);
                userService.saveUser( user );
            });
            game.setGameStatus(GameStatus.COMPLETED);
            gameService.saveGame(game);
        }
        else throw new GameException("Game Already Ended");

        return "Success";
    }
}

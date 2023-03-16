package com.Task1.Task.controller;

import com.Task1.Task.dto.GameDTO;
import com.Task1.Task.enums.CancelReason;
import com.Task1.Task.enums.GameStatus;
import com.Task1.Task.events.publishers.EventPublisher;
import com.Task1.Task.exceptions.GameException;
import com.Task1.Task.model.*;
import com.Task1.Task.service.BetService;
import com.Task1.Task.service.CurrencyService;
import com.Task1.Task.service.GameService;
import com.Task1.Task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @Autowired
    EventPublisher eventPublisher;

    @RequestMapping("/lobby")
    @ResponseBody
    public List<Game> lobby(HttpServletResponse response, Principal principal) {
        Cookie cookie = new Cookie("username", principal.getName());
        response.addCookie(cookie);
        return gameService.gameList();
    }

    @PostMapping("/create")
    @ResponseBody
    public String createGame(@RequestBody GameDTO gameDTO, Principal principal){
        User user = userService.getByUsername(principal.getName());
        Role role = new Role("ROLE_ADMIN");
        user.getRoles().add(role);
        user.setRoles(user.getRoles());
        Game game = new Game();
        game.setGametype(new GameType(gameDTO.getGameType()));
        game.setGameStartTime(new Timestamp(System.currentTimeMillis()));
        game.setCreator(user);
        game.setAssignGameName(gameDTO.getGameName());
        game.setGameStatus(GameStatus.NEW);
        game.getPlayers().add(user);
        game.setBetAmount(gameDTO.getBetAmount());
        gameService.saveGame(game);
        return "Game Created";
    }

    @PreAuthorize("ROLE_ADMIN")
    @Operation(summary = "Starts a Created Game")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Game Started", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @GetMapping("/{gid}/start")
    public ResponseEntity<?> startGame(@PathVariable Long gid, HttpSession session) {
        Game game = gameService.getById(gid);
        if (game != null) {
            if (game.getGameStatus() == GameStatus.IN_PROGRESS) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Game Already Started");
            else if (game.getGameStatus() == GameStatus.COMPLETED) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Game Already Completed");
            Set<User> playerList = game.getPlayers();
            HashMap<Long, Bet> bets = new HashMap<>();
            if (playerList.size() > 1) {
                playerList.forEach(user -> {
                    Bet bet = new Bet();
                    bet.setAmount(game.getBetAmount());
                    bet.setPlaceTime(Timestamp.from(Instant.now()));
                    bet.setGameId(game.getId());
                    bet.setUserId(user.getId());
                    bets.put(user.getId(), bet);
                });
                session.setAttribute("playerBets", bets);
                eventPublisher.publishStartGame(game); // publish game start
            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Such Game Exists");
        }
        return ResponseEntity.status(HttpStatus.OK).body(game);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Cancel New or In-Status Game")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Game Cancelled", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @GetMapping("/{gid}/cancel")
    public ResponseEntity<?> deleteGame(@PathVariable long gid, HttpSession session ) {
        Game game = gameService.getById(gid);
        if(game!=null){
            Set<User> playerList = game.getPlayers();
            List<Bet> betList = new ArrayList<>();
            HashMap<Long, Bet> bets = (HashMap<Long, Bet>) session.getAttribute("playerBets");
            if (bets != null) {
                if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
                    playerList.forEach(user -> {
                        Bet bet = bets.get(user.getId());
                        bet.setPayOff(game.getBetAmount());
                        bet.setStatus('C');
                        bet.setSettleTime(Timestamp.from(Instant.now()));
                        betList.add(bet);
                    });
                    betService.saveBets(betList);
                }else return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Game Already Completed");
            } else return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Bets Found in Session");
            game.setGameStatus(GameStatus.COMPLETED);
            game.setCancelReason(CancelReason.USER_CANCELLED);
            game.setPlayers( new HashSet<>());
            gameService.saveGame(game);
        }else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Such Game Exists");
        return ResponseEntity.status(HttpStatus.OK).body(game);
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
    @Deprecated
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
                bet.setSettleTime(Timestamp.from(Instant.now()));
                bet.setStatus('S');
                betService.saveBet(bet, user, multiplier);
            });
            game.setGameStatus(GameStatus.COMPLETED);
            game.setPlayers(new HashSet<>());
            gameService.saveGame(game);
            return "Game Has Ended";
        }
        else return "Game Already Ended";
    }

    @RequestMapping("/rolldie/{id}")
    @ResponseBody
    public String rollDie(@PathVariable long id, Principal principal){
        Game game = gameService.getById(id);
        if (game.getGameStatus() == GameStatus.IN_PROGRESS){
            long userId = userService.getByUsername(principal.getName()).getId();
            eventPublisher.publishRollDie(userId, id);
            return "Request for Die Roll Placed";
        }
        else return "Die Roll Only Allowed For Ongoing Games";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/simulategame/{gid}")
    @ResponseBody
    public String simulateGame(@PathVariable long gid, HttpSession session){
        Game game = gameService.getById(gid);
        if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
            eventPublisher.publishSimulateGame(game, (HashMap<Long, Bet>) session.getAttribute("playerBets"));
            game.setGameStatus(GameStatus.COMPLETED);
            gameService.saveGame(game);
            return "Request For Simulation Placed";
        }
        else return "Game State Does not Allow Simulation";
    }
}

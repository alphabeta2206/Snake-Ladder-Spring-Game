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
    EventPublisher eventPublisher;

    @Operation(summary = "List of Active Games")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Fetched Active Game List", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @GetMapping("/lobby")
    public ResponseEntity<List<Game>> lobby(HttpServletResponse response, Principal principal) {
        Cookie cookie = new Cookie("username", principal.getName());
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body(gameService.gameList());
    }

    @Operation(summary = "Creates a New Game")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Game Created", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @RequestMapping("/create")
    public ResponseEntity<Game> createGame(@RequestBody GameDTO gameDTO, Principal principal){
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
        return ResponseEntity.status(HttpStatus.OK).body(game);
    }

    @Operation(summary = "Starts a Created Game")
    @PreAuthorize("ROLE_ADMIN")
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
    @Operation(summary = "Cancel New or In-Progress Game")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Game Cancelled", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @GetMapping("/{gid}/cancel")
    public ResponseEntity<?> cancelGame(@PathVariable long gid, HttpSession session ) {
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

    @Operation(summary = "Join New Game")
    @GetMapping("/{gid}/join")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Joined Game", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    public ResponseEntity<?> joinGame(@PathVariable Long gid, Principal principal) {
        Game game = gameService.getById(gid);
        if (game.getGameStatus() == GameStatus.NEW) {
            User user = userService.getByUsername(principal.getName());
            game.getPlayers().add(user);
        }else if (game.getGameStatus() == GameStatus.IN_PROGRESS) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Game Already Started");
        else return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Game Already Completed");
        gameService.saveGame(game);
        return ResponseEntity.status(HttpStatus.OK).body(game);
    }

    @Operation(summary = "Roll Die")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Die Rolled"),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @GetMapping("/{id}/roll")
    public ResponseEntity<String> rollDie(@PathVariable long id, Principal principal){
        Game game = gameService.getById(id);
        if (game.getGameStatus() == GameStatus.IN_PROGRESS){
            long userId = userService.getByUsername(principal.getName()).getId();
            eventPublisher.publishRollDie(userId, id);
        }
        else return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Die Roll Only Allowed For Ongoing Games");
        return ResponseEntity.status(HttpStatus.OK).body("Request For Die Roll Placed");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Simulate Game")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Die Rolled"),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @GetMapping("/{gid}/sim")
    public ResponseEntity<String> simulateGame(@PathVariable long gid, HttpSession session){
        Game game = gameService.getById(gid); // add null check...
        if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
            eventPublisher.publishSimulateGame(game, (HashMap<Long, Bet>) session.getAttribute("playerBets"));
            game.setGameStatus(GameStatus.COMPLETED);
            gameService.saveGame(game);
        }
        else return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Game State Does Not Allow Simulation");
        return ResponseEntity.status(HttpStatus.OK).body("Game Simulation in Progress");
    }
}

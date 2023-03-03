package com.Task1.Task.controller;

import com.Task1.Task.enums.CancelReason;
import com.Task1.Task.enums.GameStatus;
import com.Task1.Task.model.Game;
import com.Task1.Task.model.GameType;
import com.Task1.Task.model.Role;
import com.Task1.Task.model.User;
import com.Task1.Task.service.GameService;
import com.Task1.Task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.sql.Timestamp;

@Controller
public class GameController {
    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @RequestMapping("/lobby")
    @ResponseBody
    public String lobby(Principal principal) {
        return "welcome " + principal.getName();
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
        game.setGameStatus(GameStatus.IN_PROGRESS);
        gameService.saveGame(game);
        return "Game Start";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/cancelgame/{gid}")
    @ResponseBody
    public String deleteGame(@PathVariable long gid) {
        Game game = gameService.getById(gid);
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

    @RequestMapping("/endgame/{gid}")
    @ResponseBody
    public String endGame(@PathVariable Long gid){
        Game game = gameService.getById(gid);
        game.setGameStatus(GameStatus.COMPLETED);
        gameService.saveGame(game);
        return "Game End";
    }
}

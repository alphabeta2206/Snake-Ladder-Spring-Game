package com.security.SecureApp.controller;

import com.security.SecureApp.Service.*;
import com.security.SecureApp.enums.BetStatus;
import com.security.SecureApp.modal.Bet;
import com.security.SecureApp.repository.GameRepository;
import com.security.SecureApp.modal.CancelFinishedGame;
import com.security.SecureApp.modal.Games;
import com.security.SecureApp.modal.User;
import com.security.SecureApp.repository.UserRepository;
import com.security.SecureApp.enums.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class GameController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MyUserDetailsService myUserDetailsServiceImpl;

    @Autowired
    GameService gameServiceImpl;

    @Autowired
    BetServiceImpl betService;

    @Autowired
    CancelFinishedGameService cancelFinishedGameService;

    @RequestMapping("/lobby")
    public String showLobby(Model model, Principal principal) {

        String username = principal.getName();
        model.addAttribute("user_details", myUserDetailsServiceImpl.findUserByUsername(username));
        model.addAttribute("games", gameServiceImpl.findAllByStatus(String.valueOf(GameStatus.ABOUT_TO_START)));
        return "lobbyhome";
    }
// THIS IS FOR THE INSERTION OF A NEW GAME THROUGH DIRECT API, THAT IS POSTMAN
    @PostMapping("/createGame")
    public ResponseEntity<Games> createGame(@RequestBody Games games, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        games.setCreation_time(now.format(dtf));
        games.setGamecreatorid(user.getId());
        games.setStatus(String.valueOf(GameStatus.ABOUT_TO_START));
        gameServiceImpl.saveGame(games);
        return new ResponseEntity<Games>(games, HttpStatus.CREATED);
    }


    @RequestMapping("/processCreateGame")
    @ResponseBody
    public ResponseEntity<Games> createGame(Principal principal, @RequestParam long bet_amount,
                                            @RequestParam String game_typeid,
                                            @RequestParam String game_name) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Games games = new Games();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        games.setBet_amount(bet_amount);
        games.setCreation_time(now.format(dtf));
        games.setGame_typeid(game_typeid);
        games.setGamecreatorid(user.getId());
        games.setGame_name(game_name);
        games.setStatus(String.valueOf(GameStatus.ABOUT_TO_START));
//        List<User> playerList = games.getPlayers();
//        playerList.add(user);
//        games.setPlayers(playerList);

        gameServiceImpl.saveGame(games);




        return new ResponseEntity<Games>(games, HttpStatus.CREATED);

    }

    @RequestMapping("/cancelgame/{id}/{uid}")
    public String cancelGame(Principal principal, @PathVariable long id, @PathVariable long uid, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Games games = gameServiceImpl.findbyId(id);

        if (user.getId() == games.getGamecreatorid()) {
            model.addAttribute("games", gameServiceImpl.findbyId(games.getId()));
            return "cancelreason";
        }
        else
        {
            System.out.println(user.getId()+ " - you cant cancel the game.");
            return "redirect:/lobby";
        }


    }

    @PostMapping("ProcessCancelGame/{id}")
    public String processCancelGame(@PathVariable long id,
                                    Model model,
                                    @ModelAttribute Games games) {
        //get the existing cancelreason-
        Games games1 = gameServiceImpl.findbyId(id);
        games.setId(games.getId());
        games1.setCancel_reason(games.getCancel_reason());
        games1.setStatus(String.valueOf(GameStatus.CANCELLED));

        gameServiceImpl.cancelGame(games1);



        return "redirect:/lobby";
    }



    @RequestMapping(value = "/joingame/{id}/{uid}" )
    public String joinGame(Principal principal, @PathVariable long id, @PathVariable long uid, Model model, @ModelAttribute Games games) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        if (user.isIn_game())
            return "You are already in a game";
        else
            user.setIn_game(true);
        myUserDetailsServiceImpl.saveUserDetails(user);

        Games games1 = gameServiceImpl.findbyId(id);

        model.addAttribute("user_details", myUserDetailsServiceImpl.findUserByUsername(username));
        model.addAttribute("games", gameServiceImpl.findbyId(games.getId()));

        try{
            betupdation(games1.getBet_amount(),games1.getId(),user.getId());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (games1.getPlayers().size() < 4)
        {
            games1.getPlayers().add(user);
            gameServiceImpl.saveGame(games1);
        }
        else throw new RuntimeException("Maximum number of players have been reached");

        return "redirect:/lobby";

    }

    public void betupdation(long bet_amount, long game_id, long user_id) {

        Bet bet = new Bet();
        bet.setAmount(bet_amount);
        bet.setGameId(game_id);
        bet.setUserId(user_id);
        bet.setStatus(Boolean.parseBoolean(String.valueOf(BetStatus.GAME_IN_PROGRESS)));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        bet.setPlaceTime(Timestamp.valueOf(now.format(dtf)));

        betService.saveBet(bet);


    }

}

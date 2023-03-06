package com.spring.game.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.game.enums.GameStatus;
import com.spring.game.enums.MessageType;
import com.spring.game.model.Game;
import com.spring.game.dto.GameMessage;
import com.spring.game.dto.GameUpdateMessage;
import com.spring.game.model.User;
import com.spring.game.service.GameService;
import com.spring.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.stream.Collectors;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        GameMessage gameMessage = objectMapper.readValue(payload, GameMessage.class);

        if (gameMessage.getType() == MessageType.JOIN_GAME) {
            Long gameId = gameMessage.getGameId();
            Game game = gameService.findById(gameId);
            User player = userService.findByUsername(gameMessage.getUsername());
            game.getPlayers().add(player);
            gameService.updateGame(game);

            GameUpdateMessage updateMessage = new GameUpdateMessage(game.getId(), game.getCreator().getUsername(),
                    game.getPlayers().stream().map(User::getUsername).collect(Collectors.toList()), game.getGameStatus());
            messagingTemplate.convertAndSend("/topic/game/" + game.getId(), updateMessage);
        } else if (gameMessage.getType() == MessageType.START_GAME) {
            Long gameId = gameMessage.getGameId();
            Game game = gameService.findById(gameId);
            if (!game.getCreator().getUsername().equals(gameMessage.getUsername())) {
                throw new UsernameNotFoundException("Only the creator can start the game");
            }
            game.setGameStatus(GameStatus.IN_PROGRESS);
            gameService.updateGame(game);

            GameUpdateMessage updateMessage = new GameUpdateMessage(game.getId(), game.getCreator().getUsername(),
                    game.getPlayers().stream().map(User::getUsername).collect(Collectors.toList()), game.getGameStatus());
            messagingTemplate.convertAndSend("/topic/game/" + game.getId(), updateMessage);
        }
    }
}
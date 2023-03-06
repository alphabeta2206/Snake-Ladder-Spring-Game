package com.spring.game.dto;
import com.spring.game.enums.MessageType;
import lombok.Data;
import org.springframework.messaging.simp.SimpMessageType;
@Data
public class GameMessage {
    private MessageType type;
    private Long gameId;
    private String username;
}
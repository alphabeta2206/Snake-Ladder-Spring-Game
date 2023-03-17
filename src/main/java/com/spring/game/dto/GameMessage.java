package com.spring.game.dto;
import com.spring.game.enums.MessageType;
import lombok.Data;
@Data
public class GameMessage {
    private MessageType type;
    private Long gameId;
    private String username;
}
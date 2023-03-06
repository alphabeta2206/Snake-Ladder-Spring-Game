package com.spring.game.dto;

import com.spring.game.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameUpdateMessage {

    private Long id;

    private String creator;

    private List<String> players;

    private GameStatus gameStatus;
}

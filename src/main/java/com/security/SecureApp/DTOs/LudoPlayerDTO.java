package com.security.SecureApp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LudoPlayerDTO {
    private long id;
    private int moves;
    private boolean two_sixes;
    private boolean three_sixes;
    private double payout;
    private int startPosition;
    private List<Character> pawnLobby;
    private Map<Character,Integer> pawnPosition;
    private List<Character> safeLobby;
    private int absolutePosition;
    private int tempPlayerID;


    public LudoPlayerDTO(long id){
        this.id = id;
        this.moves = 0;
        this.pawnLobby = new ArrayList<>();
        this.pawnLobby.add('a');
        this.pawnLobby.add('b');
        this.pawnLobby.add('c');
        this.pawnLobby.add('d');
        this.two_sixes = false;
        this.three_sixes = false;
        this.absolutePosition = startPosition;


    }
}
package com.security.SecureApp.gameplay.ludo;


import com.security.SecureApp.DTOs.LudoPlayerDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateBoard {
    public List<String> mainCreateBoard(){
        List<String> board = new LinkedList<>();
        for (int i = 1;i<51;i++){
            board.add(""+i);
        }
        return board;

    }

    public void getPawnPositions(List<LudoPlayerDTO> playerDTO, int playerID, String pawn){

    }
    public void setStartingPositions(List<LudoPlayerDTO> playerDTO){

    }



    public void startBoard(List<LudoPlayerDTO> playerDTO){
        if (playerDTO.size()==2){
            playerDTO.get(0).setStartPosition(1);
            playerDTO.get(1).setStartPosition(27);
            playerDTO.get(0).setAbsolutePosition(playerDTO.get(0).getStartPosition());
            playerDTO.get(1).setAbsolutePosition(playerDTO.get(1).getStartPosition());


        }
        else if (playerDTO.size()==3||playerDTO.size()==4){
            playerDTO.get(0).setStartPosition(1);
            for (int i=2;i< playerDTO.size();i++){
                playerDTO.get(i).setStartPosition((i-1)*52/4+1);
                playerDTO.get(i).setAbsolutePosition(playerDTO.get(i).getStartPosition());
            }
        }
    }
}

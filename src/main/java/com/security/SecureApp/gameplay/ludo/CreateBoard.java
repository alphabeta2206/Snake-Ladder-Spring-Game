package com.security.SecureApp.gameplay.ludo;


import com.security.SecureApp.DTOs.LudoPlayerDTO;

import java.util.*;

public class CreateBoard {
    public List<String> mainCreateBoard(){
        List<String> board = new LinkedList<>();
        for (int i = 1;i<53;i++){
            board.add(""+i);
        }
        return board;

    }

    public Map<Character, Integer> getPawnStartPosition(List<LudoPlayerDTO> playerDTO, int playerID){

        setStartingPositions(playerDTO);
        if (playerDTO.get(playerID).getPawnPosition()==null){
            Map<Character, Integer> temp = new HashMap<>();
            temp.put(playerDTO.get(playerID).getPawnLobby().get(0),playerDTO.get(playerID).getStartPosition());
            playerDTO.get(playerID).setPawnPosition(temp);
        }
       else {
            playerDTO.get(playerID).getPawnPosition().put(playerDTO.get(playerID).getPawnLobby().get(0),playerDTO.get(playerID).getStartPosition());
        }

        System.out.println(playerDTO.get(playerID).getPawnPosition());
        playerDTO.get(playerID).getPawnLobby().remove(0);


        return playerDTO.get(playerID).getPawnPosition();

    }
    public List<LudoPlayerDTO> getTempPlayerID(List<LudoPlayerDTO> playerDTO){
        for (int i = 0;i< playerDTO.size();i++ ){
            playerDTO.get(i).setTempPlayerID(i);
        }
        return playerDTO;
    }




    public void setStartingPositions(List<LudoPlayerDTO> playerDTO){
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

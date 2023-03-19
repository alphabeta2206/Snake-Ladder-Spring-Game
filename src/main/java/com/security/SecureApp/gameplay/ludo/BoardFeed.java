package com.security.SecureApp.gameplay.ludo;


import com.security.SecureApp.DTOs.LudoPlayerDTO;

import java.util.List;

public class BoardFeed {
    List<String> board;

    public List<String> makeMainBoard(List<LudoPlayerDTO> playersDTO) {

        //null points are there in the playerdtos and this is not able to manage that ...how to do is the question


        CreateBoard createBoard = new CreateBoard();
        board = createBoard.mainCreateBoard();
        String temp = "";

        for (int i = 0; i < playersDTO.size(); i++) {
            if (playersDTO.get(i).getPawnPosition()!=null){
                for (Character key : playersDTO.get(i).getPawnPosition().keySet()) {
                    temp = board.get(playersDTO.get(i).getPawnPosition().get(key));
                    board.set(playersDTO.get(i).getPawnPosition().get(key), temp + "_P" + (i + 1) + "-" + key);
                }
            }
            else {
                continue;
            }
        }
        System.out.println(playersDTO);
        return board;
    }

//    public int getStartingPosition(List<LudoPlayerDTO> players, int playerID){
//
//
//    }

    public List<String> updatePawnLobby(List<LudoPlayerDTO> playersDTO, int playerID) {
        System.out.println(playersDTO);
        char pawn = 'a';

        playersDTO.get(playerID).getPawnLobby();
        return null;
    }

//    public List<String> getSafeBoard(List<LudoPlayerDTO> playersDTO){
//
//    }


}








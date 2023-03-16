package com.security.SecureApp.gameplay.ludo;

import com.spring.game.dto.LudoPlayerDTO;

import java.util.List;
public class BoardFeed {
    List<String> board;
    public List<String> makeMainBoard(List<LudoPlayerDTO> playersDTO){
        try{
            CreateBoard createBoard = new CreateBoard();
            board = createBoard.mainCreateBoard();
            String temp = "";

            for (int i = 0; i < playersDTO.size(); i++) {
                for(String key : playersDTO.get(i).getPawnPositions().keySet()){
                    temp = board.get(playersDTO.get(i).getPawnPositions().get(key));
                    board.set(playersDTO.get(i).getPawnPositions().get(key), temp+"_P"+(i+1)+"-"+key);
                }
            }
            System.out.println(playersDTO);
            return board;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> updatePawnLobby(List<LudoPlayerDTO> playersDTO, int playerID){
        System.out.println(playersDTO);
        String pawn = "";

        CreateBoard createBoard = new CreateBoard();
        playersDTO.get(playerID).setPawnPositions(createBoard.getPawnPosition(playersDTO.size(), playerID,pawn));


return null;
    }

//    public List<String> getSafeBoard(List<LudoPlayerDTO> playersDTO){
//
//    }







}








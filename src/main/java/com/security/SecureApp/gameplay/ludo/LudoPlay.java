package com.security.SecureApp.gameplay.ludo;

import com.security.SecureApp.DTOs.LudoPlayerDTO;
import com.security.SecureApp.enums.LudoPawns;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LudoPlay {
    Scanner scanner = new Scanner(System.in);

    public void playLudo(List<LudoPlayerDTO> playerDTO){
        System.out.println("id");
        int id = scanner.nextInt();
        System.out.println("dice");
        int dice = scanner.nextInt();

        processPlayLudo(playerDTO,id-1,dice);

            }
    public void processPlayLudo(List<LudoPlayerDTO> playerDTO,int playerID,int dice){
        CreateBoard createBoard = new CreateBoard();

        System.out.println(playerDTO.get(playerID).getPawnPosition());
        if (playerDTO.get(playerID).getPawnPosition()==null){
            if (dice==6){
                System.out.println("ddddd");
                playerDTO.get(playerID).setPawnPosition(createBoard.getPawnStartPosition(playerDTO,playerID));

            }

        }


        if (playerDTO.get(playerID).getPawnPosition()!=null){
            System.out.println("Available Pawns for the player:"+playerDTO.get(playerID).getId());
            Set<Character> keySet= new HashSet<Character>(playerDTO.get(playerID).getPawnPosition().keySet());
            keySet.forEach(key-> System.out.println(key));

            System.out.println("Enter the pawn:");
            char pawn = scanner.next().charAt(0);
            int check=0;

            Character[] keySetArray = keySet.stream().toArray(Character[]::new);
            for (int i = 0; i<keySetArray.length;i++){
                if (keySetArray[i].equals(pawn)){
                    check++;
                }
            }
            if (check==1){
                playerDTO.get(playerID).getPawnPosition().put(pawn,playerDTO.get(playerID).getPawnPosition().get('a'));
                if (playerDTO.get(playerID).getPawnPosition().get(pawn)>51){
                    playerDTO.get(playerID).getPawnPosition().put(pawn,playerDTO.get(playerID).getPawnPosition().get(pawn)%52);
                }
                playerDTO.get(playerID).setAbsolutePosition(playerDTO.get(playerID).getAbsolutePosition()+dice);

                if (playerDTO.get(playerID).getAbsolutePosition()>playerDTO.get(playerID).getStartPosition()+51){

                }
            }
            else if (check==0){
                throw new RuntimeException("Select only the ones that are in the board");
            }
            else {
                throw new RuntimeException("FUck off");
            }
        }
        else {
            System.out.println("no pawns in the board");
        }
        System.out.println(playerDTO);
        System.out.println("This is the board:");
        BoardFeed boardFeed = new BoardFeed();
        System.out.println(boardFeed.makeMainBoard(playerDTO));
    }
}

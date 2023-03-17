package com.security.SecureApp.gameplay.ludo;

import com.security.SecureApp.DTOs.LudoPlayerDTO;
import com.security.SecureApp.enums.LudoPawns;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LudoPlay {
    Scanner scanner = new Scanner(System.in);

    public void playLudo(){
        System.out.println("id");
        int id = scanner.nextInt();
        System.out.println("dice");
        int dice = scanner.nextInt();

        processPlayLudo(playerDTO,id,dice);

            }
    public void processPlayLudo(List<LudoPlayerDTO> playerDTO,int playerID,int dice){

        System.out.println("Available Pawns for the player:"+playerDTO.get(playerID).getId());

        Set<Character> keySet= new HashSet<Character>(playerDTO.get(playerID).getPawnPosition().keySet());
        keySet.forEach(key-> System.out.println(key));

        System.out.println("Enter the pawn:");
        char pawn = scanner.next().charAt(0);
        //if the pawn is there in the available list then the pawn should move.
        int check=0;
        for (int i = 0; i<keySet.size();i++){
            String[] keySetArray = keySet.toArray(new String[keySet.size()]);
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
            throw new RuntimeException("Select only the ones that are outside the lobby");
        }

    }
}

package com.security.SecureApp.gameplay.ludo;


import com.security.SecureApp.DTOs.LudoPlayerDTO;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String...args){
        List<LudoPlayerDTO> playerDTOS = new ArrayList<>();
        playerDTOS.add(new LudoPlayerDTO(1L));
        playerDTOS.add(new LudoPlayerDTO(2L));
        playerDTOS.add(new LudoPlayerDTO(3L));
        playerDTOS.add(new LudoPlayerDTO(4L));

        System.out.println(playerDTOS);
    }
}

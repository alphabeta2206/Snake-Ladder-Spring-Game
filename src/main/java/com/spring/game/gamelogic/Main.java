package com.spring.game.gamelogic;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SNLReader snlReader = new SNLReader("/home/pp-in-564/Desktop/Task/src/main/java/com/spring/game/gamelogic/testfiles/moves1.txt");
        System.out.println(snlReader.getMoveList());
    }
}

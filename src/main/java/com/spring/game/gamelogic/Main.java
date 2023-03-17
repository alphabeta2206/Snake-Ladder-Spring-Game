package com.spring.game.gamelogic;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SNLReader snlReader = new SNLReader("/home/pp-in-507/IdeaProjects/spring-game/src/main/java/com/spring/game/gamelogic/testfiles/moves1.txt");
        System.out.println(snlReader.getMoveList());
    }
}

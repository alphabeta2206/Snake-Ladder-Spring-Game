package com.spring.game.gamelogic;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class SNLReader {
    private File moveFile;
    private List<List<Integer>> moveList;

    public SNLReader(String filePath) throws IOException {
        moveFile = new File(filePath);
        moveList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(moveFile));
        int players =  Integer.parseInt(br.readLine());
        for (int i = 0; i < players; i++) {
            moveList.add(Stream.of(br.readLine().split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
        }
    }
}

package com.Task1.Task.gamelogic;

import com.Task1.Task.dto.PlayerDTO;

import java.util.*;

public class SNL extends GameLogic {
    private HashMap<Integer, Integer> snakes; // Key = head, value = tail
    private HashMap<Integer, Integer> ladders; // Key = bottom, value = top
    private HashMap<Integer, Integer> bonusLadder;
    private HashMap<Integer, Integer> bonusSnake;

    public SNL(List<PlayerDTO> players, double pricePool) {
        super(players, pricePool);

        Random random = new Random();
        snakes = new HashMap<>();
        ladders = new HashMap<>();

        // Generate Random Snakes
        Map<Integer, Integer> SNAKE_CONFIG = Map.of(6, 2, 8, 1, 5, 3, 2, 1, 4, 3);
        for (Map.Entry<Integer, Integer> entry : SNAKE_CONFIG.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            int snakeHead = random.nextInt(8 * (key - 1) + 1, 8 * key + 1);
            int snakeTail = random.nextInt(8 * (value - 1) + 1, 8 * value + 1);
            snakes.put(snakeHead, snakeTail);
        }
        // Generate Random Ladders
        Map<Integer, Integer> LADDER_CONFIG = Map.of(2, 6, 1, 7, 3, 8, 5, 6, 7, 8);
        for (Map.Entry<Integer, Integer> entry : LADDER_CONFIG.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            int ladderBottom = random.nextInt(8 * (key - 1) + 1, 8 * key + 1);
            int ladderTop = random.nextInt(8 * (value - 1) + 1, 8 * value + 1);
            ladders.put(ladderBottom, ladderTop);
        }
    }

    @Override
    public void rollDie() {
        super.setTotalMoves(super.getTotalMoves() + 1);
        int dieValue = super.getDice().getValue();
        int playerTurn = super.getPlayerTurn() % super.getPlayers().size();
        PlayerDTO player = super.getPlayers().get(playerTurn);
        int nextPosition = player.getPosition() + dieValue;

        if (snakes.containsKey(nextPosition)) {
            nextPosition = snakes.get(nextPosition);
        } else if (ladders.containsKey(nextPosition)) {
            nextPosition = ladders.get(nextPosition);
        }
        System.out.println(getPlayers());
        player.setPosition(nextPosition);
        player.setMoves(player.getMoves() + 1);
        player.setPrevRoll(dieValue);
        super.getPlayers().set(playerTurn, player);
        System.out.println(getPlayers());
    }

    @Override
    public void calculatePayout() {
        super.getPlayers().forEach(player -> player.setPayout(getPayoutMultiplier(super.getWinnerList().get(player)) * super.getPricePool()));
    }

    @Override
    public double getPayoutMultiplier(int winNum) {
        int playerCount = super.getPlayers().size();
        if (playerCount == 2) {
            if (winNum == 0) return 1;
        } else if (playerCount == 3) {
            if (winNum== 0) return 0.6;
            else if (winNum == 1) return 0.4;
        } else if (playerCount == 4) {
            if (winNum == 0) return 0.5;
            else if (winNum== 1) return 0.3;
            else if (winNum == 2) return 0.2;
        }
        return 0;
    }

    @Override
    public void updateWinnerList(PlayerDTO player) {
        super.setPlayersWon(super.getPlayersWon() + 1);
        super.getWinnerList().put(player, super.getPlayersWon());
    }
}

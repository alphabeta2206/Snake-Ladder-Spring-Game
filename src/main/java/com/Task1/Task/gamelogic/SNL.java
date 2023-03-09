package com.Task1.Task.gamelogic;

import com.Task1.Task.dto.PlayerDTO;

import java.util.*;
import java.util.stream.Collectors;

public class SNL extends GameLogic {
    private HashMap<Integer, Integer> snakes; // Key = head, value = tail
    private HashMap<Integer, Integer> ladders; // Key = bottom, value = top
    private HashMap<Integer, Integer> bonusLadders;
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

    public boolean isPrime(int no) {
        int count = 0;
        for (int i = 1; i <= no; i++) {
            if (no % i == 0)
                count++;
        }
        return count == 2;
    }

    public void generateBonusLadder(int currentPlayerPosition) {
        // Generating to two levels above
        Random rand = new Random();
        for (int i = currentPlayerPosition + 1; i <= currentPlayerPosition + 6; i++) {
            if (bonusLadders.containsKey(i)) {
                return;
            }
        }
        int ladderStart = rand.nextInt(currentPlayerPosition, currentPlayerPosition + 6);
        int startLevel = ladderStart % 8;
        int endLevel = Math.min(startLevel + 3, 8);
        int ladderEnd = rand.nextInt((endLevel - 1) * 8 + 1, endLevel * 8 + 1);
        bonusLadders.put(ladderStart, ladderEnd);
    }

    @Override
    public void rollDie() {
        super.setTotalMoves(super.getTotalMoves() + 1);
        int dieValue = super.getDice().getValue();
        int playerTurn = super.getPlayerTurn();
        PlayerDTO player = super.getPlayers().get(playerTurn);
        int nextPosition = player.getPosition() + dieValue;

        if (snakes.containsKey(nextPosition)) {
            nextPosition = snakes.get(nextPosition);
        } else if (ladders.containsKey(nextPosition)) {
            nextPosition = ladders.get(nextPosition);
        }

        if (nextPosition<64){ // still playing
            player.setPosition(nextPosition);
            player.setMoves(player.getMoves() + 1);
            player.setPrevRoll(dieValue);
            super.getPlayers().set(playerTurn, player);
        }
        else {
            player.setPosition(nextPosition);
            player.setMoves(player.getMoves() + 1);
            player.setPrevRoll(dieValue);
            super.getPlayers().set(playerTurn, player);
            this.updateWinnerList(player); // player won
            super.updateGameState(player);
            playerTurn--;
        }

        playerTurn++;  // Update player turn
         if(playerTurn==super.getPlayers().size()) {
            playerTurn = 0;
//        super.setRound(super.getRound()+1);
        }
        super.setPlayerTurn(playerTurn);
    }

    @Override
    public void calculatePayout() {
        super.getWinnerList().forEach((player, value) -> player.setPayout(getPayoutMultiplier(value) * super.getPricePool()));
//        super.getWinnerList().entrySet().forEach(player -> player.getKey()setPayout(getPayoutMultiplier(super.getWinnerList().get(player)) * super.getPricePool()));
    }

    @Override
    public double getPayoutMultiplier(int winNum) {
        int playerCount = super.getWinnerList().size() + 1;
        if (playerCount == 2) {
            if (winNum == 0) return 1;
        } else if (playerCount == 3) {
            if (winNum == 0) return 0.6;
            else if (winNum == 1) return 0.4;
        } else if (playerCount == 4) {
            if (winNum == 0) return 0.5;
            else if (winNum == 1) return 0.3;
            else if (winNum == 2) return 0.2;
        }
        return 0;
    }

    @Override
    public void updateWinnerList(PlayerDTO player) {
        super.setPlayersWon(super.getPlayersWon() + 1);
        super.getWinnerList().put(player, super.getPlayersWon() - 1);
//        System.out.println(getWinnerList());
    }
}

package com.Task1.Task.gamelogic;

import com.Task1.Task.dto.BonusLadderDTO;
import com.Task1.Task.dto.BonusSnakeDTO;
import com.Task1.Task.dto.PlayerDTO;

import java.util.*;

public class SNL extends GameLogic {
    private HashMap<Integer, Integer> snakes; // Key = head, value = tail
    private HashMap<Integer, Integer> ladders; // Key = bottom, value = top

    private List<BonusLadderDTO> bonusLadders;

    private List<BonusSnakeDTO> bonusSnakes;

    public SNL(List<PlayerDTO> players, double pricePool) {
        super(players, pricePool);
        bonusLadders = new ArrayList<BonusLadderDTO>();
        bonusSnakes = new ArrayList<BonusSnakeDTO>();

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
        Random rand = new Random();
        BonusLadderDTO bonusLadder = new BonusLadderDTO();

        for(BonusLadderDTO ladder: this.bonusLadders){
            if (ladder.getLadderStart() > currentPlayerPosition && ladder.getLadderStart() < currentPlayerPosition + 6){
                ladder.setLife(ladder.getLife() + 1);
                return;
            }
        }
        boolean flag = true;
        int ladderStart = 0;
        while(flag) {
            ladderStart = rand.nextInt(currentPlayerPosition, currentPlayerPosition + 6);
            if(ladders.containsKey(ladderStart) || snakes.containsKey(ladderStart)) continue;
            flag = false;
        }

        int startLevel = ladderStart % 8;
        int endLevel = Math.min(startLevel + 3, 8);
        int ladderEnd = rand.nextInt((endLevel - 1) * 8 + 1, endLevel * 8 + 1);
        bonusLadder.setLadderStart(ladderStart);
        bonusLadder.setLadderEnd(ladderEnd);
        bonusLadder.setLife(super.getPlayers().size());
        this.bonusLadders.add(bonusLadder);
    }

    public void generateBonusSnake(int currentPlayerPosition){
        Random rand = new Random();
        BonusSnakeDTO bonusSnake = new BonusSnakeDTO();

        for(BonusSnakeDTO snake: this.bonusSnakes){
            if (snake.getSnakeStart() > currentPlayerPosition && snake.getSnakeStart() < currentPlayerPosition + 6){
                snake.setLife(snake.getLife() + 1);
                return;
            }
        }
        boolean flag = true;
        int snakeStart = 0;
        while(flag) {
            snakeStart = rand.nextInt(currentPlayerPosition, currentPlayerPosition + 6);
            if(ladders.containsKey(snakeStart) || snakes.containsKey(snakeStart)) continue;
            flag = false;
        }

        int snakeEnd = rand.nextInt(7) + 1;
        bonusSnake.setSnakeStart(snakeStart);
        bonusSnake.setSnakeEnd(snakeEnd);
        bonusSnake.setLife(super.getPlayers().size());
        this.bonusSnakes.add(bonusSnake);
    }

    @Override
    public void rollDie() {
        super.setTotalMoves(super.getTotalMoves() + 1);
        int dieValue = super.getDice().getValue();
        int playerTurn = super.getPlayerTurn();
        PlayerDTO player = super.getPlayers().get(playerTurn);
        int nextPosition = player.getPosition() + dieValue;

        bonusLadders.forEach(ladder -> ladder.setLife(ladder.getLife() - 1));


        if (snakes.containsKey(nextPosition)) {
            nextPosition = snakes.get(nextPosition);
        } else if (ladders.containsKey(nextPosition)) {
            nextPosition = ladders.get(nextPosition);
        } else {
            for (BonusLadderDTO bonusLadder : bonusLadders) {
                if (bonusLadder.getLadderStart() == nextPosition) nextPosition = bonusLadder.getLadderEnd();
            }
            for(BonusSnakeDTO bonusSnake: bonusSnakes){
                if (bonusSnake.getSnakeStart() == nextPosition) nextPosition = bonusSnake.getSnakeEnd();
            }
        }

        if(player.getPrevRoll() == 6 && !player.isThreeSixes())  {
            if(isPrime(dieValue + 6)) generateBonusLadder(nextPosition); // generate bonus ladder
            if(player.isTwoSixes() && dieValue == 6) {
                player.setTwoSixes(false);
                generateBonusSnake(nextPosition); // generate bonus snake
                player.setThreeSixes(true);
            }
            else if (!player.isTwoSixes() && dieValue == 6) player.setTwoSixes(true);
        }
        else player.setThreeSixes(false);

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
            if(playerTurn > 0) playerTurn--;
        }


        if(dieValue!=6 && !player.isThreeSixes()) playerTurn++; // Update player turn
        if(playerTurn==super.getPlayers().size()) {
            playerTurn = 0;
        }
        super.setPlayerTurn(playerTurn);
        bonusLadders.removeIf(ladder -> ladder.getLife() == 0);
    }

    @Override
    public void calculatePayout() {
        super.getWinnerList().forEach((player, value) -> player.setPayout(getPayoutMultiplier(value) * super.getPricePool()));
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
    }
}

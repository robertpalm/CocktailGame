package com.ridango.game;

import java.util.HashSet;
import java.util.Set;

public class GameStatus {
    private int score;
    private int remainingTries;
    private Set<String> seenCocktailIds;

    public GameStatus() {
        this.score = 0;
        this.remainingTries = 5;
        this.seenCocktailIds = new HashSet<>();
    }

    public int getScore() {
        return score;
    }

    public int getRemainingTries() {
        return remainingTries;
    }

    public void decreaseTries() {
        if (remainingTries > 0) {
            remainingTries--;
        }
    }

    public void addPoints(int points) {
        score += points;
    }

    public void resetTries() {
        remainingTries = 5;
    }

    public boolean isGameOver() {
        return remainingTries == 0;
    }

    public boolean isDrinksSeen(String id) {
        return seenCocktailIds.contains(id);
    }

    public void markDrinkAsSeen(String id) {
        seenCocktailIds.add(id);
    }
}

package com.ridango.game;

public class GameStatus {
    private int score;
    private int remainingTries;

    public GameStatus() {
        this.score = 0;
        this.remainingTries = 5;
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
}

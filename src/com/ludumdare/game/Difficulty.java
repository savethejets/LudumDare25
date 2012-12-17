package com.ludumdare.game;

public class Difficulty {

    private int difficulty = 1;

    public float getSpaceBetween() {
        return difficulty * 20 + 100 < 260 ? difficulty * 20 + 120 : 260f;
    }

    public int getNumberOfInvaders() {
        if (difficulty == 2) {
            return 3;
        } else if (difficulty == 3) {
            return 4;
        } else if (difficulty == 5) {
            return 5;
        } else if (difficulty > 5) {
            return 6;
        }

        return 2;
    }

    public int getNumberOfShooters() {
        if (difficulty == 2) {
            return 2;
        } else if (difficulty == 3) {
            return 3;
        } else if (difficulty == 5) {
            return 4;
        } else if (difficulty > 5) {
            return 6;
        }
        return 1;
    }

    public int getNumberOfShields() {
        if (difficulty > 5) {
            return 6;
        }
        return 3;
    }

    public void increaseDifficulty() {
        difficulty++;
    }

    public void reset() {
        difficulty = 1;
    }
}

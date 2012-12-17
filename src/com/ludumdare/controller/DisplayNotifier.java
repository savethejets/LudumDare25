package com.ludumdare.controller;

import com.ludumdare.scene2d.GameScreen;

public class DisplayNotifier {
    private static DisplayNotifier ourInstance = new DisplayNotifier();

    public enum DisplayEvent {
        GAME_OVER_WIN,
        SHAKE_CAMERA,
        GAME_OVER_LOSE, PLAY_SOUND
    }

    GameScreen gameScreen;

    public static DisplayNotifier getInstance() {
        return ourInstance;
    }

    private DisplayNotifier() {
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void notify(DisplayEvent displayEvent, Object... objects) {
        gameScreen.notifyOfDislayEventRequest(displayEvent, objects);
    }
}

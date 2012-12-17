package com.ludumdare;

import com.badlogic.gdx.Game;
import com.ludumdare.scene2d.GameScreen;
import com.ludumdare.scene2d.SplashScreen;

public class LudumDareMain extends Game {
    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }
}

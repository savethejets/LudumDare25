package com.ludumdare.entities;

import com.ludumdare.controller.GameBoardInformation;

public class InvaderBullet extends AbstractBullet {
    private static final float VELOCITY = 100f;

    @Override
    public void doOnCollision(Object object) {
        
    }

    @Override
    public float getWidth() {
        return 4;
    }

    @Override
    public float getHeight() {
        return 8;
    }

    @Override
    public void update(float delta) {
        position.y -= delta * VELOCITY;


        if (position.y < -100) {
            cleanUp = true;
        }
    }
}

package com.ludumdare.entities;

import com.ludumdare.controller.GameBoardInformation;

public class ShooterBullet extends AbstractBullet {

    private static final float VELOCITY = 100f;

    @Override
    public void doOnCollision(Object object) {
        if (object instanceof InvaderBullet) {
            ((InvaderBullet) object).cleanUp = true;
            cleanUp = true;
        }
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
        position.y += delta * VELOCITY;

        if (position.y > GameBoardInformation.getInstance().getScreenSize().height + 100) {
            cleanUp = true;
        }
    }
}

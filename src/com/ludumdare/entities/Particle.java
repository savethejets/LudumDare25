package com.ludumdare.entities;

public class Particle extends AbstractEntity implements Updateable {

    float xVelocity;
    float yVelocity;

    float aliveTime;

    @Override
    public void doOnCollision(Object object) {

    }

    @Override
    public float getWidth() {
        return 10;
    }

    @Override
    public float getHeight() {
        return 10;
    }

    @Override
    public void update(float delta) {
        getPosition().x += delta * xVelocity;
        getPosition().y += delta * yVelocity;

        aliveTime -= delta * 3;

        if (aliveTime <= 0) {
            cleanUp = true;
        }
    }

    public float getAliveTime() {
        return aliveTime;
    }
}
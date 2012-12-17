package com.ludumdare.entities;

public class Shield extends AbstractEntity implements Updateable {

    private float health = 100f;

    private boolean isDead = false;

    public void damage(float amount) {
        health -= amount;

        if (health <= 0) {
            isDead = true;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void doOnCollision(Object object) {
        if (object instanceof InvaderBullet) {
            ((InvaderBullet) object).cleanUp = true;
        }
    }

    @Override
    public float getWidth() {
        return 64;
    }

    @Override
    public float getHeight() {
        return 16;
    }
}

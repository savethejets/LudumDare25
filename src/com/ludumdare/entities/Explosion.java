package com.ludumdare.entities;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Explosion extends AbstractEntity implements Updateable, CompositeDrawable {

    private List<AbstractEntity> particleList = new ArrayList<AbstractEntity>();

    public Explosion(Vector2 position) {

        Random random = new Random();

        setPosition(position);

        for (int i = 0; i < 10; i++) {

            Particle particle = new Particle();

            particle.setPosition(new Vector2(position.x + random.nextInt(10), position.y));
            particle.xVelocity = -80 + random.nextInt(140);
            particle.yVelocity = -65f;
            particle.aliveTime = 10f;

            particleList.add(particle);

        }

    }

    @Override
    public void doOnCollision(Object object) {

    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public void update(float delta) {
        for (int i = particleList.size()-1; i >= 0; i--) {
            if (particleList.get(i) instanceof Updateable) {
                ((Updateable) particleList.get(i)).update(delta);

                if (particleList.get(i).isCleanUp()) {
                    particleList.remove(i);
                }
            }
        }

        if (particleList.isEmpty()) {
            cleanUp = true;
        }
    }

    @Override
    public List<AbstractEntity> getChildren() {
        return particleList;
    }
}

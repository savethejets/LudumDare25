package com.ludumdare.entities;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class ImminentDangerDetector extends AbstractEntity implements Updateable, NotRenderable {

    private Shooter shooter;
    private List<Shield> shields;

    public ImminentDangerDetector(Shooter shooter, List<Shield> shields) {
        this.shooter = shooter;
        this.shields = shields;
        this.shooter.setImminentDangerDetector(this);
        setPosition(new Vector2(shooter.position.x, shooter.position.y));
    }

    @Override
    public void doOnCollision(Object object) {
        if (object instanceof InvaderBullet) {
            Shield nearestShield = findNearestSheild();
            if (nearestShield != null) {
                shooter.moveUnderShield(nearestShield);
            }
        }
    }

    boolean isUnderAShield() {
        for (Shield shield : shields) {
            if (shooter.position.x >= shield.getPosition().x && shooter.position.x + shooter.getWidth() <= shield.getPosition().x + shield.getWidth()) {
                return true;
            }
        }
        return false;

    }

    Shield findNearestSheild() {
        Shield nearestShield = null;
        float nearestDistance = 1000000;
        for (Shield shield : shields) {
            float dst = shield.getPosition().dst(shooter.position);
            if (dst < nearestDistance) {
                nearestDistance = dst;
                nearestShield = shield;
            }
        }
        return nearestShield;
    }

    @Override
    public float getWidth() {
        return shooter.getWidth() + 4;
    }

    @Override
    public float getHeight() {
        return 80f;
    }

    @Override
    public void update(float delta) {
        getPosition().x = shooter.position.x - 2;
        getPosition().y = shooter.position.y;
    }
}

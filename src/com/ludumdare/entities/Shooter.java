package com.ludumdare.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare.controller.DisplayNotifier;
import com.ludumdare.controller.EntitySpawner;
import com.ludumdare.controller.SoundController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shooter extends AbstractEntity implements Updateable {

    private static final float VELOCITY = 80f;
    public static float TIME_BETWEEN_SHOTS = 0.9f;
    public static final float TIME_TO_WAIT_UNDER_SHIELD = 0.7f;
    public static final float TIME_TO_WAIT_FOR_RESPAWN = 6f;
    private boolean hold;

    // fix weird pause bug - RAWR HACKZORS!
    private int numberOfTimesWeDidNotMove = 0;

    public enum AIState {
        MOVE_TO_TARGET,
        SHOOT,
        MOVE_TO_COVER,
        AWAIT_ORDERS,
        HIDE_TILL_SAFE,
        DEAD_WAIT_TO_RESPAWN;


    }
    private AbstractEntity target = null;

    private AIState aiState = AIState.AWAIT_ORDERS;
    private float timeBetweenShots = TIME_BETWEEN_SHOTS;
    private float timeWaitingUnderShield = TIME_TO_WAIT_UNDER_SHIELD;
    private float timeToWaitTillRespawn = TIME_TO_WAIT_FOR_RESPAWN;
    private ImminentDangerDetector imminentDangerDetector;
    public void findTarget(List<AbstractEntity> entities) {

        Invader closestInvader = null;

        float closestDistance = 1000000;

        Random random = new Random();

        boolean findClosestTarget = random.nextBoolean();

        if (!entities.isEmpty()) {
            if (findClosestTarget) {
                for (AbstractEntity entity : entities) {
                    if (entity instanceof Invader) {
                        float dst = getPosition().dst(entity.getPosition());

                        if (dst < closestDistance) {
                            closestDistance = dst;
                            closestInvader = (Invader) entity;
                        }
                    }
                }
            } else {
                // in this case get a random invader to shoot at.
                List<Invader> listOfInvaders = new ArrayList<Invader>();
                for (AbstractEntity entity : entities) {
                    if (entity instanceof Invader) {
                        listOfInvaders.add((Invader) entity);
                    }
                }
                if (listOfInvaders.size() > 1) {
                    int i = random.nextInt(listOfInvaders.size() - 1) - 1;
                    if (i >= 0 && i < listOfInvaders.size()) {
                        closestInvader = listOfInvaders.get(i);
                    }
                } else if (!listOfInvaders.isEmpty()) {
                    closestInvader = listOfInvaders.get(0);
                }
            }
        }

        target = closestInvader;
    }

    public boolean shouldFindNewTarget() {
        return target == null && !AIState.DEAD_WAIT_TO_RESPAWN.equals(aiState);
    }

    @Override
    public void update(float delta) {

        if (!hold) {
            if (AIState.AWAIT_ORDERS.equals(aiState)) {
                if (target != null) {
                    aiState = AIState.MOVE_TO_TARGET;
                }
            }

            if (AIState.MOVE_TO_TARGET.equals(aiState)) {
                MoveDirection moveDirection = null;

                Vector2 oldPosition = new Vector2(getPosition().x, getPosition().y);

                Rectangle rectangle = new Rectangle(target.getPosition().x, -50, target.getWidth(), 10000);

                if (rectangle.contains(getPosition().x, getPosition().y)) {
                    if (target instanceof Invader) {
                        if (!imminentDangerDetector.isUnderAShield()) {
                            target = null;
                            aiState = AIState.SHOOT;
                        }
                    } else {
                        aiState = AIState.HIDE_TILL_SAFE;
                    }
                } else if (target.getPosition().x > getPosition().x) {
                    moveDirection = MoveDirection.RIGHT;
                } else if (target.getPosition().x < getPosition().x) {
                    moveDirection = MoveDirection.LEFT;
                }

                if (MoveDirection.RIGHT.equals(moveDirection)) {
                    position.x += delta * VELOCITY;
                }

                if (MoveDirection.LEFT.equals(moveDirection)) {
                    position.x -= delta * VELOCITY;
                }

                if (position.x == oldPosition.x && oldPosition.y == position.y) {
                    numberOfTimesWeDidNotMove++;
                }

                if (numberOfTimesWeDidNotMove > 30) {
                    target = null;
                    aiState = AIState.AWAIT_ORDERS;
                    numberOfTimesWeDidNotMove = 0;
                }
            }

            if (AIState.SHOOT.equals(aiState)) {
                if (timeBetweenShots <= 0 && target instanceof Invader) {
                    shootBulletAt((Invader) target);
                    aiState = AIState.AWAIT_ORDERS;
                    timeBetweenShots = TIME_BETWEEN_SHOTS;
                } else {
                    timeBetweenShots -= delta * 3;
                }
            }

            if (AIState.HIDE_TILL_SAFE.equals(aiState)) {
                timeWaitingUnderShield -= delta * 3;
                if (timeWaitingUnderShield <= 0) {
                    timeWaitingUnderShield = TIME_TO_WAIT_UNDER_SHIELD;
                    aiState = AIState.AWAIT_ORDERS;
                    target = null;
                }
            }

            if (AIState.DEAD_WAIT_TO_RESPAWN.equals(aiState)) {
                timeToWaitTillRespawn -= delta * 3;

                if (timeToWaitTillRespawn <= 0) {
                    timeToWaitTillRespawn = TIME_TO_WAIT_FOR_RESPAWN;
                    position.x = 0;
                    aiState = AIState.AWAIT_ORDERS;
                }
            }
        }

    }

    private void shootBulletAt(Invader target) {
        ShooterBullet bullet = new ShooterBullet();

        bullet.setPosition(new Vector2(getPosition().x + getWidth() / 2, getPosition().y + getHeight()));

        EntitySpawner.getInstance().spawn(bullet);

        DisplayNotifier.getInstance().notify(DisplayNotifier.DisplayEvent.PLAY_SOUND, SoundController.SoundId.SHOOTER_SHOOT);
    }

    public boolean isDodgingBullet() {
        return AIState.MOVE_TO_COVER.equals(aiState);
    }

    public float getTimeBetweenShots() {
        return timeBetweenShots;
    }

    public void setTimeBetweenShots(float timeBetweenShots) {
        this.timeBetweenShots = timeBetweenShots;
    }

    public void moveUnderShield(Shield object) {
        if (!AIState.DEAD_WAIT_TO_RESPAWN.equals(aiState)) {
            target = object;
            aiState = AIState.MOVE_TO_TARGET;
        }
    }

    @Override
    public void doOnCollision(Object object) {
        if (object instanceof InvaderBullet) {
            position.x = -1000;
            aiState = AIState.DEAD_WAIT_TO_RESPAWN;
            target = null;

            DisplayNotifier.getInstance().notify(DisplayNotifier.DisplayEvent.PLAY_SOUND, SoundController.SoundId.SHOOTER_DESTROYED );
        }
    }

    public void setImminentDangerDetector(ImminentDangerDetector imminentDangerDetector) {
        this.imminentDangerDetector = imminentDangerDetector;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    @Override
    public float getWidth() {
        return 32;
    }

    @Override
    public float getHeight() {
        return 16;
    }
}

package com.ludumdare.entities;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare.controller.DisplayNotifier;
import com.ludumdare.controller.EntitySpawner;
import com.ludumdare.controller.GameBoardInformation;
import com.ludumdare.controller.SoundController;

import java.util.Random;

public class Invader extends AbstractEntity implements Updateable {

    private static final float DISTANCE_TO_MOVE_UP_OR_DOWN = 25f;
    public static final float SIZE = 32f;
    public static float VELOCITY = 60f;

    private MoveDirection moveDirection;

    private float movedUpDownDistance;
    private boolean isOpposite = false;
    private boolean isHold = false;

    private boolean isDead = false;

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(MoveDirection moveDirection) {
        if (!MoveDirection.UP.equals(this.moveDirection) && !MoveDirection.DOWN.equals(this.moveDirection)) {
            if (isOpposite) {
                this.moveDirection = moveDirection.getOpposite();
            } else {
                this.moveDirection = moveDirection;
            }
        } else {
            this.moveDirection = this.moveDirection.getOpposite();
        }
    }
    @Override
    public void update(float delta) {

        if (!isHold) {
            if (MoveDirection.RIGHT.equals(moveDirection)) {

                if (willMovingRightPutOutOfBounds(delta)) {
                    if (isOpposite) {
                        moveDirection = MoveDirection.UP;
                    } else {
                        moveDirection = MoveDirection.DOWN;
                    }

                    movedUpDownDistance = 0;
                }

                position.x += delta * VELOCITY;

            }

            if (MoveDirection.LEFT.equals(moveDirection)) {

                if (willMovingLeftPutOutOfBounds(delta)) {
                    if (isOpposite) {
                        moveDirection = MoveDirection.UP;
                    } else {
                        moveDirection = MoveDirection.DOWN;
                    }
                    movedUpDownDistance = 0;
                }

                position.x -= delta * VELOCITY;

            }

            if (MoveDirection.DOWN.equals(moveDirection)) {

                if (isDoneMovingUpOrDown()) {
                    moveDirection = GameBoardInformation.getInstance().getInvaderMoveDirection().getOpposite();
                    isOpposite = !isOpposite;

                    Invader.VELOCITY += 0.5f;
                }


                movedUpDownDistance += delta * VELOCITY;
                position.y -= delta * VELOCITY;
            }

            if (MoveDirection.UP.equals(moveDirection)) {

                if (isDoneMovingUpOrDown()) {
                    moveDirection = GameBoardInformation.getInstance().getInvaderMoveDirection();
                    isOpposite = !isOpposite;

                    Invader.VELOCITY -= 0.2f;
                }


                movedUpDownDistance += delta * VELOCITY;
                position.y += delta * VELOCITY;
            }
        }

    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    private boolean isDoneMovingUpOrDown() {
        return movedUpDownDistance > DISTANCE_TO_MOVE_UP_OR_DOWN;
    }

    public void setOpposite(boolean opposite) {
        this.isOpposite = opposite;
    }

    public boolean isMovingUpOrDown() {
        return MoveDirection.DOWN.equals(moveDirection) || MoveDirection.UP.equals(moveDirection);
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    private boolean willMovingLeftPutOutOfBounds(float delta) {
        return position.x - (delta * VELOCITY) <= 0;
    }

    private boolean willMovingRightPutOutOfBounds(float delta) {
        return position.x + (delta * VELOCITY) + getWidth() > GameBoardInformation.getInstance().getScreenSize().width;
    }

    @Override
    public void doOnCollision(Object object) {
        if (object instanceof ShooterBullet) {

            DisplayNotifier.getInstance().notify(DisplayNotifier.DisplayEvent.SHAKE_CAMERA);

            DisplayNotifier.getInstance().notify(DisplayNotifier.DisplayEvent.PLAY_SOUND, SoundController.SoundId.INVADER_DESTROYED);

            EntitySpawner.getInstance().spawn(new Explosion(getPosition()));

            cleanUp = true;
            ((ShooterBullet) object).cleanUp = true;
        }
    }

    public boolean isHold() {
        return isHold;
    }

    public void setHold(boolean hold) {
        isHold = hold;
    }

    @Override
    public float getWidth() {
        return SIZE;
    }

    @Override
    public float getHeight() {
        return SIZE;
    }

    public void shoot() {
        AbstractBullet bullet = new InvaderBullet();

        bullet.setPosition(new Vector2(getPosition().x + getWidth() / 2, getPosition().y - getHeight()));

        EntitySpawner.getInstance().spawn(bullet);

        DisplayNotifier.getInstance().notify(DisplayNotifier.DisplayEvent.PLAY_SOUND, SoundController.SoundId.INVADER_SHOOT);
    }
}

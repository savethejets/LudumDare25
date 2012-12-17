package com.ludumdare.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare.util.Size;

import java.util.List;

public abstract class AbstractEntity implements Collidable {

    public enum MoveDirection {
        RIGHT,
        LEFT,
        DOWN,
        UP;

        public MoveDirection getOpposite() {
            if (this.equals(RIGHT)) {
                return LEFT;
            }

            if (this.equals(LEFT)) {
                return RIGHT;
            }

            if (this.equals(DOWN)) {
                return UP;
            }

            if (this.equals(UP)) {
                return DOWN;
            }

            return null;
        }

    }

    protected Vector2 position;
    protected boolean cleanUp;

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isCleanUp() {
        return cleanUp;
    }

    @Override
    public void testCollision(List<AbstractEntity> entities) {
        Rectangle selfRectangle = new Rectangle(position.x, position.y, getWidth(), getHeight());

        for (AbstractEntity entity : entities) {
            if (!entity.isCleanUp() && !entity.equals(this)) {
                Rectangle entityRectangle = new Rectangle(entity.getPosition().x, entity.getPosition().y, entity.getWidth(), entity.getHeight());

                if (selfRectangle.overlaps(entityRectangle)) {
                    doOnCollision(entity);
                }
            }
        }
    }

    public abstract void doOnCollision(Object object);

    public abstract float getWidth();
    public abstract float getHeight();



}

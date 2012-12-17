package com.ludumdare.scene2d.drawdelegates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ludumdare.entities.AbstractEntity;

public interface Drawable {
    public void draw(SpriteBatch spriteBatch, AbstractEntity entity);
}

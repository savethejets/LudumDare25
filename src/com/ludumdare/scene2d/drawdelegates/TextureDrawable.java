package com.ludumdare.scene2d.drawdelegates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ludumdare.entities.AbstractEntity;

public class TextureDrawable implements Drawable {

    Texture texture;

    public TextureDrawable(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, AbstractEntity entity) {
        spriteBatch.begin();
        spriteBatch.draw(texture, entity.getPosition().x, entity.getPosition().y);
        spriteBatch.end();
    }
}

package com.ludumdare.scene2d.drawdelegates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ludumdare.entities.AbstractEntity;
import com.ludumdare.entities.CompositeDrawable;
import com.ludumdare.entities.Particle;

public class ParticleDrawable implements Drawable {

    private Texture texture;

    public ParticleDrawable(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, AbstractEntity entity) {
        if (entity instanceof CompositeDrawable) {
            for (AbstractEntity entity1 : ((CompositeDrawable) entity).getChildren()) {
                Color color = spriteBatch.getColor();
                if (entity1 instanceof Particle) {
                    spriteBatch.begin();
                    spriteBatch.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, ((Particle) entity1).getAliveTime()));
                    spriteBatch.draw(texture, entity1.getPosition().x, entity1.getPosition().y);
                    spriteBatch.end();
                    spriteBatch.setColor(color);
                }
            }
        } else {
            throw new RuntimeException("Entity is not a composite drawable!");
        }
    }
}

package com.ludumdare.controller;

import com.badlogic.gdx.graphics.Texture;
import com.ludumdare.scene2d.drawdelegates.Drawable;

import java.util.HashMap;

public class TextureController {

    private HashMap<Class, Drawable> textureMap = new HashMap<Class, Drawable>();

    public Drawable getTexture(Class domainClass) {
        return textureMap.get(domainClass);
    }

    public void registerTexture(Drawable texture, Class clazz) {
        textureMap.put(clazz, texture);
    }

}

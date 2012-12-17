package com.ludumdare.scene2d.camera;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public class Camera {

    OrthographicCamera camera;
    boolean isAttached = true;

    public boolean isDirty;

    // for tweening.
    private Tween tween;

    public Camera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setPosition(float x, float y) {
        camera.position.set(x, y, 0.0f);

        flagAsDirty();
    }

    public void resetCamera() {
        setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2f);
        camera.zoom = 1f;
    }

    private void flagAsDirty() {
        isDirty = true;
    }

    public float getXPosition() {
        return camera.position.x;
    }

    public float getYPosition() {
        return camera.position.y;
    }

    public void update(boolean updateFrustrum) {
        camera.update(updateFrustrum);

        flagAsDirty();
    }

    public void update() {
        camera.update(true);

        flagAsDirty();
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean attached) {
        isAttached = attached;

        flagAsDirty();
    }

    public Matrix4 getCombined() {
        return camera.combined;
    }

}
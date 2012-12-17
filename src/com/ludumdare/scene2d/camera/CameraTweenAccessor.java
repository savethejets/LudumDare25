package com.ludumdare.scene2d.camera;

import aurelienribon.tweenengine.TweenAccessor;

public class CameraTweenAccessor implements TweenAccessor<Camera> {

    public static final int POSITION_XY = 1;

    @Override
    public int getValues(Camera target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_XY:
                returnValues[0] = target.camera.position.x;
                returnValues[1] = target.camera.position.y;
                return 2;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Camera target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_XY:
                target.camera.position.x = newValues[0];
                target.camera.position.y = newValues[1];
                break;
            default:
                assert false;
                break;
        }
    }
}

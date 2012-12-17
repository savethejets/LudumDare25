package com.ludumdare.controller;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare.entities.AbstractEntity;
import com.ludumdare.entities.Invader;
import com.ludumdare.util.Size;

import java.awt.*;

public class GameBoardInformation {
    private static GameBoardInformation ourInstance = new GameBoardInformation();

    private Size screenSize;
    private Invader.MoveDirection invaderMoveDirection;
    private Vector2 midScreenPoint;

    public static GameBoardInformation getInstance() {
        return ourInstance;
    }

    private GameBoardInformation() {
    }

    public Size getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Size screenSize) {
        this.screenSize = screenSize;
    }

    public AbstractEntity.MoveDirection getInvaderMoveDirection() {
        return invaderMoveDirection;
    }

    public void setInvaderMoveDirection(AbstractEntity.MoveDirection invaderMoveDirection) {
        this.invaderMoveDirection = invaderMoveDirection;
    }

    public Vector2 getMidScreenPoint() {
        return midScreenPoint;
    }

    public void setMidScreenPoint(Vector2 midScreenPoint) {
        this.midScreenPoint = midScreenPoint;
    }
}

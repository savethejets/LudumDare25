package com.ludumdare.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare.entities.*;
import com.ludumdare.game.Difficulty;
import com.ludumdare.util.Size;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameController implements Updateable {

    List<AbstractEntity> objects = new ArrayList<AbstractEntity>();
    private Music music;

    public enum GameState {
        CREATE,
        STARTED,
        GAME_OVER_WIN,
        GAME_OVER_LOSE;
    }

    private AbstractEntity.MoveDirection queuedDirection;


    private GameState gameState;
    private Difficulty difficulty;

    public GameController() {
        difficulty = new Difficulty();
        music = Gdx.audio.newMusic(Gdx.files.internal("song1.mp3"));
        create();
    }

    public Invader randomInvaderType() {
        Random random = new Random();
        int i = random.nextInt(3);
        switch (i) {
            case 0:
                return new InvaderOne();
            case 1:
                return new InvaderTwo();
            case 2:
                return new InvaderThree();
            default:
                return new InvaderOne();
        }
    }
    private void create() {

        gameState = GameState.CREATE;

        music.stop();
        music.play();

        float invaderBuffer = 36f;

        float spaceForInvaders = (Invader.SIZE * 3) + (invaderBuffer * 2);
        float spaceBetween = difficulty.getSpaceBetween();
        float spaceForShields = 20f;
        float spaceForShooter = 15f;

        float startingY = spaceForInvaders + spaceForShooter + spaceBetween + spaceForShields;

        for (int i = 0; i < difficulty.getNumberOfInvaders(); i++) {
            for (int j = 0; j < 6; j++) {

                Invader invader = randomInvaderType();

                invader.setPosition(new Vector2(j * (Invader.SIZE + invaderBuffer), (i * (Invader.SIZE + invaderBuffer)) + startingY));

                objects.add(invader);
            }
        }

        Shield shield1 = new Shield();
        shield1.setPosition(new Vector2(50, spaceForShooter + shield1.getHeight() / 2));
        objects.add(shield1);

        Shield shield2 = new Shield();
        shield2.setPosition(new Vector2(200, spaceForShooter + shield2.getHeight() / 2));
        objects.add(shield2);

        Shield shield3 = new Shield();
        shield3.setPosition(new Vector2(350, spaceForShooter + shield3.getHeight() / 2));
        objects.add(shield3);

        for (int i = 0; i < difficulty.getNumberOfShooters(); i++) {
            Shooter shooter = new Shooter();

            shooter.setPosition(new Vector2(5 * i, 0));

            objects.add(shooter);

            shooter.setHold(true);

            EntitySpawner.getInstance().spawn(new ImminentDangerDetector(shooter, Arrays.asList(shield1, shield2, shield3)));
        }

        Invader.VELOCITY = 60f;
    }

    @Override
    public void update(float delta) {

        if (queuedDirection != null) {
            setDirection(queuedDirection);
            queuedDirection = null;
        }

        EntitySpawner.getInstance().collectObjectsToSpawn(objects);

        cleanUpDestroyedObjects();

        for (AbstractEntity object : objects) {
            if (object instanceof Shooter && ((Shooter) object).shouldFindNewTarget()) {
                ((Shooter) object).findTarget(objects);
            }
        }

        for (AbstractEntity object : objects) {
            if (object instanceof Updateable) {
                ((Updateable) object).update(delta);
            }
        }

        for (AbstractEntity object : objects) {
            object.testCollision(objects);
        }

        if (isNumberOfOppositeInvadersEqualToTotalInvaders()) {
            GameBoardInformation information = GameBoardInformation.getInstance();

            information.setInvaderMoveDirection(information.getInvaderMoveDirection().getOpposite());

            for (AbstractEntity object : objects) {
                if (object instanceof Invader) {
                    ((Invader) object).setOpposite(false);
                }
            }
        }

        int invaderCount = 0;
        for (AbstractEntity object : objects) {
            if (object instanceof Invader) {
                invaderCount++;
                if (object.getPosition().y <= 70) {
                    gameWin();
                }
            }
        }

        if (invaderCount == 0) {
            gameLose();
        }
    }

    private void gameLose() {
        gameState = GameState.GAME_OVER_LOSE;

        DisplayNotifier.getInstance().notify(DisplayNotifier.DisplayEvent.GAME_OVER_LOSE);
    }

    private void gameWin() {
        for (AbstractEntity object : objects) {
            if (object instanceof Invader) {
                ((Invader) object).setHold(true);
            }
            if (object instanceof Shooter) {
                ((Shooter) object).setHold(true);
            }
        }

        DisplayNotifier.getInstance().notify(DisplayNotifier.DisplayEvent.GAME_OVER_WIN);

        // restart with Harder difficulty
        gameState = GameState.GAME_OVER_WIN;

    }

    private void setDirection(AbstractEntity.MoveDirection queuedDirection) {

        if (!areAnyInvadersMovingUpOrDown()) {
            for (AbstractEntity object : objects) {
                if (object instanceof Invader) {
                    ((Invader) object).setMoveDirection(queuedDirection);
                }
            }

            GameBoardInformation.getInstance().setInvaderMoveDirection(queuedDirection);
        }

    }

    private void cleanUpDestroyedObjects() {
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (objects.get(i).isCleanUp()) {
                objects.remove(i);
            }
        }
    }

    public boolean areAnyInvadersMovingUpOrDown() {
        for (AbstractEntity object : objects) {
            if (object instanceof Invader && ((Invader) object).isMovingUpOrDown()) {
                return true;
            }
        }
        return false;
    }

    public void onButtonShootPressed() {
        Invader aRandomInvader = findARandomInvader();

        if (aRandomInvader != null) {
            aRandomInvader.shoot();
        }
    }

    private Invader findARandomInvader() {
        // in this case get a random invader to shoot at.
        Random random = new Random();
        List<Invader> listOfInvaders = new ArrayList<Invader>();
        for (AbstractEntity entity : objects) {
            if (entity instanceof Invader) {
                listOfInvaders.add((Invader) entity);
            }
        }
        if (listOfInvaders.size() > 1) {
            int i = random.nextInt(listOfInvaders.size() - 1) - 1;
            if (i >= 0 && i < listOfInvaders.size()) {
                return listOfInvaders.get(i);
            }
        } else if (!listOfInvaders.isEmpty()) {
            return listOfInvaders.get(0);
        }
        return null;
    }

    public void onButtonIncreaseVelocityPressed() {
        Invader.VELOCITY = 80f;
        if (areInvadersPaused()) {
            onButtonStopPressed();
        }
    }

    public void onButtonDecreaseVelocityPressed() {
        Invader.VELOCITY = 45f;
        if (areInvadersPaused()) {
            onButtonStopPressed();
        }
    }

    public void moveAllInvadersRight() {
        if (!areAnyInvadersMovingUpOrDown()) {

            for (AbstractEntity object : objects) {
                if (object instanceof Invader) {
                    ((Invader) object).setMoveDirection(AbstractEntity.MoveDirection.RIGHT);
                }
            }

            GameBoardInformation.getInstance().setInvaderMoveDirection(AbstractEntity.MoveDirection.RIGHT);
        } else {
            queuedDirection = AbstractEntity.MoveDirection.RIGHT;
        }
    }

    public void onButtonStopPressed() {
        for (AbstractEntity object : objects) {
            if (object instanceof Invader) {
                ((Invader) object).setHold(!((Invader) object).isHold());
            }
        }
    }

    public boolean areInvadersPaused() {
        for (AbstractEntity object : objects) {
            if (object instanceof Invader) {
                if (((Invader) object).isHold()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNumberOfOppositeInvadersEqualToTotalInvaders() {
        int numberOfOppositeInvaders = 0;
        int numberOfInvaders = 0;

        for (AbstractEntity object : objects) {
            if (object instanceof Invader) {
                if (!((Invader) object).isDead()) {
                    numberOfInvaders++;
                }
                if (((Invader) object).isOpposite()) {
                    numberOfOppositeInvaders++;
                }
            }
        }
        return numberOfOppositeInvaders == numberOfInvaders;
    }

    public List<AbstractEntity> getObjects() {
        return objects;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isGameOver() {
        return GameState.GAME_OVER_LOSE.equals(gameState) || GameState.GAME_OVER_WIN.equals(gameState);
    }

    public boolean isGameStarted() {
        return GameState.STARTED.equals(gameState);
    }

    public void startGame() {
        gameState = GameState.STARTED;
        for (AbstractEntity object : objects) {
            if (object instanceof Shooter) {
                ((Shooter) object).setHold(false);
            }
        }
        moveAllInvadersRight();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void restart() {
        if (GameState.GAME_OVER_WIN.equals(gameState)) {
            difficulty.increaseDifficulty();
        } else {
            difficulty.reset();
        }

        objects.clear();
        create();

    }
}

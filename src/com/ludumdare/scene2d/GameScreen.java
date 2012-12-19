package com.ludumdare.scene2d;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;
import bloom.Bloom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ludumdare.LudumDareMain;
import com.ludumdare.controller.*;
import com.ludumdare.entities.*;
import com.ludumdare.scene2d.camera.Camera;
import com.ludumdare.scene2d.camera.CameraTweenAccessor;
import com.ludumdare.scene2d.drawdelegates.Drawable;
import com.ludumdare.scene2d.drawdelegates.ParticleDrawable;
import com.ludumdare.scene2d.drawdelegates.TextureDrawable;
import com.ludumdare.util.Size;

public class GameScreen extends AbstractScreen {

    private GameController gameController;
    private ShapeRenderer renderer;
    private Camera camera;
    private TweenManager tweenManager;

    private SpriteBatch spriteBatch;

    private Bloom bloom;

    private TextureController textureController;
    private SoundController soundController;

    Texture pressToStart = new Texture(Gdx.files.internal("press_key.png"));
    Texture youWin = new Texture(Gdx.files.internal("you_win.png"));
    Texture youLose = new Texture(Gdx.files.internal("you_lose.png"));

    public GameScreen(LudumDareMain game) {
        super(game);

        GameBoardInformation.getInstance().setScreenSize(new Size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()));

        gameController = new GameController();

        renderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(this);

        DisplayNotifier.getInstance().setGameScreen(this);

        camera = new Camera(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        camera.resetCamera();

        tweenManager = new TweenManager();
        Tween.registerAccessor(Camera.class, new CameraTweenAccessor());

        textureController = new TextureController();
        soundController = new SoundController();

        loadTextures();

        loadSounds();

        bloom = new Bloom();

    }

    private void loadSounds() {

        soundController.registerSound(SoundController.SoundId.INVADER_SHOOT, Gdx.audio.newSound(Gdx.files.internal("laser1.wav")));
        soundController.registerSound(SoundController.SoundId.INVADER_DESTROYED, Gdx.audio.newSound(Gdx.files.internal("ship_explode.wav")));
        soundController.registerSound(SoundController.SoundId.SHOOTER_SHOOT, Gdx.audio.newSound(Gdx.files.internal("laser2.wav")));
        soundController.registerSound(SoundController.SoundId.SHOOTER_DESTROYED, Gdx.audio.newSound(Gdx.files.internal("shooter_explode.wav")));

    }

    public void loadTextures() {
        spriteBatch = new SpriteBatch();

        Texture invader1 = new Texture(Gdx.files.internal("Invader1.png"));
        Texture invader2 = new Texture(Gdx.files.internal("Invader2.png"));
        Texture invader3 = new Texture(Gdx.files.internal("Invader3.png"));
        Texture blueBullet = new Texture(Gdx.files.internal("blue_bullet.png"));
        Texture yellowBullet = new Texture(Gdx.files.internal("yellow_bullet.png"));
        Texture shooter = new Texture(Gdx.files.internal("shooter.png"));
        Texture shield = new Texture(Gdx.files.internal("shield.png"));
        Texture particle = new Texture(Gdx.files.internal("particle.png"));

        textureController.registerTexture(new TextureDrawable(invader1), InvaderOne.class);
        textureController.registerTexture(new TextureDrawable(invader2), InvaderTwo.class);
        textureController.registerTexture(new TextureDrawable(invader3), InvaderThree.class);
        textureController.registerTexture(new TextureDrawable(blueBullet), ShooterBullet.class);
        textureController.registerTexture(new TextureDrawable(yellowBullet), InvaderBullet.class);
        textureController.registerTexture(new TextureDrawable(shooter), Shooter.class);
        textureController.registerTexture(new TextureDrawable(shield), Shield.class);
        textureController.registerTexture(new ParticleDrawable(particle), Explosion.class);
    }

    @Override
    public void render(float delta) {

        super.render(delta);

        gameController.update(delta);

        camera.update();

        tweenManager.update(delta);

        spriteBatch.setProjectionMatrix(camera.getCombined());
        bloom.capture();

        if (!gameController.isGameStarted()) {
            spriteBatch.begin();
            spriteBatch.draw(pressToStart, Gdx.graphics.getWidth()/2 - pressToStart.getWidth() - 30, 180);
            spriteBatch.end();
        }

        if (GameController.GameState.GAME_OVER_LOSE.equals(gameController.getGameState())) {
            spriteBatch.begin();
            spriteBatch.draw(youLose, Gdx.graphics.getWidth()/2 - pressToStart.getWidth() - 30, 240);
            spriteBatch.end();
        }

        if (GameController.GameState.GAME_OVER_WIN.equals(gameController.getGameState())) {
            spriteBatch.begin();
            spriteBatch.draw(youWin, Gdx.graphics.getWidth()/2 - pressToStart.getWidth() - 30, 240);
            spriteBatch.end();
        }

        renderer.setProjectionMatrix(camera.getCombined());
        for (AbstractEntity entity : gameController.getObjects()) {
            if (!(entity instanceof NotRenderable)) {
                Drawable drawable = textureController.getTexture(entity.getClass());
                if (drawable != null) {
                    drawable.draw(spriteBatch, entity);
                } else {
                    renderer.begin(ShapeRenderer.ShapeType.Rectangle);
                    renderer.rect(entity.getPosition().x, entity.getPosition().y, entity.getWidth(), entity.getHeight());
                    renderer.end();
                }
            }
        }

        bloom.render();

    }

    @Override
    public boolean keyDown(int keycode) {

        if (gameController.isGameStarted()) {

            if (Input.Keys.UP == keycode) {
                gameController.onButtonIncreaseVelocityPressed();
            }

            if (Input.Keys.DOWN == keycode) {
                gameController.onButtonDecreaseVelocityPressed();
            }

            if (Input.Keys.LEFT == keycode
                    || Input.Keys.RIGHT == keycode
                    || Input.Keys.SHIFT_RIGHT == keycode
                    || Input.Keys.SHIFT_LEFT == keycode) {
                gameController.onButtonStopPressed();
            }

            if (Input.Keys.SPACE == keycode) {
                gameController.onButtonShootPressed();
            }
        }

        if (GameController.GameState.CREATE.equals(gameController.getGameState())) {
            gameController.startGame();
        }

        if (gameController.isGameOver()) {
            gameController.restart();
        }

        return true;
    }

    public void notifyOfDislayEventRequest(DisplayNotifier.DisplayEvent displayEvent, Object... objects) {
        if (DisplayNotifier.DisplayEvent.SHAKE_CAMERA.equals(displayEvent)) {
            Tween
                    .to(camera, CameraTweenAccessor.POSITION_XY, 0.1f)
                    .target(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 20)
                    .repeat(2, 0f).ease(Elastic.INOUT).start(tweenManager).setCallback(new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> baseTween) {
                    camera.resetCamera();
                }
            });
        }

        if (DisplayNotifier.DisplayEvent.PLAY_SOUND.equals(displayEvent)) {
            soundController.play((SoundController.SoundId) objects[0]);
        }
    }
}

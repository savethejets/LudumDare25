package com.ludumdare.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ludumdare.Constants;
import com.ludumdare.LudumDareMain;
import com.ludumdare.controller.DisplayNotifier;

public abstract class AbstractScreen extends InputAdapter implements Screen {

    protected final LudumDareMain game;
    protected final BitmapFont font;
    protected final SpriteBatch batch;

    public AbstractScreen(LudumDareMain game) {
        this.game = game;
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    // Screen implementation

    @Override
    public void show() {
        Gdx.app.log(Constants.LOG, "Showing screen: " + getName());
    }

    @Override
    public void resize(int width,int height) {
        Gdx.app.log(Constants.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);

    }

    @Override
    public void render(float delta) {
        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


    }

    @Override
    public void hide() {
        Gdx.app.log(Constants.LOG, "Hiding screen: " + getName());
    }

    @Override
    public void pause() {
        Gdx.app.log(Constants.LOG, "Pausing screen: " + getName());
    }

    @Override
    public void resume() {
        Gdx.app.log(Constants.LOG, "Resuming screen: " + getName());
    }

    @Override
    public void dispose() {
        Gdx.app.log(Constants.LOG, "Disposing screen: " + getName());

        // dispose the collaborators
        batch.dispose();
        font.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);    
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);    
    }

    @Override
    public boolean keyTyped(char character) {
        return super.keyTyped(character);    
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return super.touchDown(x, y, pointer, button);    
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return super.touchUp(x, y, pointer, button);    
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return super.touchDragged(x, y, pointer);    
    }

    @Override
    public boolean touchMoved(int x, int y) {
        return super.touchMoved(x, y);    
    }

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount);    
    }


}
package com.ludumdare.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.ludumdare.LudumDareMain;

public class SplashScreen extends AbstractScreen {

    private Texture codeHerdLabel;
    protected SpriteBatch stage;

    public SplashScreen(LudumDareMain game) {
        super(game);
        this.stage = new SpriteBatch();
    }

    @Override
    public void show() {
        super.show();

        // load the texture with the splash image
        codeHerdLabel = new Texture("title.png");

        // set the linear texture filter to improve the image stretching
        codeHerdLabel.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        TextureRegion splashRegion = new TextureRegion(codeHerdLabel);

        Image splashImage = new Image(splashRegion, Scaling.stretch, Align.BOTTOM | Align.LEFT);
        splashImage.width = width;
        splashImage.height = height;

        splashImage.color.a = 0f;

        Sequence actions = Sequence.$(FadeIn.$(0.75f), Delay.$(FadeOut.$(0.75f), 1.75f));
        actions.setCompletionListener(new OnActionCompleted() {
            @Override
            public void completed(Action action) {
                game.setScreen(new GameScreen(game));
            }
        });
        splashImage.action(actions);
    }

    @Override
    public void dispose() {
        super.dispose();
        codeHerdLabel.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.begin();
        stage.draw(codeHerdLabel, 0, 0);
        stage.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        game.setScreen(new GameScreen(game));
        return true;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.TitleScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Leikr;
import com.leikr.core.GroovySystemMethods;

/**
 *
 * @author tor
 */
public class TitleScreen extends Controllers implements InputProcessor, Screen {

    Leikr game;
    Texture font;
    SpriteBatch batch;
    Camera camera;
    Viewport viewport;

    Texture animTexture;
    TextureRegion[] animationFrames;
    Animation animation;
    float elapsedTime;

    float blink;

    int halfX;
    int halfY;

    GroovySystemMethods groovySystemMethods = new GroovySystemMethods();

    public TitleScreen(Leikr game) {
        this.game = game;
        batch = game.batch;

        if (!Gdx.files.external("LeikrVirtualDrive/").exists() || !Gdx.files.external("LeikrVirtualDrive/ChipSpace/").exists()) {
            groovySystemMethods.initFileSystem();
        }

        animTexture = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/TitleAnimation/TitleAnimation.png"));
        TextureRegion[][] tmpFrames = TextureRegion.split(animTexture, 64, 24);

        animationFrames = new TextureRegion[27];
        int index = 0;
        for (int j = 0; j < 27; j++) {
            animationFrames[index] = tmpFrames[j][0];
            index++;
        }

        animation = new Animation(1f / 27f, animationFrames);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        font = new Texture("LeikrFontA.png");
        Pixmap pm = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/HideCursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();

        halfX = (int) (Leikr.WIDTH / 2);
        halfY = (int) (Leikr.HEIGHT / 2);

        blink = 0;

        Gdx.input.setInputProcessor(this);
    }

    void BeginLeikr() {
        game.setScreen(new ConsoleScreen(game));
        this.dispose();
    }

    public void drawText(String text, int x, int y) {
        int fontX;
        int fontY;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        batch.begin();
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * 8;
            fontY = ((int) C / 16) * 8;
            batch.draw(font, x, y, fontX, fontY, 8, 8);
            x = x + 8;
        }
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        BeginLeikr();
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        BeginLeikr();
        return false;
    }

    public boolean buttonUp(Controller controller, int buttonCode) {
        BeginLeikr();
        return true;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);

        batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, false), halfX - 64, halfY - 32, 64 * 2, 24 * 2);

        batch.end();

        if (animation.isAnimationFinished(elapsedTime)) {
            batch.begin();
            if (blink > 0.4) {
                batch.draw(font, 232, 8, 0, 0, 8, 8);
                blink += delta;
                if (blink > 1) {
                    blink = 0;
                }
            } else {
                blink += delta;
            }
            batch.end();
            drawText("Press button to start...", (halfX / 2) - 20, 8);

        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}

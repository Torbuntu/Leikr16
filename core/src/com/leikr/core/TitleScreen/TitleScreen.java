/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import com.leikr.core.DesktopEnvironment.DesktopEnvironmentScreen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class TitleScreen extends Controllers implements InputProcessor, Screen {

    Leikr game;
    Texture font;
    int glyphWidth;
    int glyphHeight;

    SpriteBatch batch;
    Camera camera;
    Viewport viewport;

    Texture animTexture;
    TextureRegion[] animationFrames;
    Animation animation;
    float elapsedTime;

    TextureRegion[][] tmpFrames;

    float blink;
    String introText = "Press button to start...";
    int halfX;
    int halfY;
    int len;

    public TitleScreen(Leikr game) {
        this.game = game;
        batch = game.batch;

        animTexture = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/TitleAnimation/TitleAnimation.png"));
        tmpFrames = TextureRegion.split(animTexture, 64, 24);

        animationFrames = new TextureRegion[27];
        int index = 0;
        for (int j = 0; j < 27; j++) {
            animationFrames[index] = tmpFrames[j][0];
            index++;
        }
        float tmp = 1f / 27f;
        animation = new Animation(tmp, (Object[]) animationFrames);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/" + game.customSettings.fontName));
        Pixmap pm = new Pixmap(new FileHandle(Leikr.ROOT_PATH + "OS/HideCursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();

        halfX = (int) (Leikr.WIDTH / 2);
        halfY = (int) (Leikr.HEIGHT / 2);

        glyphWidth = (int) game.customSettings.glyphWidth;
        glyphHeight = (int) game.customSettings.glyphHeight;
        len = ((halfX / 2) - 20) + (introText.length() * glyphWidth);

        blink = 0;

        Gdx.input.setInputProcessor(this);
    }

    void BeginLeikr() {
        if (game.customSettings.startx) {
            game.setScreen(new DesktopEnvironmentScreen(game));
        } else {
            game.setScreen(new ConsoleScreen(game));

        }
        this.dispose();
    }

    public void drawText(String text, int x, int y) {
        int fontX;
        int fontY;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        batch.begin();
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * glyphWidth;
            fontY = ((int) C / 16) * glyphHeight;
            batch.draw(font, x, y, fontX, fontY, glyphWidth, glyphHeight);
            x = x + glyphWidth;
        }
        batch.end();
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
            drawText(introText, (halfX / 2) - 20, glyphHeight);
            batch.begin();
            if (blink > 0.4) {
                batch.draw(font, len, glyphHeight, 0, 0, glyphWidth, glyphHeight);
                blink += delta;
                if (blink > 1) {
                    blink = 0;
                }
            } else {
                blink += delta;
            }
            batch.end();

        }

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

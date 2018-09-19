/*
 * Copyright 2018 torbuntu
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
package com.leikr.core.DesktopEnvironment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class LeikrDesktopEngine {

    Texture appMenuTexture;

    ImageButton appMenuButton;
    Drawable appMenuDrawable;

    Stage stage;
    Camera camera;
    ShapeRenderer renderer;
    private Texture cursor;
    private Vector2 cursorCoords;
    private int cursorX;
    private int cursorY;

    public Stage appStage;
    private boolean renderAppMenu = false;

    private Texture consoleTexture;
    private TextureRegionDrawable consoleDrawable;
    private ImageButton consoleButton;

    public FitViewport viewport;

    public void preCreate(Leikr game) {
        cursorCoords = new Vector2();

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        stage = new Stage(viewport);
        appStage = new Stage(viewport);

        consoleTexture = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/Console.png"));
        consoleDrawable = new TextureRegionDrawable(new TextureRegion(consoleTexture));
        consoleButton = new ImageButton(consoleDrawable);
        consoleButton.setPosition(100, 100);
        consoleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ConsoleScreen(game));
                dispose();
                System.out.println("App Menu clicked!");
            }
        });
        appStage.addActor(consoleButton);

        camera = stage.getViewport().getCamera();

        // default background color
        renderer = new ShapeRenderer();

        // Hide system cursor and setup custom
        Pixmap pm = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/HideCursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();
        cursor = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/Cursor.png"));

        // input processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(appStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void create() {
    }

    public void preRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void render() {
    }

    public void renderStageAndCursor() {

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (renderAppMenu) {

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.setProjectionMatrix(camera.combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0, 0, 0, 0.5f);
            renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
            renderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            appStage.act(Gdx.graphics.getDeltaTime());
            appStage.draw();
        }

        //draw custom cursor
        stage.getBatch().begin();
        stage.getViewport().unproject(cursorCoords.set(Gdx.input.getX(), Gdx.input.getY()));
        cursorX = (int) (cursorCoords.x);
        cursorY = (int) (cursorCoords.y - 8);
        stage.getBatch().draw(cursor, cursorX, cursorY);
        stage.getBatch().end();
    }

    public void solidBg(float r, float g, float b) {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(r, g, b, 1);
        renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
        renderer.end();
    }

    public void setAppMenu(float x, float y, String fileName) {
        // App menu icon
        appMenuTexture = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/" + fileName));
        appMenuDrawable = new TextureRegionDrawable(new TextureRegion(appMenuTexture));
        appMenuButton = new ImageButton(appMenuDrawable);
        appMenuButton.setPosition(x, y);
        appMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                renderAppMenu = !renderAppMenu; // flips the menu
                System.out.println("App Menu clicked!");
            }
        });
        stage.addActor(appMenuButton);
    }

    public void update(int x, int y) {
        stage.getViewport().update(x, y, true);
    }

    public void dispose() {
        stage.dispose();
        appStage.dispose();
        renderer.dispose();
        consoleTexture.dispose();
        appMenuTexture.dispose();
    }

}

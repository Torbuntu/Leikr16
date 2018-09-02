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
package com.leikr.core.DesktopEnvironment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class DesktopEnvironment {

    private final Leikr game;
    Texture appMenuTexture;

    ImageButton appMenuButton;
    Drawable appMenuDrawable;

    Stage stage;
    Camera camera;
    ShapeRenderer renderer;
    private final Texture cursor;
    private Vector2 cursorCoords;
    private int cursorX;
    private int cursorY;

    DesktopEnvironment(Leikr game) {
        this.game = game;

        // App menu icon
        appMenuTexture = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/" + game.customSettings.appMenuIcon));
        appMenuDrawable = new TextureRegionDrawable(new TextureRegion(appMenuTexture));
        appMenuButton = new ImageButton(appMenuDrawable);

        appMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("App Menu clicked!");
            }
        });

        cursorCoords = new Vector2();

        stage = new Stage(new FitViewport(Leikr.WIDTH, Leikr.HEIGHT));
        stage.addActor(appMenuButton);

        camera = stage.getViewport().getCamera();

        // default background color
        renderer = new ShapeRenderer();

        // Hide system cursor and setup custom
        Pixmap pm = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/HideCursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();
        cursor = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/Cursor.png"));

        Gdx.input.setInputProcessor(stage);
    }

    public void renderDesktopEnvironment() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(.3f, .3f, .3f, 1);
        renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
        renderer.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        //draw custom cursor
        stage.getBatch().begin();
        stage.getViewport().unproject(cursorCoords.set(Gdx.input.getX(), Gdx.input.getY()));
        cursorX = (int) (cursorCoords.x);
        cursorY = (int) (cursorCoords.y - 8);
        stage.getBatch().draw(cursor, cursorX, cursorY);
        stage.getBatch().end();
    }

    public void updateDesktopEnvironment(int x, int y) {
        stage.getViewport().update(x, y, true);
    }

    public void DisposeDesktopEnvironment() {
    }

}

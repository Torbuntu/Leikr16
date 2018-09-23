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
package com.leikr.core.SpriteEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Leikr;
import static com.leikr.core.Leikr.gameName;

/**
 *
 * @author tor TODO: Rework this whole file once it is 'feature complete '
 */
final class SpriteEditor implements InputProcessor {

    Leikr game;
    SpriteEditorScreen sriteEditorScreen;
    Viewport viewport;
    Camera camera;
    ShapeRenderer renderer;
    PaintBrush paintBrush;
    Texture font;
    Texture cursor;

    SpriteBatch batch;
    Pixmap pixmap;
    Pixmap zoomPixmap;

    Texture spriteSheet;
    Texture zoomSpriteSheet;

    String firstSpriteSheet;
    String secondSpriteSheet;
    String thirdSpriteSheet;
    String fourthSpriteSheet;

    String selectedSpriteSheet;

    Color drawColor;

    Vector2 coords;
    Vector2 cursorCoords;
    int graphicsY;

    int spriteId = 0;
    int spriteIdX = 0;
    int spriteIdY = 0;

    int mainBoxWidth;
    int mainBoxHeight;

    int zoomBoxWidth;
    int zoomBoxHeight;

    int spriteWidth = 8;
    int spriteHeight = 8;

    int actualX;
    int actualY;

    int zoomX;
    int zoomY;
    int count = 0;
    int color = 0;

    boolean exitDialog = false;
    SpriteEditorButtons speButtons;

    public SpriteEditor(Leikr game, SpriteEditorScreen speScreen) {
        this.game = game;
        batch = game.batch;
        sriteEditorScreen = speScreen;
        drawColor = Color.BLACK;
        coords = new Vector2();
        cursorCoords = new Vector2();
        graphicsY = 0;
        font = new Texture("LeikrFontA.png");
        cursor = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/Cursor.png"));

        renderer = new ShapeRenderer();
        paintBrush = new PaintBrush(renderer, game);

        // gets the sprite sheet from the given fileName (which is the loaded game)
        firstSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/" + gameName + "_0.png";
        secondSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/" + gameName + "_1.png";
        thirdSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/" + gameName + "_2.png";
        fourthSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/" + gameName + "_3.png";

        selectedSpriteSheet = firstSpriteSheet;
        setSelectedSpriteSheet(selectedSpriteSheet);

        actualX = 0;
        actualY = 0;
        zoomX = 0;
        zoomY = 0;

        mainBoxWidth = spriteSheet.getWidth() + 2;
        mainBoxHeight = spriteSheet.getHeight() + 2;

        zoomBoxWidth = 128;
        zoomBoxHeight = 128;

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();

        speButtons = new SpriteEditorButtons(viewport, this, paintBrush);

        Pixmap pm = new Pixmap(Gdx.files.internal("HideCursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        // input processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(speButtons.stage);
        inputMultiplexer.addProcessor(speButtons.confirmExitStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void setSelectedSpriteSheet(String selected) {

        selectedSpriteSheet = selected;
        pixmap = new Pixmap(new FileHandle(selectedSpriteSheet));
        pixmap.setBlending(Pixmap.Blending.None);
        spriteSheet = new Texture(pixmap);

        zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        zoomPixmap.setBlending(Pixmap.Blending.None);
        zoomSpriteSheet = new Texture(zoomPixmap);

        zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, 8, 8, 0, 0, 8, 8);
        zoomSpriteSheet.draw(zoomPixmap, 0, 0);
    }

    public void renderSpriteEditor(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //background color
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(.1f, .2f, .2f, 1);
        renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
        renderer.end();

        batch.setProjectionMatrix(camera.combined);

        // Sprite sheet background black
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 1);
        renderer.rect(8, 8, mainBoxWidth - 2, mainBoxHeight - 2);
        renderer.rect(spriteSheet.getWidth() + 12, 8, zoomBoxWidth, zoomBoxHeight);
        renderer.end();

        // Outlines
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(7, 7, mainBoxWidth, mainBoxHeight);
        renderer.rect(spriteSheet.getWidth() + 11, 7, zoomBoxWidth + 2, zoomBoxHeight + 2);
        renderer.end();

        batch.begin();
        batch.setColor(Color.WHITE);

        //main sprite sheet
        if (spriteSheet != null) {
            batch.draw(spriteSheet, 8, 8);
        }
        if (zoomSpriteSheet != null) {
            batch.draw(zoomSpriteSheet, spriteSheet.getWidth() + 12, 8, zoomBoxWidth, zoomBoxHeight);
        }

        drawText();

        batch.end();

        // draw gui buttons
        speButtons.stage.act(Gdx.graphics.getDeltaTime());
        speButtons.stage.draw();

        if (exitDialog) {
            //background gray
            renderer.setProjectionMatrix(camera.combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0, 0, 0, 1);
            renderer.rect(viewport.getWorldWidth() / 2 - 100, viewport.getWorldHeight() / 2 + 12, 224, 60);
            renderer.end();
            batch.begin();
            drawExitText();
            batch.end();
            speButtons.confirmExitStage.act(Gdx.graphics.getDeltaTime());
            speButtons.confirmExitStage.draw();
        }

        // Draw custom cursor
        batch.begin();
        viewport.unproject(cursorCoords.set(Gdx.input.getX(), Gdx.input.getY()));
//        int tmpY = (int) (camera.viewportHeight - (cursorCoords.y));

        batch.draw(cursor, cursorCoords.x, cursorCoords.y - 8);
        batch.end();
    }

    public void drawExitText() {
        int fontX;
        int fontY;
        int x = (int) (viewport.getWorldWidth() / 2 - 98);
        int y = (int) (viewport.getWorldHeight() / 2 + 40);
        // Set the variable test for evaluating the x and y position of the ASCII set.
        String text = "Would you like exit (y/n)?";
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * 8;
            fontY = ((int) C / 16) * 8;
            batch.draw(font, x, y, fontX, fontY, 8, 8);
            x = x + 8;
        }
    }

    public void drawText() {
        int fontX;
        int fontY;
        int x = 8;
        int y = spriteSheet.getHeight() + 12;
        int sheetOffset = 0;
        if (selectedSpriteSheet.contains("_0")) {
            sheetOffset = 0 + spriteId;
        } else if (selectedSpriteSheet.contains("_1")) {
            sheetOffset = 256 + spriteId;
        } else if (selectedSpriteSheet.contains("_2")) {
            sheetOffset = 512 + spriteId;
        } else if (selectedSpriteSheet.contains("_3")) {
            sheetOffset = 768 + spriteId;
        }
        // Set the variable test for evaluating the x and y position of the ASCII set.

        String text = "Sprite ID: " + sheetOffset;
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * 8;
            fontY = ((int) C / 16) * 8;
            batch.draw(font, x, y, fontX, fontY, 8, 8);
            x = x + 8;
        }
    }

    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
    }

    public void disposeSpriteEditor() {
        renderer.dispose();
        game.batch.dispose();
        spriteSheet.dispose();
        zoomSpriteSheet.dispose();
        pixmap.dispose();
        zoomPixmap.dispose();
        font.dispose();
        cursor.dispose();
        speButtons.disposeButtons();
    }

    public void savePixmapImage() {
        PixmapIO.writePNG(new FileHandle(selectedSpriteSheet), pixmap);
    }

    public void undoRecentEdits() {
        pixmap = new Pixmap(new FileHandle(selectedSpriteSheet));
        pixmap.setBlending(Pixmap.Blending.None);
        spriteSheet = new Texture(pixmap);

        zoomPixmap = new Pixmap(spriteWidth, spriteHeight, Pixmap.Format.RGBA8888);
        zoomPixmap.setBlending(Pixmap.Blending.None);
        zoomSpriteSheet = new Texture(zoomPixmap);
        zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, spriteWidth, spriteHeight, 0, 0, spriteWidth, spriteHeight);
        zoomSpriteSheet.draw(zoomPixmap, 0, 0);
    }

    public void drawSelectedPixmapToMain() {
        if (actualX > 129) {

            zoomPixmap.setColor(drawColor);
            zoomPixmap.drawPixel(zoomX, zoomY);
            zoomSpriteSheet.draw(zoomPixmap, 0, 0);

            pixmap.drawPixmap(zoomPixmap, spriteIdX * 8, spriteIdY * 8, 0, 0, spriteWidth, spriteHeight);

            spriteSheet.draw(pixmap, 0, 0);
        } else {

            pixmap.setColor(drawColor);
            pixmap.drawPixel(actualX, actualY);
            spriteSheet.draw(pixmap, 0, 0);

            zoomPixmap = new Pixmap(spriteWidth, spriteHeight, Pixmap.Format.RGBA8888);
            zoomPixmap.setBlending(Pixmap.Blending.None);
            zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, spriteWidth, spriteHeight, 0, 0, spriteWidth, spriteHeight);            
            zoomSpriteSheet.draw(zoomPixmap, 0, 0);
        }

    }

    public void setDrawingCoords() {
        graphicsY = (int) (camera.viewportHeight - (coords.y));

        actualX = (int) (coords.x - 9);
        actualY = (int) (graphicsY - 104);

        if (spriteWidth == 16) {
            zoomX = (actualX / 8) - 16;
            zoomY = (actualY / 8);
        } else {
            zoomX = (actualX / 16) - 8;
            zoomY = (actualY / 16);
        }

    }

    public void drawPixelsOnTouch(int button) {

        switch (button) {
            case 0:
                drawSelectedPixmapToMain();
                break;
            case 2:
                spriteIdX = (actualX) / 8;
                spriteIdY = (actualY) / 8;
                spriteId = ((spriteIdY * 16) + (spriteIdX));

                zoomPixmap = new Pixmap(spriteWidth, spriteHeight, Pixmap.Format.RGBA8888);
                zoomPixmap.setBlending(Pixmap.Blending.None);
                zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, spriteWidth, spriteHeight, 0, 0, spriteWidth, spriteHeight);                
                zoomSpriteSheet.draw(zoomPixmap, 0, 0);
                break;
        }

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(coords.set(screenX, screenY));
        setDrawingCoords();
        drawPixelsOnTouch(button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        viewport.unproject(coords.set(screenX, screenY));
        setDrawingCoords();
        drawSelectedPixmapToMain();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (exitDialog) {
                savePixmapImage();
                game.setScreen(new ConsoleScreen(game));
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }

            exitDialog = true;
            return true;
        }
        if (exitDialog) {
            switch (keycode) {
                case Keys.N:
                    exitDialog = false;
                    return true;
                case Keys.Y:
                    savePixmapImage();
                    game.setScreen(new ConsoleScreen(game));
                    Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
                    return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
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
}

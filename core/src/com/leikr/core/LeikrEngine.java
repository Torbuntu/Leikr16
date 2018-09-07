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
package com.leikr.core;

import com.leikr.core.Graphics.LeikrPalette;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Graphics.SpriteHandler;
import com.leikr.core.SoundEngine.SoundEngine;
import java.util.Random;
import static com.leikr.core.Leikr.fileName;

/**
 *
 * @author tor
 */
public class LeikrEngine implements InputProcessor, ControllerListener {

    public static Leikr game;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Camera camera;
    Viewport viewport;
    Texture font;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    TiledMapTileLayer tiledMapLayer;

    boolean useMap;

    //keyboard buttons
    public boolean rightKeyPressed = false;
    public boolean leftKeyPressed = false;
    public boolean upKeyPressed = false;
    public boolean downKeyPressed = false;
    public boolean zKeyPressed = false;
    public boolean xKeyPressed = false;
    public boolean spaceKeyPressed = false;

    //controller buttons
    boolean buttonAisPressed = false;
    boolean buttonBisPressed = false;
    boolean buttonXisPressed = false;
    boolean buttonYisPressed = false;
    boolean bumperLeftPressed = false;
    boolean bumperRightPressed = false;

    //d-pad buttons
    boolean leftButtonPressed = false;
    boolean rightButtonPressed = false;
    boolean upButtonPressed = false;
    boolean downButtonPressed = false;

    public int screenWidth = (int) Leikr.WIDTH;
    public int screenHeight = (int) Leikr.HEIGHT;

    LeikrPalette leikrPalette;
    SpriteHandler spriteHandler;
    PaintBrush paintBrush;
    SoundEngine soundEngine;

    int fontWidth;
    int fontHeight;

    void preCreate() {
        game = LeikrGameScreen.game;
        batch = game.batch;
        shapeRenderer = new ShapeRenderer();

        spriteHandler = new SpriteHandler(game);
        paintBrush = new PaintBrush(shapeRenderer, game);
        leikrPalette = new LeikrPalette();
        soundEngine = new SoundEngine(game);

        viewport = new FitViewport(screenWidth, screenHeight);
        camera = viewport.getCamera();
        
        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/" + game.customSettings.fontName));
        fontWidth = (int) game.customSettings.glyphWidth;
        fontHeight = (int) game.customSettings.glyphHeight;
        Controllers.addListener(this);
    }

    public void create() {

    }

    public void preRender() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void renderCamera() {
        //camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        camera.update();
    }

    // Render methods used by the game scripts. 
    public void render() {
    }

    public void render(float delta) {
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    public void setFont(String fontName, int width, int height) {
        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "ChipSpace/" + fileName + "/" + fontName + ".png"));
        fontWidth = width;
        fontHeight = height;
    }

    // Map and Camera section
    public void loadMap() {
        useMap = true;
        tiledMap = new TmxMapLoader().load(Leikr.ROOT_PATH + "ChipSpace/" + fileName + "/" + fileName + ".tmx");
        tiledMapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);
    }

    public void drawMap() {
        if (tiledMapRenderer != null && useMap) {
            tiledMapRenderer.setView((OrthographicCamera) camera);
            tiledMapRenderer.render();
//            TextureRegion mpl = tiledMap.getTileSets().getTileSet("tileset").getTile(0).getTextureRegion();
        }
    }

    public void setMapSection(int row, int column) {
        //row is the row id, column is the column id.
        int width = row * 320;
        int height = column * 240;
        camera.position.x = width + 160;
        camera.position.y = height + 120;
    }

    public int getCameraX() {
        return (int) camera.position.x;
    }

    public int getCameraY() {
        return (int) camera.position.y;
    }

    public void setCamera(float x, float y) {
        camera.position.set(x, y, 0);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getMapWidth() {
        return tiledMap.getProperties().get("width", Integer.class);
    }

    public int getMapHeight() {
        return tiledMap.getProperties().get("height", Integer.class);
    }

    public int getCellTileId(float x, float y) {
//        System.out.println("X: " + x + " Y: " + y);
        try {
            if (x >= 0 && y >= 0 && x <= tiledMap.getProperties().get("width", Integer.class) - 1 && y <= tiledMap.getProperties().get("height", Integer.class) - 1) {
                return tiledMapLayer.getCell(Math.round(x), Math.round(y)).getTile().getId();
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public void setCellTile(float x, float y, int newId) {
        if (tiledMapLayer.getCell((int) x, (int) y) != null) {
            tiledMapLayer.getCell((int) x, (int) y).setTile(tiledMap.getTileSets().getTile(newId));
        } else {
            TiledMapTileLayer.Cell newCell = new Cell();
            tiledMapLayer.setCell(Math.round(x), Math.round(y), newCell.setTile(tiledMap.getTileSets().getTile(newId)));
        }
    }

    public int getRandom(int range) {
        return new Random().nextInt(range);
    }

    // Drawing section
    public void drawText(String text, float x, float y, int color) {
        int fontX;
        int fontY;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        batch.begin();
        int id = leikrPalette.palette.get(color);
        batch.setColor(new Color(id));

        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * fontWidth;
            fontY = ((int) C / 16) * fontHeight;
            batch.draw(font, x, y, fontX, fontY, fontWidth, fontHeight);
            x = x + 8;
        }
        batch.end();
    }
    
    public void sprite(int id, float x, float y) {
        spriteHandler.drawSprite(id, x, y);
    }

    public void sprite(int id, float x, float y, float scaleX, float scaleY) {
        spriteHandler.drawSprite(id, x, y, scaleX, scaleY);
    }

    public void bigSprite(int idtl, int idtr, int idbl, int idbr, float x, float y) {
        spriteHandler.drawBigSprite(idtl, idtr, idbl, idbr, x, y);
    }

    public void rect(float x, float y, float width, float height, int color, String type) {
        paintBrush.drawRect(x, y, width, height, color, type);
    }

    public void circle(float x, float y, float radius, int color, String type) {
        paintBrush.drawCircle(x, y, radius, color, type);
    }

    public void arc(float x, float y, float radius, float start, float degrees, int color, String type) {
        paintBrush.drawArc(x, y, radius, start, degrees, color, type);
    }

    public void line(float x, float y, float x2, float y2, int color) {
        paintBrush.drawLine(x, y, x2, y2, color);
    }

    public void color(int id, float x, float y) {
        paintBrush.drawColor(id, x, y);
    }

    public void color(int id, float x, float y, float width, float height) {
        paintBrush.drawColor(id, x, y, width, height);
    }

    public void drawPalette(float x, float y, float w, float h) {
        shapeRenderer.begin(ShapeType.Filled);
        for (int c : leikrPalette.palette) {
            shapeRenderer.setColor(new Color(c));
            shapeRenderer.rect(x, y, w, h);
            x += w;
        }
        shapeRenderer.end();
    }

    // Sound Section
    public void playBeep(float dur, float freq, float amp, String oscType) {
        new Thread() {
            @Override
            public void run() {
                soundEngine.soundEnginePlayBeep(dur, freq, amp, oscType);
            }
        }.start();
    }

    // disposals
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }

    public boolean key(String name) {
        switch (name.toLowerCase()) {
            case "right":
                return rightKeyPressed;
            case "left":
                return leftKeyPressed;
            case "up":
                return upKeyPressed;
            case "down":
                return downKeyPressed;
            case "z":
                return zKeyPressed;
            case "x":
                return xKeyPressed;
            case "space":
                return spaceKeyPressed;
            default:
                return false;
        }
    }

    public boolean button(String name) {
        switch (name.toLowerCase()) {
            case "a":
                return buttonAisPressed;
            case "b":
                return buttonBisPressed;
            case "x":
                return buttonXisPressed;
            case "y":
                return buttonYisPressed;
            case "bumper_left":
                return bumperLeftPressed;
            case "bumper_right":
                return bumperRightPressed;
            case "left":
                return leftButtonPressed;
            case "right":
                return rightButtonPressed;
            case "up":
                return upButtonPressed;
            case "down":
                return downButtonPressed;
            default:
                return false;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.RIGHT:
                rightKeyPressed = true;
                return true;
            case Keys.LEFT:
                leftKeyPressed = true;
                return true;
            case Keys.UP:
                upKeyPressed = true;
                return true;
            case Keys.DOWN:
                downKeyPressed = true;
                return true;
            case Keys.Z:
                zKeyPressed = true;
                return true;
            case Keys.X:
                xKeyPressed = true;
                return true;
            case Keys.SPACE:
                spaceKeyPressed = true;
                return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.ESCAPE) {
            game.setScreen(new ConsoleScreen(game));
            this.dispose();
            return true;
        }
        switch (keycode) {
            case Keys.RIGHT:
                rightKeyPressed = false;
                return true;
            case Keys.LEFT:
                leftKeyPressed = false;
                return true;
            case Keys.UP:
                upKeyPressed = false;
                return true;
            case Keys.DOWN:
                downKeyPressed = false;
                return true;
            case Keys.Z:
                zKeyPressed = false;
                return true;
            case Keys.X:
                xKeyPressed = false;
                return true;
            case Keys.SPACE:
                spaceKeyPressed = false;
                return true;
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        // This is the dpad
        switch (value) {
            case north:
                upButtonPressed = true;
                break;
            case south:
                downButtonPressed = true;
                break;
            case east:
                rightButtonPressed = true;
                break;
            case west:
                leftButtonPressed = true;
                break;
            case center:
                upButtonPressed = false;
                downButtonPressed = false;
                rightButtonPressed = false;
                leftButtonPressed = false;
                break;
        }
        return true;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        switch (buttonCode) {
            case 0:
                buttonXisPressed = true;
                return true;
            case 1:
                buttonAisPressed = true;
                return true;
            case 2:
                buttonBisPressed = true;
                return true;
            case 3:
                buttonYisPressed = true;
                return true;
            case 4:
                bumperLeftPressed = true;
                return true;
            case 5:
                bumperRightPressed = true;
                return true;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        switch (buttonCode) {
            case 0:
                buttonXisPressed = false;
                return true;
            case 1:
                buttonAisPressed = false;
                return true;
            case 2:
                buttonBisPressed = false;
                return true;
            case 3:
                buttonYisPressed = false;
                return true;
            case 4:
                bumperLeftPressed = false;
                return true;
            case 5:
                bumperRightPressed = false;
                return true;
        }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        //axis 0 = x axis -1 = left 1 = right
        //axis 1 = y axis -1 = up 1 = down
        System.out.println("Axis moved: " + axisCode + " : " + (int) value);

        if ((int) value == 0) {
            leftButtonPressed = false;
            rightButtonPressed = false;
            upButtonPressed = false;
            downButtonPressed = false;
        }
        if (axisCode == 1) {
            if (value == 1) {
                downButtonPressed = true;
            } else if (value == -1) {
                upButtonPressed = true;
            }
            return true;
        } else {
            if (value == 1) {
                rightButtonPressed = true;
            } else if (value == -1) {
                leftButtonPressed = true;
            }
            return true;
        }
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
        return false;
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
    public void connected(Controller controller) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnected(Controller controller) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

}

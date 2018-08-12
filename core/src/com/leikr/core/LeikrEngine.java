/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core;

import com.leikr.core.Graphics.LeikrPalette;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static com.leikr.core.ConsoleDirectory.Console.fileName;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Graphics.SpriteHandler;
import java.util.Random;

/**
 *
 * @author tor
 */
public class LeikrEngine implements InputProcessor {

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
    int mapType;//1. free map, 2. area map

    public boolean rightKeyPressed = false;
    public boolean leftKeyPressed = false;
    public boolean upKeyPressed = false;
    public boolean downKeyPressed = false;
    public boolean zKeyPressed = false;
    public boolean xKeyPressed = false;
    public boolean spaceKeyPressed = false;

    public int screenWidth = (int) Leikr.WIDTH;
    public int screenHeight = (int) Leikr.HEIGHT;

    public String backgroundColor = "BLACK";

    LeikrPalette leikrPalette;
    SpriteHandler spriteHandler;
    PaintBrush paintBrush;

    public void create() {
        game = LeikrGameScreen.game;
        batch = game.batch;
        shapeRenderer = new ShapeRenderer();

        spriteHandler = new SpriteHandler(game);
        paintBrush = new PaintBrush(shapeRenderer, game);
        leikrPalette = new LeikrPalette();

        viewport = new FitViewport(screenWidth, screenHeight);
        camera = viewport.getCamera();
        font = new Texture("LeikrFontA.png");

        System.out.println(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/" + fileName + "/" + fileName + ".tmx");
    }

    public void loadMap(int mapType) {
        this.mapType = mapType;
        useMap = true;
        tiledMap = new TmxMapLoader().load(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/" + fileName + "/" + fileName + ".tmx");
        tiledMapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);

//        tiledMapLayer.getCell(1, 1).getTile()
    }

    public int getCameraX() {
        return (int) camera.position.x - 128;
    }

    public int getCameraY() {
        return (int) camera.position.y - 100;
    }

    public void preRender() {
        switch (backgroundColor) {
            case "RED":
                Gdx.gl.glClearColor(1, 0, 0, 1);
                break;
            case "BLUE":
                Gdx.gl.glClearColor(0, 0, 1, 1);
                break;
            case "GREEN":
                Gdx.gl.glClearColor(0, 1, 0, 1);
                break;
            case "YELLOW":
                Gdx.gl.glClearColor(1, 1, 0, 1);
                break;
            default:
                Gdx.gl.glClearColor(0, 0, 0, 1);
                break;

        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void renderCamera() {
        //camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        if (tiledMapRenderer != null && useMap) {
            tiledMapRenderer.setView((OrthographicCamera) camera);
            tiledMapRenderer.render();
//            TextureRegion mpl = tiledMap.getTileSets().getTileSet("tileset").getTile(0).getTextureRegion();

        }
        camera.update();
    }

    public void render() {
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getColId(int x, int y) {
        System.out.println("X: " + x + " Y: "+ y);
        if (x >= 0 && y >= 0 && x <= tiledMap.getProperties().get("width", Integer.class) - 1 && y <= tiledMap.getProperties().get("height", Integer.class) - 1) {
            return tiledMapLayer.getCell(x, y).getTile().getId();
        }
        return -1;
    }

    public int getRandom(int range) {
        return new Random().nextInt(range);
    }

    public void drawText(String text, int x, int y, String color) {
        int fontX;
        int fontY;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        batch.begin();
        switch (color) {
            case "RED":
                batch.setColor(Color.RED);
                break;
            case "GREEN":
                batch.setColor(Color.GREEN);
                break;
            case "BLUE":
                batch.setColor(Color.BLUE);
                break;
            case "WHITE":
                batch.setColor(Color.WHITE);
                break;
            case "BLACK":
                batch.setColor(Color.BLACK);
                break;
            default:
                batch.setColor(Color.WHITE);
                break;
        }
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * 8;
            fontY = ((int) C / 16) * 8;
            batch.draw(font, x, y, fontX, fontY, 8, 8);
            x = x + 8;
        }
        batch.end();
    }

    void drawSprite(int id, float x, float y) {
        spriteHandler.drawSprite(id, x, y);
    }

    void drawSprite(int id, float x, float y, int scaleX, int scaleY) {
        spriteHandler.drawSprite(id, x, y, scaleX, scaleY);
    }

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     * @param type
     */
    public void drawRect(int x, int y, int width, int height, int color, String type) {
        paintBrush.drawRect(x, y, width, height, color, type);
    }

    public void drawCircle(int x, int y, int radius, int color, String type) {
        paintBrush.drawCircle(x, y, radius, color, type);
    }

    public void drawArc(int x, int y, int radius, int start, int degrees, int color, String type) {
        paintBrush.drawArc(x, y, radius, start, degrees, color, type);
    }

    public void drawLine(int x, int y, int x2, int y2, int color) {
        paintBrush.drawLine(x, y, x2, y2, color);
    }

    public void drawColor(int id, int x, int y) {
        paintBrush.drawColor(id, x, y);
    }

    public void setBackgroundColor(String color) {
        backgroundColor = color.toUpperCase();
    }

    public void drawPalette(int x, int y, int w, int h) {
        shapeRenderer.begin(ShapeType.Filled);
        for (int c : leikrPalette.palette) {
            shapeRenderer.setColor(new Color(c));
            shapeRenderer.rect(x, y, w, h);
            x += w;
        }
        shapeRenderer.end();
    }

    public boolean rightKeyPressed() {
        return rightKeyPressed;
    }

    public boolean leftKeyPressed() {
        return leftKeyPressed;
    }

    public boolean upKeyPressed() {
        return upKeyPressed;
    }

    public boolean downKeyPressed() {
        return downKeyPressed;
    }

    public boolean zKeyPressed() {
        return zKeyPressed;
    }

    public boolean xKeyPressed() {
        return xKeyPressed;
    }

    public boolean spaceKeyPressed() {
        return spaceKeyPressed;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.RIGHT:
                rightKeyPressed = true;
                break;
            case Keys.LEFT:
                leftKeyPressed = true;
                break;
            case Keys.UP:
                upKeyPressed = true;
                break;
            case Keys.DOWN:
                downKeyPressed = true;
                break;
            case Keys.Z:
                zKeyPressed = true;
                break;
            case Keys.X:
                xKeyPressed = true;
                break;
            case Keys.SPACE:
                spaceKeyPressed = true;
                break;
        }

        if (tiledMapRenderer != null) {
//            tiledMapLayer.getCell(0, 0).getTile().setTextureRegion(tiledMap.getTileSets().getTileSet("tileset").getTile(1).getTextureRegion());
//            System.out.println(tiledMapLayer.getCell(0, 0).getTile().getId());
//            System.out.println("width: " + (tiledMap.getProperties().get("width", Integer.class) - 1) + " height: " + (tiledMap.getProperties().get("height", Integer.class) - 1));

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.ESCAPE) {
            game.setScreen(new ConsoleScreen(game));
            this.dispose();
            return false;
        }
        switch (keycode) {
            case Keys.RIGHT:
                rightKeyPressed = false;
                break;
            case Keys.LEFT:
                leftKeyPressed = false;
                break;
            case Keys.UP:
                upKeyPressed = false;
                break;
            case Keys.DOWN:
                downKeyPressed = false;
                break;
            case Keys.Z:
                zKeyPressed = false;
                break;
            case Keys.X:
                xKeyPressed = false;
                break;
            case Keys.SPACE:
                spaceKeyPressed = false;
                break;
        }
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

}

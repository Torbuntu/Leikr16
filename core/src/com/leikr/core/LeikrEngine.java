/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static com.leikr.core.LeikrGameScreen.game;
import java.util.Random;

/**
 *
 * @author tor
 */
public class LeikrEngine implements InputProcessor {

    Leikr game = LeikrGameScreen.game;
    ShapeRenderer shapeRenderer;
    Camera camera;
    Viewport viewport;
    Texture font;

    public boolean rightKeyPressed = false;
    public boolean leftKeyPressed = false;
    public boolean upKeyPressed = false;
    public boolean downKeyPressed = false;
    public boolean zKeyPressed = false;
    public boolean xKeyPressed = false;
    public boolean spaceKeyPressed = false;

    public int screenWidth = 260;
    public int screenHeight = 160;

    public String backgroundColor = "BLACK";

    public void create() {

        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(screenWidth, screenHeight);
        camera = viewport.getCamera();
        font = new Texture("LeikrFontA.png");
    }

    public void setBackgroundColor(String color) {
        backgroundColor = color.toUpperCase();
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
            default:
                Gdx.gl.glClearColor(0, 0, 0, 1);
                break;

        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void render() {
    }

    public void renderCamera() {
        //camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
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

    public int getRandom(int range) {
        return new Random().nextInt(range);
    }

    public void drawText(String text, int x, int y) {
        int fontX;
        int fontY;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        game.batch.begin();
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * 8;
            fontY = ((int) C / 16) * 8;
            game.batch.draw(font, x, y, fontX, fontY, 8, 8);
            x = x + 8;
        }
        game.batch.end();
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
    public void drawRect(int x, int y, int width, int height, String color, String type) {

        switch (type) {
            case "Filled":
                shapeRenderer.begin(ShapeType.Filled);
                break;
            case "Line":
                shapeRenderer.begin(ShapeType.Line);
                break;
            default:
                shapeRenderer.begin(ShapeType.Line);
                break;
        }
        switch (color) {
            case "RED":
                shapeRenderer.setColor(Color.RED);
                break;
            case "GREEN":
                shapeRenderer.setColor(Color.GREEN);
                break;
            case "BLUE":
                shapeRenderer.setColor(Color.BLUE);
                break;
            case "WHITE":
                shapeRenderer.setColor(Color.WHITE);
                break;
            default:
                shapeRenderer.setColor(Color.BLACK);
                break;
        }

        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();

    }

    public void drawCircle(int x, int y, int radius, String color, String type) {
        switch (type) {
            case "Filled":
                shapeRenderer.begin(ShapeType.Filled);
                break;
            case "Line":
                shapeRenderer.begin(ShapeType.Line);
                break;
            default:
                shapeRenderer.begin(ShapeType.Line);
                break;
        }
        switch (color) {
            case "RED":
                shapeRenderer.setColor(Color.RED);
                break;
            case "GREEN":
                shapeRenderer.setColor(Color.GREEN);
                break;
            case "BLUE":
                shapeRenderer.setColor(Color.BLUE);
                break;
            case "WHITE":
                shapeRenderer.setColor(Color.WHITE);
                break;
            default:
                shapeRenderer.setColor(Color.BLACK);
                break;
        }
        shapeRenderer.circle(x, y, radius);
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
    public boolean spaceKeyPressed(){
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

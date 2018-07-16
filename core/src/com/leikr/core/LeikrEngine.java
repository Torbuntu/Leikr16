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
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    public boolean rightKeyPressed = false;
    public boolean leftKeyPressed = false;
    public boolean upKeyPressed = false;
    public boolean downKeyPressed = false;

    public float screenWidth = Leikr.WIDTH;
    public float screenHeight = Leikr.HEIGHT;

    public void create() {

        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(screenWidth, screenHeight);
        camera = viewport.getCamera();
    }

    public void render() {
    }
    public void renderCamera(){
        //camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
    
    
    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }
    

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }
    
    public int getRandom(int range){
        return new Random().nextInt(range);
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

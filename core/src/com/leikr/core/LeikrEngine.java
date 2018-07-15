/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core;

import java.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author tor
 */
public class LeikrEngine implements InputProcessor {

    Leikr game = LeikrGameScreen.game;
    ShapeRenderer shapeRenderer;

    public boolean rightKeyPressed = false;
    public boolean leftKeyPressed = false;
    public boolean upKeyPressed = false;
    public boolean downKeyPressed = false;

    public int screenWidth = 260;
    public int screenHeight = 160;

    public void create() {

        shapeRenderer = new ShapeRenderer();
    }

    public void render() {
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
    
    
    
    

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
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

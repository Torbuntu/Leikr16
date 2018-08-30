package com.leikr.core.ConsoleDirectory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.Leikr;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: This class is way too big. Needs to be cut up into solid pieces
public class Console implements InputProcessor {

    //global variables for the console.
    SpriteBatch batch;
    Camera camera;
    Viewport viewport;
    public static String fileName = "LeikrGame";
    public static String gameType = "groovy";

    final Leikr game;
    ConsoleScreen consoleScreen;

    FontHandler fontHandler;
    ShellHandler shellHandler;
    
    ShapeRenderer renderer;
    
    
    public Console(ConsoleScreen consoleScreen) {
        this.game = consoleScreen.game;
        this.consoleScreen = consoleScreen;
        batch = game.batch;

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();
        Gdx.input.setInputProcessor(this);

        fontHandler = new FontHandler(game, viewport);//handles command and history buffer for displaying font to screen.
        shellHandler = new ShellHandler(game, consoleScreen, fontHandler);
        renderer = new ShapeRenderer();
    }
    
    public Console(ConsoleScreen consoleScreen, String error) {
        this.game = consoleScreen.game;
        this.consoleScreen = consoleScreen;
        batch = game.batch;

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();
        Gdx.input.setInputProcessor(this);

        fontHandler = new FontHandler(game, viewport);//handles command and history buffer for displaying font to screen.
        fontHandler.setHistory(error);
        shellHandler = new ShellHandler(game, consoleScreen, fontHandler);
        renderer = new ShapeRenderer();
    }
    
    

    //Sets the camera projection. Begins the sprite batch, runs the console buffer to display text.
    public void renderConsole(float delta) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(shellHandler.bgRed, shellHandler.bgGreen, shellHandler.bgBlue, 0);
        renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
        renderer.end();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(shellHandler.fontRed, shellHandler.fontGreen, shellHandler.fontBlue, 1); // sets font color

        fontHandler.displayBufferedString(delta);
        batch.end();

    }

    //Disposes batch and font
    public void disposeConsole() {
        fontHandler.disposeFont();
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        fontHandler.updateViewport(width, height);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.BACKSPACE:
                fontHandler.backspaceHandler();
                break;
            case Input.Keys.ENTER: {
                try {
                    shellHandler.handleInput(fontHandler.getCommands(), fontHandler.getHistory());
                } catch (IOException ex) {
                    Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fontHandler.clearCommandBuffer();
            break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        //If the character not backspace or enter.
        fontHandler.addKeyStroke(character);
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

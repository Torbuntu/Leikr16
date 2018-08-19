package com.leikr.core.ConsoleDirectory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    Texture font;
    Camera camera;
    Viewport viewport;
    public static String fileName = "LeikrGame";
    public static String gameType = "groovy";

    final Leikr game;
    ConsoleScreen consoleScreen;

    FontHandler fontHandler;
    ShellHandler shellHandler;
    
    
    public Console(final Leikr game, ConsoleScreen consoleScreen) {
        this.game = game;
        this.consoleScreen = consoleScreen;
        batch = game.batch;
        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/LeikrFontA.png"));

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();
        Gdx.input.setInputProcessor(this);

        fontHandler = new FontHandler(game.batch, viewport);//handles command and history buffer for displaying font to screen.
        shellHandler = new ShellHandler(game, consoleScreen, fontHandler);
    }

    //Sets the camera projection. Begins the sprite batch, runs the console buffer to display text.
    public void renderConsole(float delta) {
        Gdx.gl.glClearColor(shellHandler.bgRed, shellHandler.bgGreen, shellHandler.bgBlue, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(shellHandler.fontRed, shellHandler.fontGreen, shellHandler.fontBlue, 1); // sets font color

        fontHandler.displayBufferedString(delta);
        batch.end();

    }

    //Disposes batch and font
    public void disposeConsole() {
        font.dispose();
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

package com.system.leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import groovy.lang.GroovyShell;
import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Console implements InputProcessor {

    //global variables for the console.
    SpriteBatch batch;
    Texture font;
    Camera camera;
    Viewport viewport;
    static final int WIDTH = 260;
    static final int HEIGHT = 160;

    //New groovy shell.
    GroovyShell groovyShell = new GroovyShell();

    //The buffer for drawing commands and history
    ArrayList<String> commandBuffer = new ArrayList<>();
    ArrayList<String> historyBuffer = new ArrayList<>();

    BiosLoader biosLoader;
    //Primary constructor. Sets a new SpriteBatch for drawing fonts. Loads the font texture.
    // Camera and Viewport initialized and the input processor set to this item.
    final Leikr game;
    ConsoleScreen consoleScreen;
    
    public Console(final Leikr game, ConsoleScreen consoleScreen) {
        this.game = game;
        this.consoleScreen = consoleScreen;
        batch = game.batch;
        font = new Texture("LeikrFontA.png");

        try {
            biosLoader = new BiosLoader();
        } catch (IOException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }
        historyBuffer.add("Bios: " + biosLoader.getBiosVersion());

        camera = new OrthographicCamera(260, 160);
        viewport = new FitViewport(260, 160, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);//Sets the camera to the correct position.
        Gdx.input.setInputProcessor(this);
    }

    //Sets the camera projection. Begins the sprite batch, runs the console buffer to display text.
    public void renderConsole() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        nDisplayBufferedString();
        batch.end();        
        
    }

    //Disposes batch and font
    public void disposeConsole() {
        font.dispose();
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    //writes the path pre-pending the command buffer.
    private void nWritePath(float carriage, float line) {
        int X;
        int Y;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        X = ((int) '~' % 16) * 8;
        Y = ((int) '~' / 16) * 8;
        batch.draw(font, carriage, line, X, Y, 8, 8);
        X = ((int) '$' % 16) * 8;
        Y = ((int) '$' / 16) * 8;
        batch.draw(font, carriage + 8f, line, X, Y, 8, 8);
    }

    //Runs through the history buffer and sets the items to the screen. Returns the line position to correctly set the command buffer input.
    public float nDisplayHistoryString(float ln) {
        int X;
        int Y;
        float carriage;
        float line = ln;
        for (String item : historyBuffer) {
            carriage = 0;
            for (char C : item.toCharArray()) {
                if (carriage >= viewport.getWorldWidth() - 8f) {
                    carriage = 0;
                    line -= 8f;
                }
                X = ((int) C % 16) * 8;
                Y = ((int) C / 16) * 8;
                batch.draw(font, carriage, line, X, Y, 8, 8);
                carriage += 8f;
            }
            line -= 8f;
        }
        return line;
    }

    //Displays the command buffer after running the history and new path. Checks the height and removes history to keep on screen. Displays blank box for cursor.
    public void nDisplayBufferedString() {
        float carriage = 0;
        float line = viewport.getWorldHeight() - 8f;
        int X;
        int Y;

        String result = String.join(",", commandBuffer).replaceAll(",", "");
        if (historyBuffer.size() > 0) {
            line = nDisplayHistoryString(line);
        }

        nWritePath(carriage, line);
        carriage += 16f;
        for (char C : result.toCharArray()) {
            if (carriage >= viewport.getWorldWidth() - 8f) {
                carriage = -1f;
                line -= 8f;
            }
            X = ((int) C % 16) * 8;
            Y = ((int) C / 16) * 8;
            batch.draw(font, carriage, line, X, Y, 8, 8);
            carriage += 8f;
        }

        if (line <= -8f && historyBuffer.size() > 0) {
            System.out.println(historyBuffer.remove(0));
        }

        batch.draw(font, carriage, line, 0, 0, 8, 8);
    }

    public void backspaceHandler() {
        if (commandBuffer.size() > 0) {
            commandBuffer.remove(commandBuffer.size() - 1);
        }
    }

    // Handles the command input.
    public void shellHandler() {
        //parse the command buffer into a String.
        String in = String.join(",", commandBuffer).replaceAll(",", "");
        String[] inputList = in.split(" ");
        
        historyBuffer.add("~$" + in);

        System.out.println("HBuffer: " + historyBuffer);

        //Default command not recognized.
        String notRecognized = "Command '" + in + "' is not recognized...";

        String result;

        //Convert to switch.
        if (inputList[0].equals("echo")) { // Process echo command
            in = in.replaceFirst("echo ", "");
            historyBuffer.add(in);

        } else {
            switch (in) {
                case "":
                    //cursorPos[1] -= 8;
                    //printPath();
                    break;
                case "help":
                    historyBuffer.add("There is no help here yet...");
                    break;
                case "exit": //close on exit command.
                    System.exit(0);
                    break;
                case "clear":
                    historyBuffer.clear();
                    commandBuffer.clear();
                    break;                    
                case "test":
                    game.setScreen(new LeikrGameScreen(game));
                    consoleScreen.dispose();
                    break;
                default: //Default, command not recognized.
                    try {
                        result = groovyShell.evaluate(in).toString();
                        System.out.println(result);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        result = "";
                    }
                    try {
                        result = (String) biosLoader.runRegisteredMethod(inputList);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        result = "";
                    }

                    if (result.length() > 0) {
                        historyBuffer.add(result);
                    } else {
                        historyBuffer.add(notRecognized);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.BACKSPACE:
                backspaceHandler();
                break;
            case Input.Keys.ENTER:
                shellHandler();
                commandBuffer.clear();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        //If the character not backspace or enter.
        if ((int) character != 8 && (int) character != 10) {
            commandBuffer.add(String.valueOf(character));
        }
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

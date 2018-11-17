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
package com.leikr.core.ConsoleDirectory;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.Leikr;
import com.leikr.core.System.LeikrSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tor
 */
public class TextHandler {

    SpriteBatch batch;
    Texture font;
    Viewport viewport;
    public Leikr game;

    float line;
    float carriage;
    float blink;

    int glyphWidth;
    int glyphHeight;

    float fontRed;
    float fontGreen;
    float fontBlue;

    float bgRed;
    float bgGreen;
    float bgBlue;

    ArrayList<String> command;
    ArrayList<String> history;
    ArrayList<String> sessionHistory;
    public LeikrSystem leikrSystem;
    public ConsoleScreen consoleScreen;

    public TextHandler(Leikr game, Viewport viewport, Console console) {
        this.batch = console.batch;
        this.viewport = viewport;
        this.game = game;
        this.consoleScreen = console.consoleScreen;

        command = new ArrayList<>();
        history = new ArrayList<>();
        sessionHistory = new ArrayList<>();

        try {
            leikrSystem = new LeikrSystem(this);
        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            fontRed = game.customSettings.fontRed;
            fontGreen = game.customSettings.fontGreen;
            fontBlue = game.customSettings.fontBlue;

            bgRed = game.customSettings.bgRed;
            bgGreen = game.customSettings.bgGreen;
            bgBlue = game.customSettings.bgBlue;
        } catch (Exception e) {
            fontRed = 1;
            fontGreen = 1;
            fontBlue = 1;

            bgRed = 0;
            bgGreen = 0;
            bgBlue = 0;
            System.out.println("No custom settings file in OS: " + e.getMessage());
        }

        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/" + game.customSettings.fontName));

        blink = 0;

        glyphWidth = (int) game.customSettings.glyphWidth;
        glyphHeight = (int) game.customSettings.glyphHeight;
    }

    public void setFontColor(String red, String green, String blue) {
        fontRed = Float.valueOf(red);
        fontGreen = Float.valueOf(green);
        fontBlue = Float.valueOf(blue);
    }

    public void setBgColor(String red, String green, String blue) {
        bgRed = Float.valueOf(red);
        bgGreen = Float.valueOf(green);
        bgBlue = Float.valueOf(blue);
    }

    private void drawFont(String characters) {
        for (char C : characters.toCharArray()) {
            switch (C) {
                case '\n':
                case '\r':
                    carriage = 0;
                    line -= glyphHeight;
                    break;
                default:
                    if (carriage >= viewport.getWorldWidth() - glyphWidth) {
                        carriage = 0;
                        line -= glyphHeight;
                    }
                    int X = (((int) C % 16) * glyphWidth);
                    int Y = (((int) C / 16) * glyphHeight);
                    batch.draw(font, carriage, line, X, Y, glyphWidth, glyphHeight);
                    carriage += glyphWidth;
                    break;
            }
        }
    }

    //Runs through the history buffer and sets the items to the screen. Returns the line position to correctly set the command buffer input.
    void displayHistoryString(float ln) {
        line = ln;
        for (String item : history) {
            carriage = 0;
            drawFont(item);
            line -= glyphHeight;
        }
    }

    //Displays the command buffer after running the history and new path. Checks the height and removes history to keep on screen. Displays blank box for cursor.
    public void displayCurrentText(float delta) {
        line = viewport.getWorldHeight() - glyphHeight;

        if (history.size() > 0) {
            displayHistoryString(line);
        }

        carriage = 0;
        drawFont("~$" + getCommandString());//pre-pend the path chars

        // Removes history that is off the screen to fit the most recent at the bottom.
        if (line <= -glyphHeight && history.size() > 0) {
            history.remove(0);
        }

        blinkCursor(delta);
    }

    void blinkCursor(float delta) {
        if (blink > 0.4) {
            batch.draw(font, carriage, line, 0, 0, glyphWidth, glyphHeight);
            blink = blink > 1 ? 0 : blink;
        }
        blink += delta;
    }

    public void clearCommandBuffer() {
        command.clear();
    }

    public void clearHistoryBuffer() {
        history.clear();
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public ArrayList<String> getCommands() {
        return command;
    }

    public String getCommandString() {
        return String.join(",", command).replaceAll(",", "");
    }

    public void setHistory(String historical) {
        if (history.size() > 60) {
            for (int i = 0; i < 10; i++) {
                history.remove(0);
            }
        }
        history.add(historical);
    }

    public void performBackspace() {
        if (command.size() > 0) {
            command.remove(command.size() - 1);
        }
    }

    public void addKeyStroke(char character) {
        if ((int) character != 8 && (int) character != 10) {
            command.add(String.valueOf(character));
        }
    }

    // Handles the command input.
    public void handleInput() {
        //parse the command buffer into a String. Add to history line.
        String inputString = String.join(",", command).replaceAll(",", "");
        history.add("~$" + inputString);

        // Convert to list for switch checking.
        String[] inputList = inputString.split(" ");

        // Skip the input if there is no item to check against.
        if (inputList[0].length() < 1) {
            return;
        }
        String result = "";
        try {

            try {
                result = (String) leikrSystem.runSystemMethod(inputList);
            } catch (Exception e) {
                result = "System commands failed: " + e.getMessage();
            }
        } catch (Exception e) {
            // return any error to the screen.
            history.add(e.getMessage());
        }

        // If result, add to history.
        if (result.length() > 0) {
            history.add(result);
        }
        clearCommandBuffer();//Clear the command when finished processing.
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    public void disposeTextHandler() {
        font.dispose();
    }
}

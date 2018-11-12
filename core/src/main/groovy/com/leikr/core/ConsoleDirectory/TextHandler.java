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

    public String getCommandString() {
        return String.join(",", command).replaceAll(",", "");
    }

    public void performBackspace() {
        if (command.size() > 0) {
            command.remove(command.size() - 1);
        }
    }

    public void addCommand(char character) {
        if ((int) character != 8 && (int) character != 10) {
            command.add(String.valueOf(character));
        }
    }

    public TextHandler(Leikr game, Viewport viewport, ConsoleScreen consoleScreen) {
        this.batch = game.batch;
        this.viewport = viewport;
        this.game = game;
        command = new ArrayList<>();
        history = new ArrayList<>();
        sessionHistory = new ArrayList<>();
        
        this.consoleScreen = consoleScreen;

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
            if (carriage >= viewport.getWorldWidth() - glyphWidth) {
                carriage = 0;
                line -= glyphHeight;
            }
            int X = (((int) C % 16) * glyphWidth);
            int Y = (((int) C / 16) * glyphHeight);
            batch.draw(font, carriage, line, X, Y, glyphWidth, glyphHeight);
            carriage += glyphWidth;
        }
    }

    //Runs through the history buffer and sets the items to the screen. Returns the line position to correctly set the command buffer input.
    public void displayHistoryString(float ln) {
        line = ln;
        for (String item : history) {
            carriage = 0;
            drawFont(item);
            line -= glyphHeight;
        }
    }

    //Displays the command buffer after running the history and new path. Checks the height and removes history to keep on screen. Displays blank box for cursor.
    public void displayBufferedString(float delta) {
        carriage = 0;
        line = viewport.getWorldHeight() - glyphHeight;

        String result = getCommandString();

        if (history.size() > 0) {
            displayHistoryString(line);
            carriage = 0;
        }

        drawFont("~$" + result);//pre-pend the path chars

        if (line <= -glyphHeight && history.size() > 0) {
            System.out.println(history.remove(0));
        }

        if (blink > 0.4) {
            batch.draw(font, carriage, line, 0, 0, glyphWidth, glyphHeight);
            blink += delta;
            if (blink > 1) {
                blink = 0;
            }
        } else {
            blink += delta;
        }
    }

    public void addKeyStroke(char character) {
        //If the character not backspace or enter.
        addCommand(character);
    }

    public void clearCommandBuffer() {
        command.clear();
    }

    public void clearHistoryBuffer() {
        history.clear();
    }

    public void backspaceHandler() {
        performBackspace();
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(String historical) {
        if (history.size() > 60) {
            for (int i = 0; i < 10; i++) {
                history.remove(0);
            }
            System.out.println(historical);
        }
        history.add(historical);
    }

    public ArrayList<String> getCommands() {
        return command;
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    public void disposeTextHandler() {
        font.dispose();

    }

    // Handles the command input.
    public void handleInput(ArrayList<String> commandBuffer, ArrayList<String> historyBuffer) throws IOException {
        //parse the command buffer into a String.
        String inputString = String.join(",", commandBuffer).replaceAll(",", "");

        // Convert to list for switch checking.
        String[] inputList = inputString.split(" ");

        historyBuffer.add("~$" + inputString);

        String result = "";
        try {
            switch (inputList[0]) {
                case "":
                    //do nothing
                    break;
                default: //Default, command not recognized.

                    try {
                        result = (String) leikrSystem.runSystemMethod(inputList);                        
                    } catch (Exception e) {
                        result = "System commands failed: " + e.getMessage();
                    }
                    break;
            }
        } catch (Exception e) {
            // return any error to the screen.
            historyBuffer.add(e.getMessage());
        }

        // If result, add to history.
        if (result.length() > 0) {
            historyBuffer.add(result);
        }
    }

}

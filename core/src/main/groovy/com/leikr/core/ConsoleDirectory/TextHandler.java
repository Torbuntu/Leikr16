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
import java.util.ArrayList;

/**
 *
 * @author tor
 */
public class TextHandler {

    SpriteBatch batch;
    Texture font;
    Viewport viewport;
    Leikr game;

    float line;
    float carriage;
    float blink;

    int glyphWidth;
    int glyphHeight;

    ArrayList<String> command;
    ArrayList<String> history;
    ArrayList<String> sessionHistory;

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

    public TextHandler(Leikr game, Viewport viewport) {
        this.batch = game.batch;
        this.viewport = viewport;
        command = new ArrayList<>();
        history = new ArrayList<>();
        sessionHistory = new ArrayList<>();

        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/" + game.customSettings.fontName));

        blink = 0;

        glyphWidth = (int) game.customSettings.glyphWidth;
        glyphHeight = (int) game.customSettings.glyphHeight;
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

    public void disposeFont() {
        font.dispose();
    }

}

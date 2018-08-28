/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class FontHandler {

    TextBuffer textBuffer;
    SpriteBatch batch;
    Texture font;
    Viewport viewport;
    float blink;
    Leikr game;

    float line;
    float carriage;

    int glyphWidth;
    int glyphHeight;

    public FontHandler(Leikr game, Viewport viewport) {
        this.batch = game.batch;
        this.viewport = viewport;
        textBuffer = new TextBuffer();

        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/" + game.customSettings.fontName));

        blink = 0;

        glyphWidth = (int) game.customSettings.glyphWidth;
        glyphHeight = (int) game.customSettings.glyphHeight;
    }

    private void drawFont(String characters) {
        String[] inputList = characters.split(" ");
        //TODO: Finish experimental line wrapping logic
//        for (String words : inputList) {
//            words = words + " ";
//            if((words.length() + carriage) >= viewport.getWorldWidth() - 8f){
//                carriage = 0;
//                line -=8f;
//            }
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
//        }
    }

    //Runs through the history buffer and sets the items to the screen. Returns the line position to correctly set the command buffer input.
    public void displayHistoryString(float ln) {
        line = ln;
        for (String item : textBuffer.history) {
            carriage = 0;
            drawFont(item);
            line -= glyphHeight;
        }
    }

    //Displays the command buffer after running the history and new path. Checks the height and removes history to keep on screen. Displays blank box for cursor.
    public void displayBufferedString(float delta) {
        carriage = 0;
        line = viewport.getWorldHeight() - glyphHeight;

        String result = textBuffer.getCommandString();

        if (textBuffer.history.size() > 0) {
            displayHistoryString(line);
            carriage = 0;
        }

        drawFont("~$" + result);//pre-pend the path chars

        if (line <= -glyphHeight && textBuffer.history.size() > 0) {
            System.out.println(textBuffer.history.remove(0));
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
        textBuffer.addCommand(character);
    }

    public void clearCommandBuffer() {
        textBuffer.command.clear();
    }

    public void clearHistoryBuffer() {
        textBuffer.history.clear();
    }

    public void backspaceHandler() {
        textBuffer.performBackspace();
    }

    public ArrayList<String> getHistory() {
        return textBuffer.history;
    }
    
    public void setHistory(String history) {
        textBuffer.history.add(history);
    }

    public ArrayList<String> getCommands() {
        return textBuffer.command;
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    public void disposeFont() {
        font.dispose();
    }

}

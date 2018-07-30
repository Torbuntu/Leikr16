/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.ConsoleDirectory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;

/**
 *
 * @author tor
 */
public class FontHandler {

    //New groovy shell.
    ArrayList<String> commandBuffer = new ArrayList<>();
    ArrayList<String> historyBuffer = new ArrayList<>();
    SpriteBatch batch;
    Texture font;
    Viewport viewport;
    float blink;

    float line;
    float carriage;

    public FontHandler(SpriteBatch batch, Viewport viewport) {
        this.batch = batch;
        this.viewport = viewport;

        font = new Texture("LeikrFontA.png");
        blink = 0;
    }

    private void drawFont(String characters) {
        for (char C : characters.toCharArray()) {
            if (carriage >= viewport.getWorldWidth() - 8f) {
                carriage = 0;
                line -= 8f;
            }
            int X = ((int) C % 16) * 8;
            int Y = ((int) C / 16) * 8;
            batch.draw(font, carriage, line, X, Y, 8, 8);
            carriage += 8f;
        }
    }

    //Runs through the history buffer and sets the items to the screen. Returns the line position to correctly set the command buffer input.
    public void displayHistoryString(float ln) {
        line = ln;
        for (String item : historyBuffer) {
            carriage = 0;
            drawFont(item);
            line -= 8f;
        }
    }

    //Displays the command buffer after running the history and new path. Checks the height and removes history to keep on screen. Displays blank box for cursor.
    public void displayBufferedString(float delta) {
        carriage = 0;
        line = viewport.getWorldHeight() - 8f;

        String result = String.join(",", commandBuffer).replaceAll(",", "");
        if (historyBuffer.size() > 0) {
            displayHistoryString(line);
            carriage = 0;
        }
        
        drawFont("~$" + result);//pre-pend the path chars

        if (line <= -8f && historyBuffer.size() > 0) {
            System.out.println(historyBuffer.remove(0));
        }

        if (blink > 0.4) {
            batch.draw(font, carriage, line, 0, 0, 8, 8);
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
        if ((int) character != 8 && (int) character != 10) {
            commandBuffer.add(String.valueOf(character));
        }
    }

    public void clearCommandBuffer() {
        commandBuffer.clear();
    }

    public void clearHistoryBuffer() {
        historyBuffer.clear();
    }

    public void backspaceHandler() {
        if (commandBuffer.size() > 0) {
            commandBuffer.remove(commandBuffer.size() - 1);
        }
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    public void disposeFont() {
        font.dispose();
    }

}
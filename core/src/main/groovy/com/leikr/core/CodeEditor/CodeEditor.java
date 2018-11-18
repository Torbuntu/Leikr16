/*
 * Copyright 2018 torbuntu.
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
package com.leikr.core.CodeEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.Leikr;
import java.util.Arrays;

/**
 *
 * @author tor
 */
public class CodeEditor {

    final Leikr game;
    CodeEditorInputProcessor ceip;
    SpriteBatch batch;
    Camera camera;
    Viewport viewport;
    Texture font;

    float line;
    float carriage;
    float blink;

    int glyphWidth;
    int glyphHeight;

    public CodeEditor(Leikr game) {
        this.game = game;
        ceip = new CodeEditorInputProcessor(game, this);
        batch = new SpriteBatch();
        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();

        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/" + game.customSettings.fontName));

        blink = 0;

        glyphWidth = (int) game.customSettings.glyphWidth;
        glyphHeight = (int) game.customSettings.glyphHeight;

        line = viewport.getWorldHeight() - glyphHeight;
        carriage = 0;
        Gdx.input.setInputProcessor(ceip);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        line = viewport.getWorldHeight() - glyphHeight;
        carriage = 0;
        parseWords("Hello World! The Code Editor isn't currently available. Come back after a future update to try it out!");
        batch.end();

    }

    private void parseWords(String words) {
        String[] wordList = words.split(" ");
        for (String w : wordList) {
            drawFontString(w);
        }
    }

    private void drawFontString(String characters) {
        if ((characters.length()*glyphWidth)+carriage > (viewport.getWorldWidth()-glyphWidth)) {
            carriage = 0;
            line -= glyphHeight;
        }
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
        carriage += glyphWidth;//space after each word
    }

    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

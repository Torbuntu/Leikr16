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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class ConsoleScreen implements Screen{
    final Leikr game;
    
    public Console console;
    public ConsoleScreen(final Leikr game){
        this.game = game;
        console = new Console(this);
    }
    
    public ConsoleScreen(final Leikr game, String error){
        this.game = game;
        console = new Console(this, error);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(console);
    }

    @Override
    public void render(float delta) {
        //Update the console items
        console.renderConsole(delta);
    }

    @Override
    public void resize(int width, int height) {
        //resize the viewport in console.
        console.updateViewport(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        //dispose console items
        console.disposeConsole();
    }
    
}

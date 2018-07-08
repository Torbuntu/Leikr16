/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.system.leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 *
 * @author tor
 */
public class ConsoleScreen implements Screen{
    final Leikr game;
    
    Console console;
    ConsoleScreen(final Leikr game){
        this.game = game;
    }
    
    @Override
    public void show() {
        console = new Console(game, this);  

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update the console items
        console.renderConsole();
        
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

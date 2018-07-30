/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.ConsoleDirectory;

import com.badlogic.gdx.Screen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class ConsoleScreen implements Screen{
    final Leikr game;
    
    Console console;
    public ConsoleScreen(final Leikr game){
        this.game = game;
    }
    
    @Override
    public void show() {
        console = new Console(game, this);  

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

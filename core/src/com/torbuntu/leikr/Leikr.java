package com.torbuntu.leikr;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Leikr extends ApplicationAdapter {

    //Primary Console
    Console console;

    @Override
    public void create() {
        //Init new console
        console = new Console();  
//        try {
//            this.setScreen(new GamePlayerScreen());
//        } catch (InstantiationException ex) {
//            Logger.getLogger(Leikr.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Leikr.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(Leikr.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update the console items
        console.renderConsole();
    }

    @Override
    public void dispose() {
        //dispose console items
        console.disposeConsole();
    }

    @Override
    public void resize(int width, int height) {
        //resize the viewport in console.
        console.updateViewport(width, height);
    }

}

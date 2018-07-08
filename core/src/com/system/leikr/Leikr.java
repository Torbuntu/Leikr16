package com.system.leikr;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Leikr extends Game {

    //Primary Console
    Console console;
    
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        //Init new console
        batch = new SpriteBatch();
        font = new BitmapFont();
        
        this.setScreen(new ConsoleScreen(this));

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
       
    }

}

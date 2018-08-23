package com.leikr.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leikr.core.ConsoleDirectory.CustomSettings;
import com.leikr.core.TitleScreen.TitleScreen;

public class Leikr extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public static final float WIDTH = 320, HEIGHT = 240;    

    public static String ROOT_PATH;
    
    public CustomSettings customSettings;
    
    @Override
    public void create() {
        //Init new console
        batch = new SpriteBatch();
        font = new BitmapFont();
        
        ROOT_PATH = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/";
        
        customSettings = new CustomSettings();

//        this.setScreen(new ConsoleScreen(this));
//        this.setScreen(new IntroScreen(this));
        this.setScreen(new TitleScreen(this));
//        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

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
        super.resize(width, height);
    }

}

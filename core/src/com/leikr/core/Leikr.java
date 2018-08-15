package com.leikr.core;

import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.ConsoleDirectory.Console;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leikr.core.TitleScreen.TitleScreen;
import com.leikr.core.intro.IntroScreen;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class Leikr extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public static final float WIDTH = 256, HEIGHT = 240;

    @Override
    public void create() {
        //Init new console
        batch = new SpriteBatch();
        font = new BitmapFont();

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

package com.leikr.core;

import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.ConsoleDirectory.Console;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class Leikr extends Game {

    //Primary Console
    Console console;
    
    public SpriteBatch batch;
    public BitmapFont font;
    
    public static final float WIDTH = 260, HEIGHT = 160;

    @Override
    public void create() {
        //Init new console
        batch = new SpriteBatch();
        font = new BitmapFont();
        
        //test();
        
        
        this.setScreen(new ConsoleScreen(this));

    }
    
    public void test(){
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = mgr.getEngineFactories();

        for (ScriptEngineFactory factory : factories) {

            System.out.println("ScriptEngineFactory Info");

            String engName = factory.getEngineName();
            String engVersion = factory.getEngineVersion();
            String langName = factory.getLanguageName();
            String langVersion = factory.getLanguageVersion();

            System.out.printf("\tScript Engine: %s (%s)%n", engName, engVersion);

            List<String> engNames = factory.getNames();
            for(String name : engNames) {
                System.out.printf("\tEngine Alias: %s%n", name);
            }

            System.out.printf("\tLanguage: %s (%s)%n", langName, langVersion);

        }
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

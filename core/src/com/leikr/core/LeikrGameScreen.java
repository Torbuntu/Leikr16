/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core;

import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.ConsoleDirectory.Console;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.leikr.core.Leikr;
import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tor
 */
public class LeikrGameScreen implements Screen, InputProcessor {

    static Leikr game;
    String fileName;

    final GroovyClassLoader classLoader;
    Class gameLoader;
    LeikrEngine leikrGame;
    
    public static Texture spriteSheet;

    public LeikrGameScreen(Leikr game) throws IOException {
        LeikrGameScreen.game = game;
        fileName = Console.fileName;
        String filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/"+fileName+"/";//sets game path
        classLoader = new GroovyClassLoader();
        gameLoader = classLoader.parseClass(new File(filePath + fileName + ".groovy"));//loads the game code
        
        spriteSheet = new Texture(filePath + fileName + ".png");
        System.out.println(filePath + fileName+".png");
        
        try {
            leikrGame = (LeikrEngine) gameLoader.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(LeikrGameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void show() {

        leikrGame.create();
        Gdx.input.setInputProcessor(leikrGame);
    }

    @Override
    public void render(float delta) {
        leikrGame.preRender();
        
        leikrGame.renderCamera();
        
        leikrGame.render();
    }

    @Override
    public void resize(int width, int height) {
        leikrGame.updateViewport(width, height);
        //viewport.update(width, height, true);
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
        leikrGame.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.ESCAPE) {
            game.setScreen(new ConsoleScreen(game));
            this.dispose();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}

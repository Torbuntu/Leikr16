/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torbuntu.leikr;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.File;
import java.io.IOException;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author tor
 */
public class GamePlayerScreen extends ApplicationAdapter{
    GroovyObject game;
    OrthographicCamera camera;
    public GamePlayerScreen() throws InstantiationException, IOException, IllegalAccessException{
        GroovyClassLoader classLoader = new GroovyClassLoader();
        Class gameClass = classLoader.parseClass(new File(Gdx.files.getExternalStoragePath() +"LeikrVirtualDrive/ChipSpace/Game.groovy"));
        
        game = (GroovyObject)gameClass.newInstance();           
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        try{
            game.invokeMethod("create", null);       
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }


    public void render(float delta) {
        camera.update();
        game.invokeMethod("render", null);

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resize(int width, int height) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    public void dispose() {
        game.invokeMethod("dispose", null);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

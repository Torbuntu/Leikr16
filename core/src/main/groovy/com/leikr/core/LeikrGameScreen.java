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
package com.leikr.core;

import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.codehaus.groovy.control.CompilationFailedException;
import org.python.util.PythonInterpreter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author tor
 */
public class LeikrGameScreen implements Screen, InputProcessor {

    final Leikr game;
    GroovyClassLoader groovyClassLoader;
    Class groovyGameClass;
    LeikrEngine leikrGame;

    ScriptEngineManager scriptManager;
    ScriptEngine scriptEngine;

    public LeikrGameScreen(Leikr game) {
        this.game = game;
        scriptManager = new ScriptEngineManager();

        String filePath = Leikr.ROOT_PATH + "ChipSpace/" + Leikr.GAME_NAME + "/";//sets game path

        try {
            switch (Leikr.GAME_TYPE.toLowerCase()) {
                case "groovy":
                case "gv":
                    loadGroovyGame(filePath);
                    break;
                case "java":
                case "jv":
                    loadJavaGame(filePath);
                    break;
                case "jython":
                case "python":
                case "jy":
                case "py":
                    loadJythonGame("com.leikr.core.LeikrEngine", filePath + Leikr.GAME_NAME + ".py");
                    break;
                default:
                    loadGroovyGame(filePath);
                    break;
            }
        } catch (Exception e) {
            game.beginConsole();
            this.dispose();
        }

    }

    public void loadScalaGame(String filePath) {

    }

    private void loadJavaGame(String filePath) {

        File f = new File(filePath);// Create file of Java game.
        try {
            // Create URL for loading the external files.
            URL[] cp = {f.toURI().toURL()};
            URLClassLoader urlCl = new URLClassLoader(cp);

            //Compile the Java source code.
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            String toCompile = java.io.File.separator + filePath + Leikr.GAME_NAME + ".java";
            compiler.run(null, null, null, toCompile);

            //New instance
            Class jvGame = urlCl.loadClass(Leikr.GAME_NAME);
            Constructor[]  cnst = jvGame.getConstructors();
            leikrGame = (LeikrEngine) cnst[0].newInstance();
            leikrGame.setCoreGame(game);
            
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            game.beginConsole();
            this.dispose();
        }

    }

//    private void loadJythonGame(String filePath) {
//        leikrGame = (LeikrEngine) getJythonObject("com.leikr.core.LeikrEngine", filePath + Leikr.GAME_NAME + ".py");
//        leikrGame.setCoreGame(game);
//    }

    public void loadJythonGame(String interfaceName, String pathToJythonModule) {
        Object javaInt = null;

        try {

            PythonInterpreter interpreter = new PythonInterpreter();

            interpreter.execfile(pathToJythonModule);

            String tempName = pathToJythonModule.substring(pathToJythonModule.lastIndexOf("/") + 1);

            tempName = tempName.substring(0, tempName.indexOf("."));

            System.out.println(tempName);
            String instanceName = tempName.toLowerCase();
            String javaClassName = tempName.substring(0, 1).toUpperCase() + tempName.substring(1);
            String objectDef = "=" + javaClassName + "()";
            interpreter.exec(instanceName + objectDef);
            Class JavaInterface = Class.forName(interfaceName);
            javaInt = interpreter.get(instanceName).__tojava__(JavaInterface);
        } catch (ClassNotFoundException ex) {
            game.beginConsole();
            this.dispose();
        }
        leikrGame = (LeikrEngine)javaInt;
        leikrGame.setCoreGame(game);
    }

    private void loadGroovyGame(String filePath) {
        groovyClassLoader = new GroovyClassLoader();
        try {
            groovyGameClass = groovyClassLoader.parseClass(new File(filePath + Leikr.GAME_NAME + ".groovy"));//loads the game code  
            Constructor[] cnst = groovyGameClass.getConstructors();//gets the constructos
            leikrGame = (LeikrEngine) cnst[0].newInstance();//instantiates based on first constructor
            leikrGame.setCoreGame(game);
        } catch (SecurityException | IllegalArgumentException | InvocationTargetException | InstantiationException | CompilationFailedException | IOException | IllegalAccessException ex) {
            game.beginConsole();
            this.dispose();
        } 
    }

    @Override
    public void show() {
        try {
            leikrGame.preCreate();
            leikrGame.create();
            Gdx.input.setInputProcessor(leikrGame.leikrControls);
        } catch (Exception e) {
            game.beginConsole();
            this.dispose();
        }
    }

    @Override
    public void render(float delta) {
        try {
            leikrGame.preRender();

            leikrGame.renderCamera();

            leikrGame.render(delta);

            leikrGame.render();

        } catch (Exception e) {
            game.beginConsole();
            this.dispose();
        }
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
    public final void dispose() {
        if (leikrGame != null) {
            leikrGame.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.ESCAPE) {
            game.beginConsole();
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

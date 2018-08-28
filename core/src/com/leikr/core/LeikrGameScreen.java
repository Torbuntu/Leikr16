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
import static com.leikr.core.ConsoleDirectory.Console.gameType;
import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.codehaus.groovy.control.CompilationFailedException;
import org.python.util.PythonInterpreter;

/**
 *
 * @author tor
 */
public class LeikrGameScreen implements Screen, InputProcessor {

    public static Leikr game;
    String fileName;

    GroovyClassLoader groovyClassLoader;
    Class groovyGameLoader;
    LeikrEngine leikrGame;

    ScriptEngineManager scriptManager;
    ScriptEngine scriptEngine;

    public LeikrGameScreen(Leikr game) throws IOException {
        LeikrGameScreen.game = game;
        scriptManager = new ScriptEngineManager();

        fileName = Console.fileName;
        String filePath = Leikr.ROOT_PATH + "ChipSpace/" + fileName + "/";//sets game path

        try {
            switch (gameType.toLowerCase()) {
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
                    loadJythonGame(filePath);
                    break;
                default:
                    loadGroovyGame(filePath);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            game.setScreen(new ConsoleScreen(game, e.getMessage() + String.format("%104s", "See host terminal output for more details.")));
            this.dispose();
        }

    }

    public void loadScalaGame(String filePath) {
//        Global g = new Global(new Settings());

//        Global.Run run = new g.Run();
        // assumes you have a Java list of file names
//        run.compile(JavaConversions.asScalaBuffer(fileNames).toList);
    }

    public void loadJavaGame(String filePath) {

        File f = new File(filePath);// Create file of Java game.
        try {
            // Create URL for loading the external files.
            URL[] cp = {f.toURI().toURL()};
            URLClassLoader urlCl = new URLClassLoader(cp);

            //Compile the Java source code.
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            String toCompile = java.io.File.separator + filePath + fileName + ".java";
            compiler.run(null, null, null, toCompile);

            //New instance
            leikrGame = (LeikrEngine) urlCl.loadClass(fileName).newInstance();
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(LeikrGameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadJythonGame(String filePath) {
        leikrGame = (LeikrEngine) getJythonObject("com.leikr.core.LeikrEngine", filePath + fileName + ".py");
    }

    public static Object getJythonObject(String interfaceName, String pathToJythonModule) {

        Object javaInt = null;
        PythonInterpreter interpreter = new PythonInterpreter();

        interpreter.execfile(pathToJythonModule);

        String tempName = pathToJythonModule.substring(pathToJythonModule.lastIndexOf("/") + 1);

        tempName = tempName.substring(0, tempName.indexOf("."));

        System.out.println(tempName);
        String instanceName = tempName.toLowerCase();
        String javaClassName = tempName.substring(0, 1).toUpperCase() + tempName.substring(1);
        String objectDef = "=" + javaClassName + "()";
        interpreter.exec(instanceName + objectDef);
        try {
            Class JavaInterface = Class.forName(interfaceName);
            javaInt = interpreter.get(instanceName).__tojava__(JavaInterface);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();  // Add logging here
        }

        return javaInt;
    }

    public void loadGroovyGame(String filePath) {
        groovyClassLoader = new GroovyClassLoader();
        try {
            groovyGameLoader = groovyClassLoader.parseClass(new File(filePath + fileName + ".groovy"));//loads the game code
        } catch (CompilationFailedException | IOException ex) {
            Logger.getLogger(LeikrGameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            leikrGame = (LeikrEngine) groovyGameLoader.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(LeikrGameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void show() {
        try {
            leikrGame.create();
            Gdx.input.setInputProcessor(leikrGame);
        } catch (Exception e) {
            e.printStackTrace();
            game.setScreen(new ConsoleScreen(game, e.getMessage() + String.format("%104s", "See host terminal output for more details.")));
            this.dispose();
        }
    }

    @Override
    public void render(float delta) {
        try {
            leikrGame.preRender();

            leikrGame.renderCamera();

            leikrGame.render();
        } catch (Exception e) {
            e.printStackTrace();
            game.setScreen(new ConsoleScreen(game, e.getMessage() + String.format("%104s", "See host terminal output for more details.")));
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

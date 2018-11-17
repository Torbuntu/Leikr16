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
package com.leikr.core.DesktopEnvironment;

import com.badlogic.gdx.Screen;
import com.leikr.core.Leikr;
import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor
 */
public class DesktopEnvironmentScreen implements Screen {

    LeikrDesktopEngine leikrDesktopEngine;
    Leikr game;
    private GroovyClassLoader groovyClassLoader;
    private Class groovyGameClass;

    public DesktopEnvironmentScreen(Leikr game) {
        this.game = game;
        String filePath = Leikr.ROOT_PATH + "OS/" + game.customSettings.userDesktop;//sets game path
        loadUserDesktop(filePath);
    }

    private void loadUserDesktop(String filePath) {
        groovyClassLoader = new GroovyClassLoader();
        try {
            groovyGameClass = groovyClassLoader.parseClass(new File(filePath + ".groovy"));//loads the game code   
            Constructor[] cnst = groovyGameClass.getConstructors();
            leikrDesktopEngine = (LeikrDesktopEngine) cnst[0].newInstance();
        } catch (InstantiationException | CompilationFailedException | IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            game.beginConsole();
            this.dispose();
        }
    }

    @Override
    public void show() {
        leikrDesktopEngine.preCreate(game);
        leikrDesktopEngine.create();
    }

    @Override
    public void render(float f) {
        leikrDesktopEngine.preRender();

        leikrDesktopEngine.render();

        leikrDesktopEngine.renderStageAndCursor();

    }

    @Override
    public void resize(int x, int y) {
        leikrDesktopEngine.update(x, y);
    }

    @Override
    public void pause() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        leikrDesktopEngine.dispose();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

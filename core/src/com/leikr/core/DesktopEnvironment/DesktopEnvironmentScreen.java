/*
 * Copyright 2018 .
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
import com.leikr.core.MapEditor.MapEditor;

/**
 *
 * @author tor
 */
public class DesktopEnvironmentScreen implements Screen{
    
    DesktopEnvironment desktopEnvironment;
    Leikr game;
    
    public DesktopEnvironmentScreen(Leikr game){
        this.game = game;
    }
    @Override
    public void show() {
        desktopEnvironment = new DesktopEnvironment(game);
    }

    @Override
    public void render(float f) {
        desktopEnvironment.renderDesktopEnvironment();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resize(int x, int y) {
        desktopEnvironment.updateDesktopEnvironment(x, y);
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
        desktopEnvironment.DisposeDesktopEnvironment();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

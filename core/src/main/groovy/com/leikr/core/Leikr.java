/*
 * Copyright 2018 torbuntu.
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

import com.leikr.core.System.CustomSettings;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.TitleScreen.TitleScreen;
import com.leikr.core.System.SystemMethods;

public class Leikr extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public static final float WIDTH = 320, HEIGHT = 240;  
    public static String gameName = "LeikrGame";
    public static String gameType = "groovy";

    // Environment Variables
    public static String ROOT_PATH;
    
    public CustomSettings customSettings;
    
    @Override
    public void create() {
        //Init new console
        batch = new SpriteBatch();
        font = new BitmapFont();
        
        if (!Gdx.files.external("Leikr/").exists() || !Gdx.files.external("Leikr/ChipSpace/").exists() || !Gdx.files.external("Leikr/OS/").exists()) {
            SystemMethods groovySystemMethods = new SystemMethods();            
            groovySystemMethods.initFileSystem();
        }
        
        ROOT_PATH = Gdx.files.getExternalStoragePath() + "Leikr/";
        
        customSettings = new CustomSettings();
        
        TitleScreen title = new TitleScreen(this);
        title.setInput();
        this.setScreen(title);

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
    
    
    public static void beginConsole(Leikr game){
        game.setScreen(new ConsoleScreen(game));
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
    

}

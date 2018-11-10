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


package com.leikr.core.System;

import com.leikr.core.Leikr;
import com.leikr.core.DesktopEnvironment.DesktopEnvironmentScreen;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.LeikrGameScreen;
import com.leikr.core.SoundEngine.SoundFxEditorScreen;
import com.leikr.core.SpriteEditor.SpriteEditorScreen;
import com.leikr.core.MapEditor.MapEditorScreen;
import groovy.io.FileType;
import java.io.File;
import java.lang.String;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SystemMethodsApi {
    public Leikr game;
    public ConsoleScreen screen;
    public repoHandler;
    public textHandler;
    public soundEngine;
      
    String ROOT_PATH = Leikr.ROOT_PATH;
    
    
    String sfx(wave, frequency, seconds, id){
        return soundEngine.writeAudioToDisk(wave, frequency, seconds, id);
    }
    
    String playsfx(id, dur){
        return soundEngine.playSound(id, dur);
    }
    
    
    void testsfx(wave, frequency, seconds){
        soundEngine.playNewAudio(wave, frequency, seconds);
    }
    
    
    void setBgColor(r, g, b){
        textHandler.setBgColor(r, g, b);
    }
    
    void setFontColor(r, g, b){
        textHandler.setFontColor(r, g, b);
    }
    
    void leikrSystemExit(){
        screen.dispose();
        System.exit(0);
    }
    
    void clearConsole(){
        screen.console.clearConsoleText();
    }
    
    Boolean pathExists(String path){
        Gdx.files.external(path).exists();
    }
    
    void startGame(){
        game.setScreen(new LeikrGameScreen(game));
        screen.dispose();
    }
    
    void startSpriteEditor(){
        game.setScreen(new SpriteEditorScreen(game));
        screen.dispose();
    }
    
    void startMapEditor(){
        game.setScreen(new MapEditorScreen(game));
        screen.dispose();
    }
    
    void startDesktop(){
        game.setScreen(new DesktopEnvironmentScreen(game));
        screen.dispose();
    }
    
    void startSoundEditor(){
        game.setScreen(new SoundFxEditorScreen(game));
        screen.dispose();
    }
    
    String initFileSystem(){        
        new AntBuilder().copy( todir: ROOT_PATH) {
            fileset( dir: Gdx.files.classpath("Leikr"));
        } 
        return "File system init.";
    }
    
}

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
    public repoHandler;
    public textHandler;
    public soundEngine;
      
    String ROOT_PATH = Leikr.ROOT_PATH;

    String sfx(wave, frequency, seconds, id){
        return soundEngine.writeAudioToDisk(wave, frequency.toInteger(), seconds.toInteger(), id.toInteger());
    }
    
    String playsfx(id, dur){
        return soundEngine.playSound(id.toInteger(), dur.toFloat());
    }
    
    
    void testsfx(wave, frequency, seconds){
        soundEngine.playNewAudio(wave, frequency.toInteger(), seconds.toInteger());
    }
    
    
    void setBgColor(r, g, b){
        textHandler.setBgColor(r, g, b);
    }
    
    void setFontColor(r, g, b){
        textHandler.setFontColor(r, g, b);
    }
    
    void leikrSystemExit(){
        soundEngine.disposeSoundEngine();
        System.exit(0);
    }
    
    void clearConsole(){
        textHandler.clearHistoryBuffer();
        textHandler.clearCommandBuffer();
    }
    
    Boolean pathExists(String path){
        Gdx.files.external(path).exists();
    }
    
    void startGame(){
        game.setScreen(new LeikrGameScreen(game));
    }
    
    void startSpriteEditor(){
        game.setScreen(new SpriteEditorScreen(game));
    }
    
    void startMapEditor(){
        game.setScreen(new MapEditorScreen(game));
    }
    
    void startDesktop(){
        game.setScreen(new DesktopEnvironmentScreen(game));
    }
    
    void startSoundEditor(){
        game.setScreen(new SoundFxEditorScreen(game));
    }
       
    String mnt(String from){
        new AntBuilder().copy( todir: ROOT_PATH+"/ChipSpace/"+from) {
            fileset( dir: ROOT_PATH+"/Download/"+from);
        }
        return "mounted "+from+" to ChipSpace from Downloads";
    }
    
    String newGame(String name, String type){
        new File(ROOT_PATH+"/ChipSpace/"+name).mkdir();
        switch(type.toLowerCase()){
        case "python":
        case "jython":
        case "py":
        case "jy":
            new AntBuilder().copy( file:Gdx.files.classpath("GameModels/JythonTemplate.py"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/"+name+".py");
            new AntBuilder().replace(file: ROOT_PATH+"/ChipSpace/"+name+"/"+name+".py", token: "GAME_NAME", value: name);        
            break;
        case "java":
            new AntBuilder().copy( file:Gdx.files.classpath("GameModels/JavaTemplate.java"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/"+name+".java");
            new AntBuilder().replace(file: ROOT_PATH+"/ChipSpace/"+name+"/"+name+".java", token: "GAME_NAME", value: name);
            break;
        case "groovy":
        default:
            //new File( RootFileSystem+"/ChipSpace/"+name+"/"+name+".groovy")
            new AntBuilder().copy( file:Gdx.files.classpath("GameModels/GroovyTemplate.groovy"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/"+name+".groovy");
            new AntBuilder().replace(file: ROOT_PATH+"/ChipSpace/"+name+"/"+name+".groovy", token: "GAME_NAME", value: name);
            //return "Not imnplemented yet";
            break;
        }
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/"+name+"_0.png");
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/"+name+"_1.png");
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/"+name+"_2.png");
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/"+name+"_3.png");
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/tmxTemplate.tmx"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/"+name+".tmx");
        new AntBuilder().replace(file: ROOT_PATH+"/ChipSpace/"+name+"/"+name+".tmx", token: "GAME_NAME", value: name);
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/Palette.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/Palette_0.png");
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/Palette.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/Palette_1.png");
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/Palette.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/Palette_2.png");
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/Palette.png"), tofile:ROOT_PATH+"/ChipSpace/"+name+"/Graphics/Palette_3.png");
        
        new File(ROOT_PATH+"/ChipSpace/"+name+"/Audio").mkdir();
        
        return "New game project `"+name+"` initialized with type `"+type+"`";
    }
    
    String initFileSystem(){        
        new AntBuilder().copy( todir: ROOT_PATH) {
            fileset( dir: Gdx.files.classpath("Leikr"));
        } 
        return "File system init.";
    }
    
    String restartSystem(){
        
        new AntBuilder().copy( todir: ROOT_PATH+"Backup") {
            fileset( dir: ROOT_PATH);
        }
       
        Gdx.files.external("Leikr/").deleteDirectory();
         
        initFileSystem();
        return "System files restored. Backup image created.";
    }
    
}

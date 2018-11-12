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

import groovy.io.FileType;
import java.io.File;
import java.lang.String;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.leikr.core.System.SystemMethodsApi;

public class Methods extends SystemMethodsApi {
    String SystemName = "Leikr 16";
    
    String locPath = "";

    String printSystemInfo(){
        "System Name: $SystemName";
    }
    
    void clear(){
        clearConsole();
    }
    
    void run(){
        startGame();
    }
    
    void run(app){
        switch(app){
            case "Map":
            case "MapEditor":
                startMapEditor();
                break;
            case "Sprite":
            case "SpriteEditor":
                startSpriteEditor();
                break;
            case "gui":
                startDesktop();
                break;
        }
    }
    
    void SpriteEditor(){
        startSpriteEditor();
    }
    
    void SoundEditor(){
        startSoundEditor();
    }
    
    void MapEditor(){
        startMapEditor();
    }
    
    void Desktop(){
        startDesktop();
    }
    
    String load(name){
        game.GAME_NAME = name;
        return "Game $name loaded. Currently set type $game.GAME_TYPE";
    }
    String load(name, type){
        game.GAME_NAME = name;
        game.GAME_TYPE = type;
        return "Game $name loaded. Game type set to $type.";
    }
    
    String setGameType(type){
        game.GAME_TYPE = type;
        return "Game type set: $type";
    }
    
    String cd(String directory){
        locPath += "/"+directory;
        return locPath;
    }
    
    String env(){
        return "Game Name: $game.GAME_NAME, Game Type: $game.GAME_TYPE, Current Directory: $locPath";
    }
    
    void exit(){
        leikrSystemExit();
    }
    
    String help(){
        return "Type `load` followed by the game you wish to load from the ChipSpace directory. Then type `run` to play. Type Exit to quit Leikr. More help coming soon...";
    }
    
    String setUserRepo(repoName){
        repoHandler.setUserRepo(repoName);
        return "User repository set: $repoName";
    }
    String setRepoType(repoType){
        repoHandler.setRepoType(repoType);
        return "Repository type set: $repoType";
    }
    
    String setRepoSettings(name, type){
        repoHandler.repoSettings(name, type);
        return "User repository set: $name, Repository type set: $type";
    }
    
    String lpm(action, item){
        if(action.equals("install")){
            return repoHandler.lpmInstall(item);
        }else{
            return "Action $action not recognized";
        }
    }
    
    String mkdir(def name){
        String result = "";
        if(!Gdx.files.isExternalStorageAvailable()){
            return "No permission to write to virtual drive";
        }
        if(!pathExists("Leikr/")){
            result += "No root file system detected. Initializing...   ";
            initFileSystem();
            result += "Directories initialized";
        }
        if(locPath.length() > 0){
            new File(ROOT_PATH+"/"+name).mkdir();

        }else{
            new File(ROOT_PATH+"/"+locPath+"/"+name).mkdir();

        }
        return result += "New directory `$name` successfully created.";
    }    
    
    String rm(def name){
        if(locPath.length() > 0){
            if(Gdx.files.external("Leikr/"+locPath+"/"+name).delete()){
                return "File `$name` successfully removed.";
            }else{
                return "There was a problem removing `$name`...";
            }
        }else{
            if(Gdx.files.external("Leikr/"+name).delete()){
                return "File `$name` successfully removed.";
            }else{
                return "There was a problem removing `$name`...";
            }
        }
        
    }
    
    String rmdir(def name){
        if(locPath.length() > 0){
            if(Gdx.files.external("Leikr/"+locPath+"/"+name).deleteDirectory()){
                return "Directory `$name` successfully removed.";
            }else{
                return "There was a problem removing `$name`...";
            }
        }else{
            if(Gdx.files.external("Leikr/"+name).deleteDirectory()){
                return "Directory `$name` successfully removed.";
            }else{
                return "There was a problem removing `$name`...";
            }
        }
        
    }
    
    // Update this to use the current directory after CD is implemented
    String ls(){
        String lsResult = "";
        FileHandle[] contents = Gdx.files.external("Leikr/"+locPath).list();
        for(FileHandle item : contents){
            lsResult += item.toString().replace("Leikr/"+locPath, "") + " ";
        }
        return lsResult;
    }
    
    String ls(String path){
        String lsResult = "";
        if(locPath.length()>0){
            FileHandle[] contents = Gdx.files.external("Leikr/"+locPath+"/"+path).list();
            for(FileHandle item : contents){
                lsResult += item.toString().replace("Leikr/"+locPath+"/"+path, "") + " ";
            }
        }else{
            FileHandle[] contents = Gdx.files.external("Leikr/"+path).list();
            for(FileHandle item : contents){
                lsResult += item.toString().replace("Leikr/"+path, "") + " ";
            }
        }
        
        return lsResult;
    }
    
    String make(String name, String type){
        return newGame(name, type);
    }
    
    String mount(String from){
        return mnt(from);
    }
    
}

new Methods();
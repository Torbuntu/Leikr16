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
    
    String cd(String directory){
        locPath += "/"+directory;
        return locPath;
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

new Methods();
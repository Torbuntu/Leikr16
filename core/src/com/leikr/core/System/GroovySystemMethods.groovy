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

package com.leikr.core.System;

import groovy.io.FileType;
import java.io.File;
import java.lang.String;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class GroovySystemMethods {
    String BiosVersion = "V0.0.1";
    String SystemName = "Leikr 16";
    String RootFileSystem = Gdx.files.getExternalStoragePath()+"Leikr";
    
    String locPath = "";
    
    String printSystemInfo(){
        "System Name: $SystemName, Bios Version: $BiosVersion ";
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
        if(!Gdx.files.external("Leikr/").exists()){
            result += "No root file system detected. Initializing...   ";
            initFileSystem();
            result += "Directories initialized";
        }
        if(locPath.length() > 0){
            new File(RootFileSystem+"/"+name).mkdir();

        }else{
            new File(RootFileSystem+"/"+locPath+"/"+name).mkdir();

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
    
    String lsPath(String path){
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
        new AntBuilder().copy( todir: RootFileSystem+"/ChipSpace/"+from) {
            fileset( dir: RootFileSystem+"/Download/"+from);
        }
        return "mounted "+from+" to ChipSpace from Downloads";
    }
    
    String newGame(String name, String type){
        new File(RootFileSystem+"/ChipSpace/"+name).mkdir();
        switch(type.toLowerCase()){
            case "python":
            case "jython":
            case "py":
            case "jy":
                new AntBuilder().copy( file:Gdx.files.classpath("GameModels/JythonTemplate.py"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".py");
                new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".py", token: "GAME_NAME", value: name);        
                break;
            case "java":
                new AntBuilder().copy( file:Gdx.files.classpath("GameModels/JavaTemplate.java"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".java");
                new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".java", token: "GAME_NAME", value: name);
                break;
            case "groovy":
            default:
                //new File( RootFileSystem+"/ChipSpace/"+name+"/"+name+".groovy")
                new AntBuilder().copy( file:Gdx.files.classpath("GameModels/GroovyTemplate.groovy"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".groovy");
                new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".groovy", token: "GAME_NAME", value: name);
                //return "Not imnplemented yet";
                break;
        }
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate_0.png"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".png");
        new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".png", token: "GAME_NAME", value: name);
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate_1.png"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".png");
        new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".png", token: "GAME_NAME", value: name);
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate_2.png"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".png");
        new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".png", token: "GAME_NAME", value: name);
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/spriteTemplate_3.png"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".png");
        new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".png", token: "GAME_NAME", value: name);
        
        new AntBuilder().copy( file:Gdx.files.classpath("GameModels/tmxTemplate.tmx"), tofile:RootFileSystem+"/ChipSpace/"+name+"/"+name+".tmx");
        new AntBuilder().replace(file: RootFileSystem+"/ChipSpace/"+name+"/"+name+".tmx", token: "GAME_NAME", value: name);
                
        
        return "New game project `"+name+"` initialized with type `"+type+"`";
    }
   
    
    String initFileSystem(){        
       new AntBuilder().copy( todir: RootFileSystem) {
            fileset( dir: Gdx.files.classpath("Leikr"));
        } 
        return "File system init.";
    }
    
    String restartSystem(){
        
        new AntBuilder().copy( todir: RootFileSystem+"Backup") {
            fileset( dir: RootFileSystem);
        }
       
        Gdx.files.external("Leikr/").deleteDirectory();
         
        initFileSystem();
        return "System files restored. Backup image created.";
    }
    
    
}

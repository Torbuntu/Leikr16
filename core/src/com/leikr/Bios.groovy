/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.leikr

import groovy.io.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


class Bios {
    String BiosVersion = "V0.0.1";
    String SystemName = "Leikr 16";
    String RootFileSystem = Gdx.files.getExternalStoragePath();
    
    
    String printSystemInfo(){
        "System Name: $SystemName, Bios Version: $BiosVersion ";
    }
    
    String mkdir(def name){
        String result = "";
        if(!Gdx.files.isExternalStorageAvailable()){
            return "No permission to write to virtual drive";
        }
        if(!Gdx.files.external("LeikrVirtualDrive/").exists()){
            result += "No root file system detected. Initializing...   ";
            new File(RootFileSystem+"LeikrVirtualDrive/").mkdir();
        }
        new File(RootFileSystem+"LeikrVirtualDrive/"+name).mkdir();
        return result += "New directory `$name` successfully created.";
    }    
    
    String rm(def name){
        if(Gdx.files.external("LeikrVirtualDrive/"+name).delete()){
            return "File `$name` successfully removed.";
        }else{
            return "There was a problem removing `$name`...";
        }
    }
    
    String rmdir(def name){
        if(Gdx.files.external("LeikrVirtualDrive/"+name).deleteDirectory()){
            return "Directory `$name` successfully removed.";
        }else{
            return "There was a problem removing `$name`...";
        }
    }
    
    // Update this to use the current directory after CD is implemented
    String ls(){
        String lsResult = ". ..";
        FileHandle[] contents = Gdx.files.external("LeikrVirtualDrive/").list();
        for(FileHandle item : contents){
            lsResult += item.toString().replace("LeikrVirtualDrive/", "") + " ";

        }
        return lsResult;
    }
    
    
}
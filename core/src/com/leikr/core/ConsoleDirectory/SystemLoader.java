/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.ConsoleDirectory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.leikr.core.GroovySystemMethods;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import java.io.File;
import java.util.Arrays;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor
 */
public class SystemLoader {
    
    final GroovyClassLoader classLoader;
    GroovyObject systemMethodsClass;

    GroovyScriptEngine groovyScriptEngine;
    Binding binding;

    // The class of Bios. Eventually replace the systemMethodsClass and biosObject
    GroovySystemMethods groovySystemMethods = new GroovySystemMethods();

    public SystemLoader() throws IOException, InstantiationException, IllegalAccessException {
        classLoader = new GroovyClassLoader();
        if (!Gdx.files.external("LeikrVirtualDrive/").exists() || !Gdx.files.external("LeikrVirtualDrive/ChipSpace/").exists()) {
            groovySystemMethods.initFileSystem();
        }
        systemMethodsClass = (GroovyObject) classLoader.parseClass(new File(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/Methods.groovy")).newInstance();
    }

    public String getBiosVersion() {
        return groovySystemMethods.getBiosVersion();
    }

    public Object runRegisteredMethod(String[] methodName) {
        Object result;

        switch (methodName[0]) {
            case "cd":
                if (methodName.length < 2) {
                    result = "~";
                    groovySystemMethods.setLocPath("");
                } else {
                    result = groovySystemMethods.cd(methodName[1]);

                }
                break;
            case "pwd":
                if (groovySystemMethods.getLocPath().length() < 1) {
                    result = "~";
                } else {
                    result = groovySystemMethods.getLocPath();
                }
                break;

            case "mkdir":
                result = groovySystemMethods.mkdir(methodName[1]);
                break;
            case "mnt":
                result = groovySystemMethods.mnt(methodName[1]);
                break;
            case "del":
            case "rm":
                if (methodName[1].equals("-rf")) {
                    result = groovySystemMethods.rmdir(methodName[2]);
                } else {
                    result = groovySystemMethods.rm(methodName[1]);
                }
                break;
            case "dir":
            case "ls":
                if (methodName.length > 1) {
                    result = groovySystemMethods.lsPath(methodName[1]);
                } else {
                    result = groovySystemMethods.ls();

                }
                break;
            case "exec":
                try {
                    GroovyObject tempObject = (GroovyObject) classLoader.parseClass(new File(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/Methods.groovy")).newInstance();
                    String[] args = Arrays.copyOfRange(methodName, 2, methodName.length);
                    result = tempObject.invokeMethod(methodName[1], args);
                } catch (IOException | IllegalAccessException | InstantiationException | CompilationFailedException e) {
                    result = String.format("`%s` is not a known method. ", methodName[1]) + e.getMessage();
                }

                break;
            case "initFileSystem":
                result = groovySystemMethods.restartSystem();
                break;
                
            case "new":
                result = groovySystemMethods.newGame(methodName[1], methodName[2]);
                break;
            default:
                try {
                    if (methodName.length == 1) {

                        result = systemMethodsClass.invokeMethod(methodName[0], null);

                    } else {
                        String[] args = Arrays.copyOfRange(methodName, 0, methodName.length);
                        result = systemMethodsClass.invokeMethod(methodName[0], args);
                    }
                } catch (Exception e) {
                    result = e.getMessage();
                }
                break;
        }
        return result.toString();
    }

}

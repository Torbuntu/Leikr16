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

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor This class is responsible for handling system level operations.
 * Such as File system management. It also manages the GroovyShell.
 */
public class SystemBios {

    final GroovyClassLoader classLoader;
    GroovyShell groovyShell;

    SystemMethods groovySystemMethods;

    Class cmClass;
    Constructor[] cmConstruct;
    GroovyObject customMethods;

    Binding binding;
    GroovyScriptEngine engine;

    public SystemBios() throws IOException, InstantiationException, IllegalAccessException {
        classLoader = new GroovyClassLoader();
        groovyShell = new GroovyShell();
        groovySystemMethods = new SystemMethods();

        if (!Gdx.files.external("Leikr/").exists() || !Gdx.files.external("Leikr/ChipSpace/").exists()) {
            groovySystemMethods.initFileSystem();
        }

        binding = new Binding();
        engine = new GroovyScriptEngine(Gdx.files.getExternalStoragePath() + "Leikr/OS/");
        try {
            customMethods = (GroovyObject) engine.run("Methods.groovy", binding);
        } catch (ResourceException | ScriptException ex) {
            Logger.getLogger(SystemBios.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String reloadCustomMethods() {
        try {
            customMethods = (GroovyObject) engine.run("Methods.groovy", binding);
            return "Custom methods reloaded.";
        } catch (ResourceException | ScriptException ex) {
            return "Custom Methods failed to reload... " + ex.getMessage();
        }
    }

    public String getBiosVersion() {
        return groovySystemMethods.getBiosVersion();
    }

    public Object runSystemMethod(String[] methodName) {
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
            case "initFileSystem":
                result = groovySystemMethods.restartSystem();
                break;

            case "./RCM":
                result = reloadCustomMethods();
                break;

            case "new":
                result = groovySystemMethods.newGame(methodName[1], methodName[2]);
                break;
            case "exec":
                try {
                    if (methodName.length == 2) {

                        result = customMethods.invokeMethod(methodName[1], null);

                    } else {
                        String[] args = Arrays.copyOfRange(methodName, 2, methodName.length);
                        result = customMethods.invokeMethod(methodName[1], args);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    result = "Execution of Method `" + methodName[0] + "` failed. The method may not exist.";
                }
                break;
            default:

                // default command and system commands do not exist. Try running groovy shell eval.
                String inputString = String.join(",", methodName).replaceAll(",", "");

                try {
                    result = groovyShell.evaluate(inputString).toString();
                } catch (CompilationFailedException e) {
                    System.out.println(e.toString());
                    result = "GroovyShell cannot evaluate input: " + inputString;
                }

                break;
        }
        return result.toString();
    }

}
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

import groovy.lang.GroovyObject;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.ConsoleDirectory.TextHandler;
import com.leikr.core.Leikr;
import com.leikr.core.RepoDirectory.RepoHandler;
import com.leikr.core.SoundEngine.SoundEngine;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tor This class is responsible for handling system level operations.
 * Such as File system management. It also manages the GroovyShell.
 */
public class LeikrSystem {

    final Leikr game;

    GroovyShell groovyShell;
    RepoHandler repoHandler;
    ConsoleScreen consoleScreen;
    SoundEngine soundEngine;
    TextHandler textHandler;

    Binding binding;
    GroovyObject customMethods;
    GroovyObject systemMethods;
    GroovyScriptEngine engine;
    GroovyScriptEngine sysEngine;

    public LeikrSystem(TextHandler textHandler) throws IOException, InstantiationException, IllegalAccessException {
        this.game = textHandler.game;
        this.textHandler = textHandler;

        soundEngine = new SoundEngine(this.game);
        repoHandler = new RepoHandler();
        groovyShell = new GroovyShell();

        binding = new Binding();
        engine = new GroovyScriptEngine(Gdx.files.getExternalStoragePath() + "Leikr/Programs");
        sysEngine = new GroovyScriptEngine(Gdx.files.getExternalStoragePath() + "Leikr/Programs");
        try {
            customMethods = (GroovyObject) engine.run("Methods.groovy", binding);
            systemMethods = (GroovyObject) sysEngine.run("SystemMethods.groovy", binding);
            systemMethods.setProperty("game", game);
            systemMethods.setProperty("repoHandler", repoHandler);
            systemMethods.setProperty("soundEngine", soundEngine);
            systemMethods.setProperty("textHandler", textHandler);
        } catch (ResourceException | ScriptException ex) {
            Logger.getLogger(LeikrSystem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String reloadMethods() {
        try {
            customMethods = (GroovyObject) engine.run("Methods.groovy", binding);
            systemMethods = (GroovyObject) sysEngine.run("SystemMethods.groovy", binding);
            systemMethods.setProperty("game", game);
            systemMethods.setProperty("repoHandler", repoHandler);
            systemMethods.setProperty("soundEngine", soundEngine);
            systemMethods.setProperty("textHandler", textHandler);
            return "Custom and System methods reloaded.";
        } catch (ResourceException | ScriptException ex) {
            return "Custom and System Methods failed to reload... " + ex.getMessage();
        }
    }

    public Object runSystemMethod(String[] inputList) {
        Object result;

        switch (inputList[0]) {
            case "reloadMethods":
                result = reloadMethods();
                break;
            case "exec":
                try {
                    if (inputList.length == 2) {
                        result = customMethods.invokeMethod(inputList[1], null);
                    } else {
                        String[] args = Arrays.copyOfRange(inputList, 2, inputList.length);
                        result = customMethods.invokeMethod(inputList[1], args);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    result = "Execution of Method `" + inputList[0] + "` failed. The method may not exist.";
                }
                break;
            default:
                try {
                    if (inputList.length == 1) {
                        result = systemMethods.invokeMethod(inputList[0], null);
                    } else {
                        String[] args = Arrays.copyOfRange(inputList, 1, inputList.length);
                        result = systemMethods.invokeMethod(inputList[0], args);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    result = "Execution of Method `" + inputList[0] + "` failed. The method may not exist. " + e.getMessage();
                }

                // default command and system commands do not exist. Try running groovy shell eval.
//                try {
//                    String inputString = String.join(",", inputList).replaceAll(",", "");
//                    result = groovyShell.evaluate(inputString).toString();
//                } catch (CompilationFailedException e) {
//                    System.out.println(e.toString());
//                    result = "GroovyShell cannot evaluate input: " + inputString;
//                }
                
                // No result returned for void system methods.
                if(result == null){
                    result = "";
                }
                break;
        }
        return result.toString();
    }

}

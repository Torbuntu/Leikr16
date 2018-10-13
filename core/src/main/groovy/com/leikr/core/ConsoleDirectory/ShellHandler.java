/*
 * Copyright 2018 torbuntu
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
package com.leikr.core.ConsoleDirectory;

import com.leikr.core.System.SystemBios;
import com.leikr.core.System.CustomSettings;
import com.leikr.core.DesktopEnvironment.DesktopEnvironmentScreen;
import com.leikr.core.Leikr;
import com.leikr.core.LeikrGameScreen;
import com.leikr.core.MapEditor.MapEditorScreen;
import com.leikr.core.RepoDirectory.RepoHandler;
import com.leikr.core.SoundEngine.SoundEngine;
import com.leikr.core.SoundEngine.SoundFxEditorScreen;
import com.leikr.core.SpriteEditor.SpriteEditorScreen;
import groovy.lang.GroovyClassLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tor
 */
public class ShellHandler {

    final Leikr game;
    ConsoleScreen consoleScreen;
    FontHandler fontHandler;
    SystemBios systemLoader;
    RepoHandler repoHandler;
    GroovyClassLoader groovyClassLoader;
    Class groovyGameLoader;
    CustomSettings customSettings;

    float fontRed;
    float fontGreen;
    float fontBlue;

    float bgRed;
    float bgGreen;
    float bgBlue;
    SoundEngine soundEngine;

    public ShellHandler(Leikr game, ConsoleScreen consoleScreen, FontHandler fontHandler) {
        this.game = game;
        this.consoleScreen = consoleScreen;
        this.fontHandler = fontHandler;
        repoHandler = new RepoHandler();
        soundEngine = new SoundEngine(this.game);
        try {
            systemLoader = new SystemBios();
        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            fontRed = game.customSettings.fontRed;
            fontGreen = game.customSettings.fontGreen;
            fontBlue = game.customSettings.fontBlue;

            bgRed = game.customSettings.bgRed;
            bgGreen = game.customSettings.bgGreen;
            bgBlue = game.customSettings.bgBlue;
        } catch (Exception e) {
            fontRed = 1;
            fontGreen = 1;
            fontBlue = 1;

            bgRed = 0;
            bgGreen = 0;
            bgBlue = 0;
            System.out.println("No custom settings file in OS: " + e.getMessage());
        }

    }

    private void setFontColor(String red, String green, String blue) {
        fontRed = Float.valueOf(red);
        fontGreen = Float.valueOf(green);
        fontBlue = Float.valueOf(blue);
    }

    private void setBgColor(String red, String green, String blue) {
        bgRed = Float.valueOf(red);
        bgGreen = Float.valueOf(green);
        bgBlue = Float.valueOf(blue);
    }

    // Handles the command input.
    public void handleInput(ArrayList<String> commandBuffer, ArrayList<String> historyBuffer) throws IOException {
        //parse the command buffer into a String.
        String inputString = String.join(",", commandBuffer).replaceAll(",", "");

        // Convert to list for switch checking.
        String[] inputList = inputString.split(" ");

        historyBuffer.add("~$" + inputString);

        String result = "";
        try {
            switch (inputList[0]) {
                case "":
                    //do nothing
                    break;
                case "load":
                    Leikr.gameName = inputList[1];
                    result = "File " + inputList[1] + " has been loaded";
                    if (inputList.length > 2) {
                        Leikr.gameType = inputList[2];
                        result += ". Game type " + inputList[2] + " set.";
                    }
                    break;

                case "setFontColor":
                    setFontColor(inputList[1], inputList[2], inputList[3]);

                    break;
                case "setBgColor":
                    setBgColor(inputList[1], inputList[2], inputList[3]);

                    break;
                case "help":
                    result = "Type `load` followed by the game you wish to load. Then type `run` to play. Type Exit to quit Leikr.";
                    break;
                case "close":
                case "exit": //close on exit command.
                    System.exit(0);
                    break;
                case "cls":
                case "clear":
                    fontHandler.clearHistoryBuffer();
                    fontHandler.clearCommandBuffer();
                    break;
                case "start":
                case "run":
                    game.setScreen(new LeikrGameScreen(game));
                    consoleScreen.dispose();
                    break;
                case "getLoadedGame":
                case "gameName":
                case "loadedGame":
                    result = Leikr.gameName;
                    break;
                case "getLoadedType":
                case "gameType":
                case "loadedType":
                    result = Leikr.gameType;
                    break;
                case "loadInfo":
                case "gameInfo":
                    result = "Name: " + Leikr.gameName + " Type: " + Leikr.gameType;
                    break;
                case "setInstrument":
                case "setinst":
                    soundEngine.setInstrument(Integer.parseInt(inputList[1]));
                    break;
                case "playinst":
                case "note":

                    soundEngine.playNote(Integer.parseInt(inputList[1]), Integer.parseInt(inputList[2]), Integer.parseInt(inputList[3]));

                    break;
                case "sfx":
                    result = soundEngine.writeAudioToDisk(inputList[1], Integer.parseInt(inputList[2]), Integer.parseInt(inputList[3]), Integer.parseInt(inputList[4]));

                    break;
                case "playsfx":
                    result = soundEngine.playSound(Integer.parseInt(inputList[1]), Float.parseFloat(inputList[2]));

                    break;
                case "testsfx":
                    soundEngine.playNewAudio(inputList[1], Integer.parseInt(inputList[2]), Integer.parseInt(inputList[3]));
                    break;
                case "./ME":
                case "MapEditor":
                    game.setScreen(new MapEditorScreen(game));
                    consoleScreen.dispose();
                    break;
                case "./DE":
                case "DesktopEnvironment":
                case "startx":
                    game.setScreen(new DesktopEnvironmentScreen(game));
                    consoleScreen.dispose();
                    break;
                case "./SPE":
                case "SpriteEditor":
                    game.setScreen(new SpriteEditorScreen(game));
                    consoleScreen.dispose();
                    break;
                case "./SFX":
                case "SoundFXEditor":
                    game.setScreen(new SoundFxEditorScreen(game));
                    consoleScreen.dispose();
                    break;
                case "setUserRepo":
                    repoHandler.setUserRepo(inputList[1]);
                    result = "User repository set to " + inputList[1];
                    break;
                case "setRepoType":
                    repoHandler.setRepoType(inputList[1]);
                    result = "Repository type set to " + inputList[1];
                    break;
                case "repoSettings":
                    repoHandler.repoSettings(inputList[1], inputList[2]);
                    result = "User repository set to " + inputList[1] + ". Repository type set to " + inputList[2];
                    break;
                case "lpm":
                    if (!inputList[1].equals("install") || !inputList[1].equals("update")) {
                        result = "lpm command `" + inputList[1] + "` not found.";
                        break;
                    }
                    switch (inputList[1]) {
                        case "install":
                            result = (inputList.length > 3) ? repoHandler.lpmInstall(inputList[2], inputList[3]) : repoHandler.lpmInstall(inputList[2]);
                            break;
                    }
                    break;
                default: //Default, command not recognized.

                    try {
                        result = (String) systemLoader.runSystemMethod(inputList);
                    } catch (Exception e) {
                        result = "System commands failed: " + e.getMessage();
                    }
                    break;
            }
        } catch (Exception e) {
            // return any error to the screen.
            historyBuffer.add(e.getMessage());
        }

        // If result, add to history.
        if (result.length() > 0) {
            historyBuffer.add(result);

        }

    }
}

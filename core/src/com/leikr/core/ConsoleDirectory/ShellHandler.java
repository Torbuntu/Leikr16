/*
 * To change this license header, choose License Headers inputString Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inputString the editor.
 */
package com.leikr.core.ConsoleDirectory;

import static com.leikr.core.ConsoleDirectory.Console.fileName;
import static com.leikr.core.ConsoleDirectory.Console.gameType;
import com.leikr.core.Leikr;
import com.leikr.core.LeikrGameScreen;
import com.leikr.core.MapEditor.MapEditorScreen;
import com.leikr.core.RepoDirectory.RepoHandler;
import com.leikr.core.SpriteEditor.SpriteEditorScreen;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor
 */
public class ShellHandler {

    final Leikr game;
    ConsoleScreen consoleScreen;
    FontHandler fontHandler;
    SystemLoader systemLoader;
    RepoHandler repoHandler;

    GroovyShell groovyShell;

    GroovyClassLoader groovyClassLoader;
    Class groovyGameLoader;
    CustomSettings customSettings;

    float fontRed;
    float fontGreen;
    float fontBlue;

    float bgRed;
    float bgGreen;
    float bgBlue;

    public ShellHandler(Leikr game, ConsoleScreen consoleScreen, FontHandler fontHandler) {
        this.game = game;
        this.consoleScreen = consoleScreen;
        this.fontHandler = fontHandler;
        groovyShell = new GroovyShell();
        repoHandler = new RepoHandler();

        try {
            systemLoader = new SystemLoader();
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

        String[] inputList = inputString.split(" ");

        historyBuffer.add("~$" + inputString);

        System.out.println("HBuffer: " + historyBuffer);

        //Default command not recognized.
        String notRecognized = "Command '" + inputString + "' is not recognized...";

        String result;

        //Convert to switch.
        switch (inputList[0]) {
            case "load":
                fileName = inputList[1];
                String out = "File " + inputList[1] + " has been loaded";
                if (inputList.length > 2) {
                    gameType = inputList[2];
                    out += ". Game type " + inputList[2] + " set.";
                }
                historyBuffer.add(out);
                break;
            case "gv":
                inputString = inputString.replaceFirst("gv ", "");
                try {
                    result = groovyShell.evaluate(inputString).toString();
                    historyBuffer.add(result);
                } catch (CompilationFailedException e) {
                    System.out.println(e.toString());
                    historyBuffer.add(notRecognized);
                }
                break;

            case "setFontColor":
                try {
                    setFontColor(inputList[1], inputList[2], inputList[3]);
                } catch (Exception e) {
                    historyBuffer.add(e.getMessage());
                }
                break;
            case "setBgColor":
                try {
                    setBgColor(inputList[1], inputList[2], inputList[3]);
                } catch (Exception e) {
                    historyBuffer.add(e.getMessage());
                }
                break;
            case "":
                //do nothing
                break;
            case "help":
                historyBuffer.add("Type `load` followed by the game you wish to load. Then type `run` to play. Type Exit to quit Leikr.");
                break;
            case "close":
            case "exit": //close on exit command.
                System.exit(0);
                break;
            case "cls":
            case "clear":
                fontHandler.clearCommandBuffer();
                fontHandler.clearHistoryBuffer();
                break;
            case "start":
            case "run":
                try {
                    game.setScreen(new LeikrGameScreen(game));

                } catch (Exception e) {
                    historyBuffer.add(e.getMessage());
                    return;
                }
                consoleScreen.dispose();
                break;
            case "./SPE":
            case "SpriteEditor":
                game.setScreen(new SpriteEditorScreen(game));
                consoleScreen.dispose();
                break;
            case "./ME":
            case "MapEditor":
                game.setScreen(new MapEditorScreen(game));
                consoleScreen.dispose();
                break;
            case "setUserRepo":
                repoHandler.setUserRepo(inputList[1]);
                result = "User repository set to " + inputList[1];
                historyBuffer.add(result);
                break;
            case "lpm":
                result = "lpm command " + inputList[1] + " not found.";
                switch (inputList[1]) {
                    case "install":
                        result = (inputList.length > 3) ? repoHandler.lpmInstall(inputList[2], inputList[3]) : repoHandler.lpmInstall(inputList[2]);
                        break;
                }
                historyBuffer.add(result);
                break;
            default: //Default, command not recognized.

                try {
                    result = (String) systemLoader.runRegisteredMethod(inputList);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    result = "";
                }

                if (result.length() > 0) {
                    historyBuffer.add(result);
                } else {
                    historyBuffer.add(notRecognized);
                }
                break;
        }
    }
}

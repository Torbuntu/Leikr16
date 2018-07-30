/*
 * To change this license header, choose License Headers inputString Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inputString the editor.
 */
package com.leikr.core.ConsoleDirectory;

import static com.leikr.core.ConsoleDirectory.Console.fileName;
import com.leikr.core.Leikr;
import com.leikr.core.LeikrGameScreen;
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

    GroovyShell groovyShell;

    float fontRed = 1;
    float fontGreen = 1;
    float fontBlue = 1;

    float bgRed = 0;
    float bgGreen = 0;
    float bgBlue = 0;

    public ShellHandler(Leikr game, ConsoleScreen consoleScreen, FontHandler fontHandler) {
        this.game = game;
        this.consoleScreen = consoleScreen;
        this.fontHandler = fontHandler;
        groovyShell = new GroovyShell();

        try {
            systemLoader = new SystemLoader();
        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
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
                historyBuffer.add("File " + inputList[1] + " has been loaded");
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
            case "exit": //close on exit command.
                System.exit(0);
                break;
            case "clear":
                fontHandler.clearCommandBuffer();
                fontHandler.clearHistoryBuffer();
                break;
            case "run":
                game.setScreen(new LeikrGameScreen(game));
                consoleScreen.dispose();
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

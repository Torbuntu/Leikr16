/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.ConsoleDirectory;

import java.util.ArrayList;

/**
 *
 * @author tor
 */
public class TextBuffer {

    ArrayList<String> command;
    ArrayList<String> history;
    ArrayList<String> sessionHistory;

    public TextBuffer() {
        command = new ArrayList<>();
        history = new ArrayList<>();
        sessionHistory = new ArrayList<>();
    }
    
    public String getCommandString(){
        return String.join(",", command).replaceAll(",", "");
    }
    
    public void performBackspace(){
        if (command.size() > 0) {
            command.remove(command.size() - 1);
        }
    }
    
    public void addCommand(char character){
         if ((int) character != 8 && (int) character != 10) {
            command.add(String.valueOf(character));
        }
    }

}

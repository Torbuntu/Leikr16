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

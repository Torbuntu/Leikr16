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
package com.leikr.core.CodeEditor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class CodeEditorInputProcessor  implements InputProcessor{

    final Leikr game;
    CodeEditor codeEditor;
    public CodeEditorInputProcessor(Leikr game, CodeEditor codeEditor){
        this.game = game;
        this.codeEditor = codeEditor;
    }
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            game.beginConsole();
            codeEditor.dispose();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

 

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }
    
}

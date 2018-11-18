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

import com.badlogic.gdx.Screen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class CodeEditorScreen implements Screen{
    
    final Leikr game;
    CodeEditor codeEditor;
    
    public CodeEditorScreen(Leikr game){
        this.game = game;
        codeEditor = new CodeEditor(game);
    }
    


    @Override
    public void render(float delta) {
        codeEditor.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        codeEditor.updateViewport(width, height);
    }

    @Override
    public void dispose() {
        codeEditor.dispose();
    }
        
    
    
    
    

    @Override
    public void show() {
    }
    
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
    
}

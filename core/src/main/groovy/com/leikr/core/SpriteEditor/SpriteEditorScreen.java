/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.SpriteEditor;

import com.badlogic.gdx.Screen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class SpriteEditorScreen implements Screen{
    Leikr game;
    
    SpriteEditor spriteEditor;
    
    public SpriteEditorScreen(Leikr game){
        this.game = game;
    }

    @Override
    public void show() {
        spriteEditor = new SpriteEditor(game, this);
    }

    @Override
    public void render(float delta) {
        spriteEditor.renderSpriteEditor(delta);
    }

    @Override
    public void resize(int width, int height) {
        spriteEditor.updateViewport(width, height);
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

    @Override
    public void dispose() {
        spriteEditor.disposeSpriteEditor();
    }
    
}

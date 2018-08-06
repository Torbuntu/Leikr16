/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.intro;

import com.badlogic.gdx.Screen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class IntroScreen implements Screen {

    Leikr game;
    Intro intro;
    
    public IntroScreen(Leikr game){
        this.game = game;
    }
    
    @Override
    public void show() {
        intro = new Intro(game, this);
    }

    @Override
    public void render(float delta) {
        intro.renderIntro(delta);
    }

    @Override
    public void resize(int width, int height) {
        intro.updateViewport(width, height);
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
        intro.disposeIntro();
    }
    
}

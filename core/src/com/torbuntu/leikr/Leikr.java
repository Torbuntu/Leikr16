package com.torbuntu.leikr;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import groovy.lang.GroovyShell;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Leikr extends ApplicationAdapter  {

	//Primary Console
	Console console;
	@Override
	public void create () {
		//Init new console
		console = new Console();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Update the console items
		console.renderConsole();
	}
	
	@Override
	public void dispose () {
		//dispose console items
		console.disposeConsole();
	}

	public void resize(int width, int height){
		//resize the viewport in console.
		console.updateViewport(width, height);
	}

}

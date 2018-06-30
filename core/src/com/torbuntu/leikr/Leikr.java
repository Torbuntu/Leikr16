package com.torbuntu.leikr;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

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

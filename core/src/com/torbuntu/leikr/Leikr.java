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

public class Leikr extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	Texture font;
	Camera camera;
	Viewport viewport;

	GroovyShell groovyShell = new GroovyShell();

	static final int WIDTH = 260;
	static final int HEIGHT = 160;

	ArrayList<String> commandBuffer = new ArrayList<>();
	ArrayList<String> historyBuffer = new ArrayList<>();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		font = new Texture("LeikrFontA.png");

		camera = new OrthographicCamera(260, 160);
		viewport = new FitViewport(260, 160, camera);
		camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);//Sets the camera to the correct position.
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);


		batch.begin();
		//batch.draw(img, 0,0);
		nDisplayBufferedString();

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void resize(int width, int height){
		viewport.update(width, height);
	}

	//writes the path pre-pending the command buffer.
	private void nWritePath(float carriage, float line){
		int X = 0;
		int Y = 0;
		// Set the variable test for evaluating the x and y position of the ASCII set.
		X = ((int)'~' % 16) * 8;
		Y = ((int)'~' / 16) * 8;
		batch.draw(font, carriage, line, X, Y,8,8);
		X = ((int)'>' % 16) * 8;
		Y = ((int)'>' / 16) * 8;
		batch.draw(font, carriage+8f, line, X, Y,8,8);
	}
	public float nDisplayHistoryString(float cg, float ln){
		int X = 0;
		int Y = 0;
		float carriage = cg;
		float line = ln;
		for(String item : historyBuffer){
			carriage = 0;
			for(char C : item.toCharArray()){
				if(carriage >= viewport.getWorldWidth()-8f){
					carriage = -1f;
					line -= 8f;
				}
				X = ((int)C%16)*8;
				Y = ((int)C/16)*8;
				batch.draw(font, carriage, line, X, Y,8,8);
				carriage +=8f;
			}
			line +=8f;
		}
		return line;
	}
	public void nDisplayBufferedString(){
		float carriage = -1f;
		float line = viewport.getWorldHeight()-8f;
		int X = 0;
		int Y = 0;

		String result = String.join(",", commandBuffer).replaceAll(",","");
		if(historyBuffer.size() > 0){
			line = nDisplayHistoryString(carriage, line);
		}
		System.out.println(line + ": line value");
		nWritePath(carriage, line);
		carriage += 16f;
		for(char C : result.toCharArray()){
			if(carriage >= viewport.getWorldWidth()-8f){
				carriage = -1f;
				line -= 8f;
			}
			X = ((int)C%16)*8;
			Y = ((int)C/16)*8;
			batch.draw(font, carriage, line, X, Y,8,8);
			carriage +=8f;
		}

		if(line >= viewport.getWorldHeight()){
			System.out.println(historyBuffer.remove(0));
		}
	}
	public void backspaceHandler(){
		if(commandBuffer.size() > 0){
			commandBuffer.remove(commandBuffer.size() - 1);
		}
	}

	// Handles the command input.
	public void shellHandler(){
		//parse the command buffer into a String.
		String in = String.join(",", commandBuffer).replaceAll(",","");
		historyBuffer.add("~>"+ in);

		System.out.println("HBuffer: "+historyBuffer);

		//Default command not recognized.
		String notRecognized = "Command '"+in+"' is not recognized...";

		String result;

		//Convert to switch.
		if(in.length() > 3 && in.substring(0,4).equals("echo")) { // Process echo command
			in = in.replaceFirst("echo ", "");
			historyBuffer.add(in);
		}
		else{
			switch (in){
				case "":
					//cursorPos[1] -= 8;
					//printPath();
					break;
				case "help":
					historyBuffer.add("There is no help here yet...");
					break;
				case "exit": //close on exit command.
					System.exit(0);
					break;
				default: //Default, command not recognized.
					try {
						result = groovyShell.evaluate(in).toString();
						System.out.println(result);
					}catch (Exception e){
						System.out.println(e.toString());
						result = "";
					}
					if(result.length() > 0){
						historyBuffer.add(result);
					}else {
						historyBuffer.add(notRecognized);
					}
					break;
			}
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.BACKSPACE){
			backspaceHandler();
		}
		switch (keycode){
			case Input.Keys.BACKSPACE:
				backspaceHandler();
				break;
			case Input.Keys.ENTER:
				shellHandler();
				commandBuffer.clear();
				break;
			default:
				break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		//If the character not backspace or enter.
		System.out.println((int)character);
		if((int)character != 8 && (int)character != 10){
			commandBuffer.add(String.valueOf(character));
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
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
}

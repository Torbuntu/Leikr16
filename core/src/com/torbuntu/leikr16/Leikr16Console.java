package com.torbuntu.leikr16;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.github.czyzby.lml.parser.impl.tag.listener.InputListenerLmlTag;
import javafx.scene.input.KeyCode;

import javax.xml.soap.Text;

import java.io.IOException;

import static com.badlogic.gdx.Input.Keys.ENTER;

public class Leikr16Console extends ApplicationAdapter {


	private Stage stage;
	private Skin skin;
	private Table container;
	private TextField input;
	private Label label;

	@Override
	public void create () {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new StretchViewport(256, 224));
		Gdx.input.setInputProcessor(stage);

		// Create the main container table. set fill parent to true. align top left.
		container = new Table();
		container.setFillParent(true);
		container.top().left();
		//container.setDebug(true);

		// Init label and add to table
		label = new Label("~ $  ", skin);
		label.setAlignment(Align.left);
		container.add(label).top().left();

		// init input textfield and add to table. Set focus to input textfield.
		input = new TextField("", skin);
		container.add(input).fill().expandX();
		stage.setKeyboardFocus(input);

		// Input listener.
		container.addListener(new InputListener(){
			public boolean keyDown(InputEvent event, int keycode){
				if(keycode == ENTER){

					// First command. Clears the screen.
					if(input.getText().equals("clear")){
						container.clearChildren();


					}
					if(input.getText().toLowerCase().equals("help")){
						//container.clearChildren();
						// New Row.
						container.row();
						Label help = new Label("HELP: ", skin);
						container.add(help).left().top();

						// New label added to table container
						Label output = new Label("There is no help yet.", skin);
						output.setWrap(true);
						container.add(output).left().top().fill().expandX();
					}
					if(input.getText().toLowerCase().equals("ls")){
						//container.clearChildren();
						// New Row.
						container.row();
						Label help = new Label("LS: ", skin);
						container.add(help).left().top();

						// New label added to table container
						Label output = new Label("This directory is empty.", skin);
						output.setWrap(true);
						container.add(output).left().top().fill().expandX();
					}

					if(input.getText().equals("love")){
						Runtime runtime = Runtime.getRuntime();     //getting Runtime object

						try
						{
							runtime.exec("love");        //opens "love2d" program
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}

						container.clearChildren();

						label = new Label("~ $  ", skin);
						input = new TextField("", skin);

						container.add(label);
						container.add(input).fill();
						stage.setKeyboardFocus(input);
					}

					else {
						// New Row.
						container.row();

						// New label added to table container
						Label newLabel = new Label(label.getText(), skin);
						container.add(newLabel).left().top();

						// disable old input textfield.
						input.setDisabled(true);

						// New input textfield init and add to table. Set focus to new textfield.
						input = new TextField("", skin);
						container.add(input).fill().expandX().top().left();
						stage.setKeyboardFocus(input);
					}

					// If the table is stretching bellow the view, delete the top row.
					if(container.getRows() >= 9){
						container.getChildren().first().remove();
						container.getChildren().first().remove();
					}

				}

				return true;
			}
		});


		ScrollPane scrollPane = new ScrollPane(container, skin);
		scrollPane.setFillParent(true);
		scrollPane.setOverscroll(false, false);

		stage.addActor(scrollPane);
	}

	// Render the stage.
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	// Update the stage viewport.
	@Override
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}




}
/* NOTES */
// BROKEN, Font needs to be reloaded from older HIERO app, since this one breaks the dang thing.
//TODO: Figure out a way to fix the HELP section adding a weird row. Figure out layout ...
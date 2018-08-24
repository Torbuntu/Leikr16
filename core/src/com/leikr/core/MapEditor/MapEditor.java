/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.MapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import static com.leikr.core.ConsoleDirectory.Console.fileName;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class MapEditor implements InputProcessor {

    Stage stage;
    Leikr game;
    Table table;

    Texture map;
    private final TiledMap tiledMap;
    private final TiledMapTileLayer tiledMapLayer;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;

    public MapEditor(Leikr game) {
        this.game = game;
        stage = new Stage(new FitViewport(Leikr.WIDTH, Leikr.HEIGHT));
        table = new Table();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());
        table.setPosition(0, 0);

        tiledMap = new TmxMapLoader().load(Leikr.ROOT_PATH + "ChipSpace/" + fileName + "/" + fileName + ".tmx");
        tiledMapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);

        stage.addActor(table);
        Gdx.input.setInputProcessor(this);
    }

    public void renderMapEditor() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        tiledMapRenderer.setView((OrthographicCamera) stage.getViewport().getCamera());
        tiledMapRenderer.render();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            game.setScreen(new ConsoleScreen(game));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}

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
package com.leikr.core.MapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class MapEditor implements InputProcessor {

    Leikr game;

    Texture map;
    private final TiledMap tiledMap;
    private final TiledMapTileLayer tiledMapLayer;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    
    ShapeRenderer renderer;
    private final FitViewport viewport;
    private final Camera camera;

    public MapEditor(Leikr game) {
        this.game = game;

        tiledMap = new TmxMapLoader().load(Leikr.ROOT_PATH + "ChipSpace/" + Leikr.gameName + "/" + Leikr.gameName + ".tmx");
        tiledMapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();
        renderer = new ShapeRenderer();
        
        Gdx.input.setInputProcessor(this);
    }

    public void renderMapEditor() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setProjectionMatrix(camera.combined);
        
        renderer.begin(ShapeRenderer.ShapeType.Filled);        
        renderer.setColor(.3f,.3f,.3f,1);
        renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
        renderer.end();
        
        tiledMapRenderer.setView((OrthographicCamera) camera);
        tiledMapRenderer.render();
    }
    
    public void updateMapEditor(int x, int y){
        viewport.update(x, y, true);
        camera.update();
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

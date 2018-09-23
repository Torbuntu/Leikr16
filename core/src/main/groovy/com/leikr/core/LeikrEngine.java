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
package com.leikr.core;

import com.leikr.core.Graphics.LeikrPalette;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Graphics.SpriteHandler;
import com.leikr.core.SoundEngine.SoundEngine;
import java.util.Random;
import com.leikr.core.LeikrControls.LeikrControls;
import static com.leikr.core.Leikr.gameName;

/**
 *
 * @author tor
 */
public class LeikrEngine {
    
    public static Leikr game;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Camera camera;
    Viewport viewport;
    Texture font;
    
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    TiledMapTileLayer tiledMapLayer;
    
    boolean useMap;
    
    public int screenWidth = (int) Leikr.WIDTH;
    public int screenHeight = (int) Leikr.HEIGHT;
    
    LeikrPalette leikrPalette;
    SpriteHandler spriteHandler;
    PaintBrush paintBrush;
    SoundEngine soundEngine;
    LeikrControls leikrControls;
    
    int fontWidth;
    int fontHeight;
    
    void preCreate() {
        game = LeikrGameScreen.game;
        batch = game.batch;
        shapeRenderer = new ShapeRenderer();
        
        spriteHandler = new SpriteHandler(game);
        paintBrush = new PaintBrush(shapeRenderer, game);
        leikrPalette = new LeikrPalette();
        soundEngine = new SoundEngine(game);
        leikrControls = new LeikrControls(game, this);
        
        viewport = new FitViewport(screenWidth, screenHeight);
        camera = viewport.getCamera();
        
        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "OS/" + game.customSettings.fontName));
        fontWidth = (int) game.customSettings.glyphWidth;
        fontHeight = (int) game.customSettings.glyphHeight;
        
        Controllers.addListener(leikrControls);
    }
    
    public void create() {        
    }
    
    public void preRender() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    
    public void renderCamera() {
        //camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        camera.update();
    }

    // Render methods used by the game scripts. 
    public void render() {
    }
    
    public void render(float delta) {
    }

    //Updates the view on resize in the Leikr main.
    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
    }
    
    public void setFont(String fontName, int width, int height) {
        font = new Texture(new FileHandle(Leikr.ROOT_PATH + "ChipSpace/" + gameName + "/" + fontName + ".png"));
        fontWidth = width;
        fontHeight = height;
    }

    // Map and Camera section
    public void loadMap() {
        useMap = true;
        tiledMap = new TmxMapLoader().load(Leikr.ROOT_PATH + "ChipSpace/" + gameName + "/" + gameName + ".tmx");
        tiledMapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);
    }
    
    public void drawMap() {
        if (tiledMapRenderer != null && useMap) {
            tiledMapRenderer.setView((OrthographicCamera) camera);
            tiledMapRenderer.render();
//            TextureRegion mpl = tiledMap.getTileSets().getTileSet("tileset").getTile(0).getTextureRegion();
        }
    }
    
    public void setMapSection(int row, int column) {
        //row is the row id, column is the column id.
        int width = row * 320;
        int height = column * 240;
        camera.position.x = width + 160;
        camera.position.y = height + 120;
    }
    
    public int getCameraX() {
        return (int) camera.position.x;
    }
    
    public int getCameraY() {
        return (int) camera.position.y;
    }
    
    public void setCamera(float x, float y) {
        camera.position.set(x, y, 0);
    }
    
    public int getScreenWidth() {
        return screenWidth;
    }
    
    public int getScreenHeight() {
        return screenHeight;
    }
    
    public int getMapWidth() {
        return tiledMap.getProperties().get("width", Integer.class);
    }
    
    public int getMapHeight() {
        return tiledMap.getProperties().get("height", Integer.class);
    }
    
    public int getCellTileId(float x, float y) {
//        System.out.println("X: " + x + " Y: " + y);
        try {
            if (x >= 0 && y >= 0 && x <= tiledMap.getProperties().get("width", Integer.class) - 1 && y <= tiledMap.getProperties().get("height", Integer.class) - 1) {
                return tiledMapLayer.getCell(Math.round(x), Math.round(y)).getTile().getId();
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }
    
    public void setCellTile(float x, float y, int newId) {
        if (tiledMapLayer.getCell((int) x, (int) y) != null) {
            tiledMapLayer.getCell((int) x, (int) y).setTile(tiledMap.getTileSets().getTile(newId));
        } else {
            TiledMapTileLayer.Cell newCell = new Cell();
            tiledMapLayer.setCell(Math.round(x), Math.round(y), newCell.setTile(tiledMap.getTileSets().getTile(newId)));
        }
    }
    
    public int getRandom(int range) {
        return new Random().nextInt(range);
    }

    // Drawing section
    public void drawText(String text, float x, float y, int color) {
        int fontX;
        int fontY;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        batch.begin();
        int id = leikrPalette.palette.get(color);
        batch.setColor(new Color(id));
        
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * fontWidth;
            fontY = ((int) C / 16) * fontHeight;
            batch.draw(font, x, y, fontX, fontY, fontWidth, fontHeight);
            x = x + 8;
        }
        batch.end();
    }
    
    public void sprite(int id, float x, float y) {
        spriteHandler.drawSprite(id, x, y);
    }
    
    public void sprite(int id, float x, float y, float scaleX, float scaleY) {
        spriteHandler.drawSprite(id, x, y, scaleX, scaleY);
    }
    
    public void bigSprite(int idtl, int idtr, int idbl, int idbr, float x, float y) {
        spriteHandler.drawBigSprite(idtl, idtr, idbl, idbr, x, y);
    }
    
    public void rect(float x, float y, float width, float height, int color, String type) {
        paintBrush.drawRect(x, y, width, height, color, type);
    }
    
    public void circle(float x, float y, float radius, int color, String type) {
        paintBrush.drawCircle(x, y, radius, color, type);
    }
    
    public void arc(float x, float y, float radius, float start, float degrees, int color, String type) {
        paintBrush.drawArc(x, y, radius, start, degrees, color, type);
    }
    
    public void line(float x, float y, float x2, float y2, int color) {
        paintBrush.drawLine(x, y, x2, y2, color);
    }
    
    public void color(int id, float x, float y) {
        paintBrush.drawColor(id, x, y);
    }
    
    public void color(int id, float x, float y, float width, float height) {
        paintBrush.drawColor(id, x, y, width, height);
    }
    
    public void drawPalette(float x, float y, float w, float h) {
        shapeRenderer.begin(ShapeType.Filled);
        for (int c : leikrPalette.palette) {
            shapeRenderer.setColor(new Color(c));
            shapeRenderer.rect(x, y, w, h);
            x += w;
        }
        shapeRenderer.end();
    }

    public Sound getSfx(int id) {
        return soundEngine.getSfx(id);
    }
    
    public void playSfx(Sound snd) {
        soundEngine.playSfx(snd);
    }

    public void playSfx(Sound snd, float vol) {
        soundEngine.playSfx(snd, vol);
    }

    public void playSfx(Sound snd, float vol, float pit, float pan) {
        soundEngine.playSfx(snd, vol, pit, pan);
    }

    // disposals
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        soundEngine.disposeSoundEngine();
    }
    
    public boolean key(String name) {
        return leikrControls.key(name);
    }
    
    public boolean button(String name) {
        return leikrControls.button(name);
    }
        
}

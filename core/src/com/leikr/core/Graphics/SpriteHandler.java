/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.leikr.core.ConsoleDirectory.Console;
import com.leikr.core.Leikr;
import com.leikr.core.LeikrEngine;
import com.leikr.core.LeikrGameScreen;
import java.util.HashMap;
import java.util.Map;
//import javafx.scene.paint.Color;

/**
 *
 * @author tor
 */
public class SpriteHandler {

    Leikr game;

    public static Texture spriteSheet;
    String fileName;
    TextureRegion[][] regions;
    public Map<Integer, TextureRegion> sprites;

    public SpriteHandler(Leikr game) {
        this.game = game;

        fileName = Console.fileName;
        String filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/" + fileName + "/";//sets game path
        spriteSheet = new Texture(filePath + fileName + ".png");

        regions = TextureRegion.split(spriteSheet, 8, 8);
        sprites = new HashMap<>();
        mapAllSprites();
    }
    
    void setSpriteRegion(Texture newRegion){
        regions = TextureRegion.split(newRegion, 8, 8);
        sprites = new HashMap<>();
        mapAllSprites();
    }

    //adds all of the split textures from regions to the sprites map for easier calling.
    void mapAllSprites() {
        int id = 0;
        for (int row = 0; row < regions.length; row++) {
            for (int col = 0; col < regions[row].length; col++) {
                sprites.put(id, regions[row][col]);
                id++;
            }
        }
    }
    
    public TextureRegion getSpriteByRegion(int x, int y){
        return regions[y][x];
    }
    
    public void drawSprite(int id, float x, float y) {
        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(sprites.get(id), x, y);
        game.batch.end();
    }

    public void drawSprite(int id, float x, float y, int scaleX, int scaleY) {
        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(sprites.get(id), x, y, scaleX, scaleY);
        game.batch.end();
    }
    
    public void drawBigSprite(int idtl, int idtr, int idbl, int idbr, float x, float y) {
        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(sprites.get(idbl), x, y);
        game.batch.draw(sprites.get(idbr), x+8, y);
        
        game.batch.draw(sprites.get(idtl), x, y+8);
        game.batch.draw(sprites.get(idtr), x+8, y+8);
        game.batch.end();
    }
    
}

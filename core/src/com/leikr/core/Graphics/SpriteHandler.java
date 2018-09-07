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
import com.leikr.core.Leikr;
import java.util.HashMap;
import java.util.Map;
import static com.leikr.core.Leikr.fileName;

/**
 *
 * @author tor
 */
public class SpriteHandler {

    Leikr game;

    public static Texture spriteSheet_0;
    public static Texture spriteSheet_1;
    public static Texture spriteSheet_2;
    public static Texture spriteSheet_3;

    TextureRegion[][] regions;
    public Map<Integer, TextureRegion> sprites;

    private Texture selectedSpriteSheet;

    public SpriteHandler(Leikr game) {
        this.game = game;

        String filePath = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/";//sets game path
        try {
            spriteSheet_0 = new Texture(filePath + fileName + "_0.png");
            spriteSheet_1 = new Texture(filePath + fileName + "_1.png");
            spriteSheet_2 = new Texture(filePath + fileName + "_2.png");
            spriteSheet_3 = new Texture(filePath + fileName + "_3.png");
            mapAllSprites();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            spriteSheet_0 = new Texture("Logo.png");
            spriteSheet_1 = new Texture("Logo.png");
            spriteSheet_2 = new Texture("Logo.png");
            spriteSheet_3 = new Texture("Logo.png");
            mapAllSprites();
        }
        selectedSpriteSheet = spriteSheet_0;
        mapAllSprites();
    }

    //adds all of the split textures from regions to the sprites map for easier calling.
    void mapAllSprites() {
        int id = 0;
        sprites = new HashMap<>();
        regions = TextureRegion.split(spriteSheet_0, 8, 8);
        id = addToSprites(regions, id);
        regions = TextureRegion.split(spriteSheet_1, 8, 8);
        id = addToSprites(regions, id);
        regions = TextureRegion.split(spriteSheet_2, 8, 8);
        id = addToSprites(regions, id);
        regions = TextureRegion.split(spriteSheet_3, 8, 8);
        addToSprites(regions, id);
    }

    int addToSprites(TextureRegion[][] region, int id) {
        for (int row = 0; row < regions.length; row++) {
            for (int col = 0; col < regions[row].length; col++) {
                sprites.put(id, regions[row][col]);
                id++;
            }
        }
        return id;
    }

    void setSpriteRegion(Texture newRegion) {
        regions = TextureRegion.split(newRegion, 8, 8);
        sprites = new HashMap<>();
        mapAllSprites();
    }

    public TextureRegion getSpriteByRegion(int x, int y) {
        return regions[y][x];
    }

    public void drawSprite(int id, float x, float y) {
        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(sprites.get(id), x, y);
        game.batch.end();
    }

    public void drawSprite(int id, float x, float y, float scaleX, float scaleY) {
        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(sprites.get(id), x, y, scaleX, scaleY);
        game.batch.end();
    }

    public void drawBigSprite(int idtl, int idtr, int idbl, int idbr, float x, float y) {
        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(sprites.get(idbl), x, y);
        game.batch.draw(sprites.get(idbr), x + 8, y);

        game.batch.draw(sprites.get(idtl), x, y + 8);
        game.batch.draw(sprites.get(idtr), x + 8, y + 8);
        game.batch.end();
    }

}

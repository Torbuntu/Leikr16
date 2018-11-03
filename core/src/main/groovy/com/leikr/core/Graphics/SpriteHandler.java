/*
 * Copyright 2018 torbuntu
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
package com.leikr.core.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.leikr.core.Leikr;
import java.util.HashMap;
import java.util.Map;

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

    public SpriteHandler(Leikr game) {
        this.game = game;

        String filePath = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + Leikr.GAME_NAME + "/Graphics/";//sets game path
        try {
            spriteSheet_0 = new Texture(filePath + Leikr.GAME_NAME + "_0.png");
            spriteSheet_1 = new Texture(filePath + Leikr.GAME_NAME + "_1.png");
            spriteSheet_2 = new Texture(filePath + Leikr.GAME_NAME + "_2.png");
            spriteSheet_3 = new Texture(filePath + Leikr.GAME_NAME + "_3.png");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            spriteSheet_0 = new Texture("Logo.png");
            spriteSheet_1 = new Texture("Logo.png");
            spriteSheet_2 = new Texture("Logo.png");
            spriteSheet_3 = new Texture("Logo.png");
        }
        mapAllSprites();
    }

    //adds all of the split textures from regions to the sprites map for easier calling.
    private void mapAllSprites() {
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
        for (TextureRegion[] region2 : regions) {
            for (TextureRegion region1 : region2) {
                sprites.put(id, region1);
                id++;
            }
        }
        return id;
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

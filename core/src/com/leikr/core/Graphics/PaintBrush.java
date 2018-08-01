/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.Graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class PaintBrush {
    ShapeRenderer shapeRenderer;
    Leikr game;
    LeikrPalette leikrPalette;
    public PaintBrush(ShapeRenderer sr, Leikr game){
        this.game = game;
        shapeRenderer = sr;
        leikrPalette = new LeikrPalette();
    }
    
    public void drawPalette(int x, int y, int w, int h) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int c : leikrPalette.palette) {
            shapeRenderer.setColor(new Color(c));
            shapeRenderer.rect(x, y, w, h);
            x += w;
        }
        shapeRenderer.end();
    }
    
    public void drawColor(int id, int x, int y){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        int color = leikrPalette.palette.get(id);
        shapeRenderer.setColor(new Color(color));
        shapeRenderer.rect(x, y, 5, 5);
        shapeRenderer.end();
    }
    
    public void drawRect(int x, int y, int width, int height, int color, String type){
        switch (type) {
            case "Filled":
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                break;
            case "Line":
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                break;
            default:
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                break;
        }
        
        
    }
    
}

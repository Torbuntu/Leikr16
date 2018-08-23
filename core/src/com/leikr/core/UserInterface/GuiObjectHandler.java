/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.UserInterface;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.Graphics.LeikrPalette;
import com.leikr.core.Leikr;
import static com.leikr.core.LeikrEngine.game;

/**
 *
 * @author tor
 */
public class GuiObjectHandler {

    SpriteBatch batch;
    ShapeRenderer shapes;
    Texture font;
    Viewport viewport;
    LeikrPalette pallete;

    public GuiObjectHandler(Leikr game, Viewport viewport) {
        this.batch = game.batch;
        this.viewport = viewport;
        font = new Texture("LeikrFontA.png");
        shapes = new ShapeRenderer();
        pallete = new LeikrPalette();
    }

    private void drawFont(String characters, int x, int y, int width) {
        int carriage = x;
        for (char C : characters.toCharArray()) {
            
            int X = ((int) C % 16) * 8;
            int Y = ((int) C / 16) * 8;
            batch.draw(font, carriage, y, X, Y, 8, 8);
            carriage += 8f;
        }
    }

    public void drawRectWindow(int x, int y, int width, int height, int windowClr, String text, int tx, int ty,int tclr, int borderwidth, int borderclr) {
        shapes.setProjectionMatrix(viewport.getCamera().combined);

        shapes.begin(ShapeType.Filled);
        shapes.setColor(new Color(pallete.palette.get(windowClr)));
        shapes.rect(x, y, width, height);
        shapes.end();
        shapes.begin(ShapeType.Line);
        shapes.setColor(new Color(pallete.palette.get(borderclr)));
        shapes.rect(x - borderwidth, y - borderwidth, width + borderwidth * 2, height + borderwidth * 2);
        shapes.end();

        batch.begin();
        batch.setColor(new Color(pallete.palette.get(tclr)));
        drawFont(text, tx, ty, width);
        
        batch.setColor(Color.WHITE);
        batch.end();
    }

}

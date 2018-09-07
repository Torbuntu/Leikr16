/*
 * Copyright 2018 .
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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class PaintBrush {

    ShapeRenderer shapeRenderer;
    Leikr game;
    public LeikrPalette leikrPalette;

    public PaintBrush(ShapeRenderer sr, Leikr game) {
        this.game = game;
        shapeRenderer = sr;
        leikrPalette = new LeikrPalette(game.customSettings.customPalette);
    }

    private ShapeType getType(String type) {
        switch (type.toLowerCase()) {
            case "filled":
                return ShapeType.Filled;
            case "line":
                return ShapeType.Line;
            case "point":
                return ShapeType.Point;
            default:
                return ShapeType.Line;
        }
    }

    public void drawPalette(float x, float y, float w, float h) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int c : leikrPalette.palette) {
            shapeRenderer.setColor(new Color(c));
            shapeRenderer.rect(x, y, w, h);
            x += w;
        }
        shapeRenderer.end();
    }

    public void drawColor(int id, float x, float y) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        int color = leikrPalette.palette.get(id);
        shapeRenderer.setColor(new Color(color));
        shapeRenderer.rect(x, y, 5, 5);
        shapeRenderer.end();
    }
    
    public void drawColor(int id, float x, float y, float width, float height) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        int color = leikrPalette.palette.get(id);
        shapeRenderer.setColor(new Color(color));
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    public void drawRect(float x, float y, float width, float height, int color, String type) {
        shapeRenderer.begin(getType(type));
        shapeRenderer.setColor(new Color(leikrPalette.palette.get(color)));
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    public void drawCircle(float x, float y, float radius, int color, String type) {
        shapeRenderer.begin(getType(type));
        shapeRenderer.setColor(new Color(leikrPalette.palette.get(color)));
        shapeRenderer.circle(x, y, radius);
        shapeRenderer.end();
    }

    public void drawArc(float x, float y, float radius, float start, float degrees, int color, String type) {
        shapeRenderer.begin(getType(type));
        shapeRenderer.setColor(new Color(leikrPalette.palette.get(color)));
        shapeRenderer.arc(x, y, radius, start, degrees);
        shapeRenderer.end();
    }

    public void drawLine(float x, float y, float x2, float y2, int color) {
        shapeRenderer.setAutoShapeType(true);

        shapeRenderer.begin();

        shapeRenderer.setColor(new Color(leikrPalette.palette.get(color)));
        shapeRenderer.line(x, y, x2, y2);
        shapeRenderer.end();
        shapeRenderer.setAutoShapeType(false);

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.SpriteEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static com.leikr.core.ConsoleDirectory.Console.fileName;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
class SpriteEditor implements InputProcessor {

    Leikr game;
    SpriteEditorScreen sriteEditorScreen;
    Viewport viewport;
    Camera camera;
    ShapeRenderer renderer;
    PaintBrush paintBrush;

    SpriteBatch batch;
    Pixmap pixmap;
    Texture texture;
    Texture saveIcon;
    Texture undoIcon;
    
    String filePath;
    Color drawColor;

    Vector2 coords;
    
    int saveIconXPos;
    int undoIconXPos;

    public SpriteEditor(Leikr game, SpriteEditorScreen speScreen) {
        this.game = game;
        batch = game.batch;
        sriteEditorScreen = speScreen;
        drawColor = Color.BLACK;

        coords = new Vector2();

        renderer = new ShapeRenderer();
        paintBrush = new PaintBrush(renderer, game);

        if (fileName == null) {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/LeikrGame/LeikrGame.png";//sets game path
        } else {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/" + fileName + "/" + fileName + ".png";//sets game path
        }
        pixmap = new Pixmap(new FileHandle(filePath));
        texture = new Texture(pixmap);
        saveIcon = new Texture("saveIcon.png");
        undoIcon = new Texture("undoIcon.png");
        
        
        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();
        
        
        saveIconXPos = (int)viewport.getWorldWidth()-18;
        undoIconXPos = (int)viewport.getWorldWidth()-8;
        Gdx.input.setInputProcessor(this);
    }

    public void renderSpriteEditor(float delta) {
        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        int count = 0;
        int color = 0;
        for (float item : paintBrush.leikrPalette.palette) {
            paintBrush.drawRect(count, 0, 8, 8, color, "filled");
            count += 10;
            color++;
        }

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(1, 10, texture.getWidth() + 2, texture.getHeight() + 2);
        renderer.end();

        batch.begin();

        if (texture != null) {
            batch.draw(texture, 2, 10);
        }
        
        if(saveIcon != null){
            batch.draw(saveIcon, saveIconXPos, 0);
        }
        if(undoIcon != null){
            batch.draw(undoIcon, undoIconXPos, 0);
        }

        batch.end();

    }

    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
    }

    public void disposeSpriteEditor() {
        renderer.dispose();
        game.batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            game.setScreen(new ConsoleScreen(game));
            return false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public void drawPixelsOnTouch(int screenX, int screenY, int button) {
        viewport.unproject(coords.set(screenX, screenY));
            int graphicsY = (int) (camera.viewportHeight - (coords.y));
            
            if(coords.x >= saveIconXPos && coords.x <= saveIconXPos +8 && coords.y <= 8){
                System.out.println("Save Pressed");                
            }
            if(coords.x >= undoIconXPos && coords.x <= undoIconXPos +8 && coords.y <= 8){
                System.out.println("Undo pressed");
            }

        if (button == 0) {
            pixmap.setColor(drawColor);
            pixmap.drawPixel((int) coords.x - 2, graphicsY - 22);
            texture.draw(pixmap, 0, 0);
        } else if (button == 1) {
            if (coords.x >= 1 && coords.x <= 10) {
                drawColor.set(paintBrush.leikrPalette.palette.get(0));
            }
            if (coords.x >= 11 && coords.x <= 20) {
                drawColor.set(paintBrush.leikrPalette.palette.get(1));
            }
            if (coords.x >= 21 && coords.x <= 30) {
                drawColor.set(paintBrush.leikrPalette.palette.get(2));
            }
            if (coords.x >= 31 && coords.x <= 40) {
                drawColor.set(paintBrush.leikrPalette.palette.get(3));
            }
            if (coords.x >= 41 && coords.x <= 50) {
                drawColor.set(paintBrush.leikrPalette.palette.get(4));
            }
            if (coords.x >= 51 && coords.x <= 60) {
                drawColor.set(paintBrush.leikrPalette.palette.get(5));
            }
            if (coords.x >= 61 && coords.x <= 70) {
                drawColor.set(paintBrush.leikrPalette.palette.get(6));
            }
            if (coords.x >= 71 && coords.x <= 80) {
                drawColor.set(paintBrush.leikrPalette.palette.get(7));
            }
            if (coords.x >= 81 && coords.x <= 90) {
                drawColor.set(paintBrush.leikrPalette.palette.get(8));
            }
            if (coords.x >= 91 && coords.x <= 100) {
                drawColor.set(paintBrush.leikrPalette.palette.get(9));
            }
            if (coords.x >= 101 && coords.x <= 110) {
                drawColor.set(paintBrush.leikrPalette.palette.get(10));
            }
            if (coords.x >= 111 && coords.x <= 120) {
                drawColor.set(paintBrush.leikrPalette.palette.get(11));
            }
            if (coords.x >= 121 && coords.x <= 130) {
                drawColor.set(paintBrush.leikrPalette.palette.get(12));
            }
            if (coords.x >= 131 && coords.x <= 140) {
                drawColor.set(paintBrush.leikrPalette.palette.get(13));
            }
            if (coords.x >= 141 && coords.x <= 150) {
                drawColor.set(paintBrush.leikrPalette.palette.get(14));
            }
            if (coords.x >= 151 && coords.x <= 160) {
                drawColor.set(paintBrush.leikrPalette.palette.get(15));
            }
            //Color color = new Color(ScreenUtils.getFrameBufferPixmap(0, 0, (int) camera.viewportWidth, (int) camera.viewportHeight).getPixel((int) coords.x, graphicsY));
        }
    }

    public void drawPixelsOnTouch(int screenX, int screenY) {
        viewport.unproject(coords.set(screenX, screenY));

        int graphicsY = (int) (camera.viewportHeight - (coords.y));
        pixmap.setColor(drawColor);
        pixmap.drawPixel((int) coords.x - 2, graphicsY - 22);
        texture.draw(pixmap, 0, 0);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        drawPixelsOnTouch(screenX, screenY, button);
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        drawPixelsOnTouch(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

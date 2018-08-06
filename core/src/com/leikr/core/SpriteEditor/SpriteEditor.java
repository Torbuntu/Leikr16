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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static com.leikr.core.ConsoleDirectory.Console.fileName;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Graphics.LeikrPalette;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Leikr;
import static com.leikr.core.LeikrEngine.game;
import java.util.HashMap;
import java.util.Map;

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
    String filePath;
    Color drawColor;

    public SpriteEditor(Leikr game, SpriteEditorScreen speScreen) {
        this.game = game;
        batch = game.batch;
        sriteEditorScreen = speScreen;
        drawColor = Color.BLACK;

        renderer = new ShapeRenderer();
        paintBrush = new PaintBrush(renderer, game);

        if (fileName == null) {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/LeikrGame/LeikrGame.png";//sets game path
        } else {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/" + fileName + "/" + fileName + ".png";//sets game path
        }
        pixmap = new Pixmap(new FileHandle(filePath));
        texture = new Texture(pixmap);
        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        
        System.out.println("X: " + coords.x);
        System.out.println("Y: " + coords.y);

        if (button == 0) {
            int graphicsY = (int) (camera.viewportHeight - coords.y);
            pixmap.setColor(drawColor);
            pixmap.drawPixel((int) coords.x, graphicsY);
            texture.draw(pixmap, 0, 0);            
        } else if (button == 1) {
            switch ((int) coords.x) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    System.out.println("transparent");
                    drawColor.set(paintBrush.leikrPalette.palette.get(0));
                    break;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                    System.out.println("light blue");
                    drawColor.set(paintBrush.leikrPalette.palette.get(1));
                    break;
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                    System.out.println("Dark blue");
                    drawColor.set(paintBrush.leikrPalette.palette.get(2));
                    break;
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                    drawColor.set(paintBrush.leikrPalette.palette.get(3));
                    break;
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                    drawColor.set(paintBrush.leikrPalette.palette.get(4));
                    break;

            }
            //Color color = new Color(ScreenUtils.getFrameBufferPixmap(0, 0, (int) camera.viewportWidth, (int) camera.viewportHeight).getPixel((int) coords.x, graphicsY));
        }
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

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

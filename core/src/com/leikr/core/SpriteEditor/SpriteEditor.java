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
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static com.leikr.core.ConsoleDirectory.Console.fileName;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Graphics.SpriteHandler;
import com.leikr.core.Leikr;

/**
 *
 * @author tor TODO: Rework this whole file once it is 'feature complete '
 */
class SpriteEditor implements InputProcessor {

    Leikr game;
    SpriteEditorScreen sriteEditorScreen;
    Viewport viewport;
    Camera camera;
    ShapeRenderer renderer;
    PaintBrush paintBrush;
    Texture font;

    SpriteBatch batch;
    Pixmap pixmap;
    Pixmap zoomPixmap;

    Texture texture;
    Texture zoomTexture;

    Texture saveIcon;
    Texture undoIcon;

    String filePath;
    Color drawColor;

    Vector2 coords;
    int graphicsY;

    int saveIconXPos;
    int undoIconXPos;

    int spriteId = 0;
    int spriteIdX = 0;
    int spriteIdY = 0;

    int mainBoxWidth;
    int mainBoxHeight;

    int actualX;
    int actualY;

    int zoomX;
    int zoomY;
    int count = 0;
    int color = 0;

    public SpriteEditor(Leikr game, SpriteEditorScreen speScreen) {
        this.game = game;
        batch = game.batch;
        sriteEditorScreen = speScreen;
        drawColor = Color.BLACK;
        coords = new Vector2();
        graphicsY = 0;
        font = new Texture("LeikrFontA.png");

        renderer = new ShapeRenderer();
        paintBrush = new PaintBrush(renderer, game);

        if (fileName == null) {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/LeikrGame/LeikrGame.png";//sets game path
        } else {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/" + fileName + "/" + fileName + ".png";//sets game path
        }

        pixmap = new Pixmap(new FileHandle(filePath));
        texture = new Texture(pixmap);

        zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        zoomTexture = new Texture(zoomPixmap);

        actualX = 0;
        actualY = 0;
        zoomX = 0;
        zoomY = 0;

        mainBoxWidth = texture.getWidth() + 2;
        mainBoxHeight = texture.getHeight() + 2;

        saveIcon = new Texture("saveIcon.png");
        undoIcon = new Texture("undoIcon.png");

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();

        saveIconXPos = (int) viewport.getWorldWidth() - 18;
        undoIconXPos = (int) viewport.getWorldWidth() - 8;
        Gdx.input.setInputProcessor(this);
    }

    public void renderSpriteEditor(float delta) {
        Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);

        count = 0;
        color = 0;
        for (float item : paintBrush.leikrPalette.palette) {
            paintBrush.drawRect(count, (int) viewport.getWorldHeight() - 8, 8, 8, color, "filled");
            count += 10;
            color++;
        }

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(7, 7, mainBoxWidth, mainBoxHeight);
        renderer.rect(143, 7, 66, 66);
        renderer.end();

        batch.begin();

        //main sprite sheet
        if (texture != null) {
            batch.draw(texture, 8, 8);
        }
        if (zoomTexture != null) {
            batch.draw(zoomTexture, 144, 8, 64, 64);
        }

        drawText();

        if (saveIcon != null) {
            batch.draw(saveIcon, saveIconXPos, 0);
        }
        if (undoIcon != null) {
            batch.draw(undoIcon, undoIconXPos, 0);
        }

        batch.end();

    }

    public void drawText() {
        int fontX;
        int fontY;
        int x = 140;
        int y = 80;
        // Set the variable test for evaluating the x and y position of the ASCII set.
        String text = "Sprite ID: " + spriteId;
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * 8;
            fontY = ((int) C / 16) * 8;
            batch.draw(font, x, y, fontX, fontY, 8, 8);
            x = x + 8;
        }
    }

    public void updateViewport(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
    }

    public void disposeSpriteEditor() {
        renderer.dispose();
        game.batch.dispose();
    }

    public void savePixmapImage() {
        PixmapIO.writePNG(new FileHandle(filePath), pixmap);
    }

    public void undoRecentEdits() {
        pixmap = new Pixmap(new FileHandle(filePath));
        zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        texture = new Texture(pixmap);
    }

    public void drawSelectedPixmapToMain() {
        zoomPixmap.setColor(drawColor);
        zoomPixmap.drawPixel(zoomX, zoomY);
        zoomTexture.draw(zoomPixmap, 0, 0);

        pixmap.drawPixmap(zoomPixmap, spriteIdX * 8, spriteIdY * 8, 0, 0, 8, 8);

        pixmap.setColor(drawColor);
        pixmap.drawPixel(actualX, actualY);
        texture.draw(pixmap, 0, 0);
    }

    public void setDrawingCoords() {
        graphicsY = (int) (camera.viewportHeight - (coords.y));

        actualX = (int) (coords.x - 8);
        actualY = (int) (graphicsY - 104);

        zoomX = (actualX / 8) - 17;
        zoomY = (actualY / 8) - 8;
    }

    public void drawPixelsOnTouch(int button) {

        if (button == 2) {
            spriteIdX = (int) (actualX) / 8;
            spriteIdY = (actualY) / 8;
            spriteId = ((spriteIdY * 16) + (spriteIdX));

            zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
            zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, 8, 8, 0, 0, 8, 8);
            zoomTexture.draw(zoomPixmap, 0, 0);
        }
        if (coords.x >= saveIconXPos && coords.x <= saveIconXPos + 8 && coords.y <= 8) {
            System.out.println("Save Pressed");
            savePixmapImage();
        }
        if (coords.x >= undoIconXPos && coords.x <= undoIconXPos + 8 && coords.y <= 8) {
            System.out.println("Undo pressed");
            undoRecentEdits();
        }

        if (button == 0) {
            drawSelectedPixmapToMain();
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
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(coords.set(screenX, screenY));
        setDrawingCoords();
        drawPixelsOnTouch(button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        viewport.unproject(coords.set(screenX, screenY));
        setDrawingCoords();
        drawSelectedPixmapToMain();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            game.setScreen(new ConsoleScreen(game));
            return false;
        }
        if (keycode == Input.Keys.RIGHT && spriteId < 15) {
            spriteId++;
            if (spriteIdX < 15) {
                spriteIdX++;

            }
        }
        if (keycode == Input.Keys.LEFT && spriteId > 0) {
            spriteId--;
            spriteIdX--;
        }

        if (keycode == Input.Keys.UP) {
            if (spriteId >= 16) {
                spriteId -= 16;
                spriteIdY--;
            } else {
                spriteId = 0;
                if (spriteIdY > 0) {
                    spriteIdY--;

                }
            }
        }
        if (keycode == Input.Keys.DOWN) {

            if (spriteId < 230) {
                spriteId += 16;

            }
            if (spriteIdY < 15) {
                spriteIdY++;
            }
        }

        if (spriteId == 0) {
            spriteIdX = 0;
            spriteIdY = 0;
        }

        System.out.println("X: " + spriteIdX + " Y: " + spriteIdY + " ID:" + spriteId);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
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

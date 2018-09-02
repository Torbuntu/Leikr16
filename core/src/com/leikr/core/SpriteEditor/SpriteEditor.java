/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.SpriteEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static com.leikr.core.ConsoleDirectory.Console.fileName;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Graphics.PaintBrush;
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
    Texture cursor;

    SpriteBatch batch;
    Pixmap pixmap;
    Pixmap zoomPixmap;

    Texture texture;
    Texture zoomTexture;

    String filePath;
    Color drawColor;

    Vector2 coords;
    Vector2 cursorCoords;
    int graphicsY;

    int saveIconXPos;
    int undoIconXPos;
    int eraserIconPos;

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

    int cursorX = 0;
    int cursorY = 0;

    boolean exitDialog = false;

    Texture saveIcon;
    Texture undoIcon;
    Texture eraserIcon;
    private final TextureRegionDrawable saveIconDrawable;
    private final ImageButton saveIconButton;

    private final TextureRegionDrawable undoIconDrawable;
    private final ImageButton undoIconButton;
    private final TextureRegionDrawable eraserIconDrawable;
    private final ImageButton eraserIconButton;
    Stage stage;

    Texture okIcon;
    TextureRegionDrawable okIconDrawable;
    ImageButton okIconButton;

    Stage confirmExitStage;
    private final Texture noIcon;
    private final TextureRegionDrawable noIconDrawable;
    private final ImageButton noIconButton;

    public SpriteEditor(Leikr game, SpriteEditorScreen speScreen) {
        this.game = game;
        batch = game.batch;
        sriteEditorScreen = speScreen;
        drawColor = Color.BLACK;
        coords = new Vector2();
        cursorCoords = new Vector2();
        graphicsY = 0;
        font = new Texture("LeikrFontA.png");
        cursor = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/Cursor.png"));

        renderer = new ShapeRenderer();
        paintBrush = new PaintBrush(renderer, game);

        if (fileName == null) {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/LeikrGame/LeikrGame.png";//sets game path
        } else {
            filePath = Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/ChipSpace/" + fileName + "/" + fileName + ".png";//sets game path
        }

        pixmap = new Pixmap(new FileHandle(filePath));
        pixmap.setBlending(Pixmap.Blending.None);
        texture = new Texture(pixmap);

        zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        zoomPixmap.setBlending(Pixmap.Blending.None);
        zoomTexture = new Texture(zoomPixmap);

        zoomPixmap.drawPixmap(pixmap, 0, 0, 8, 8, 0, 0, 8, 8);
        zoomTexture.draw(zoomPixmap, 0, 0);

        actualX = 0;
        actualY = 0;
        zoomX = 0;
        zoomY = 0;

        mainBoxWidth = texture.getWidth() + 2;
        mainBoxHeight = texture.getHeight() + 2;

        viewport = new FitViewport(Leikr.WIDTH, Leikr.HEIGHT);
        camera = viewport.getCamera();

        stage = new Stage(viewport);

        eraserIconPos = (int) viewport.getWorldWidth() - 28;
        saveIconXPos = (int) viewport.getWorldWidth() - 18;
        undoIconXPos = (int) viewport.getWorldWidth() - 8;

        saveIcon = new Texture("saveIcon.png");
        saveIconDrawable = new TextureRegionDrawable(new TextureRegion(saveIcon));
        saveIconButton = new ImageButton(saveIconDrawable);
        saveIconButton.setPosition(saveIconXPos, 0);
        saveIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                savePixmapImage();
                System.out.println("Save clicked!");
            }
        });

        undoIcon = new Texture("undoIcon.png");
        undoIconDrawable = new TextureRegionDrawable(new TextureRegion(undoIcon));
        undoIconButton = new ImageButton(undoIconDrawable);
        undoIconButton.setPosition(undoIconXPos, 0);
        undoIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                undoRecentEdits();
                System.out.println("Undo clicked!");
            }
        });

        eraserIcon = new Texture("eraserIcon.png");
        eraserIconDrawable = new TextureRegionDrawable(new TextureRegion(eraserIcon));
        eraserIconButton = new ImageButton(eraserIconDrawable);
        eraserIconButton.setPosition(eraserIconPos, 0);
        eraserIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                drawColor.set(0, 0, 0, -1);
                System.out.println("Eraser clicked!");
            }
        });

        stage.addActor(saveIconButton);
        stage.addActor(undoIconButton);
        stage.addActor(eraserIconButton);

        confirmExitStage = new Stage(viewport);
        okIcon = new Texture("ok.png");
        okIconDrawable = new TextureRegionDrawable(new TextureRegion(okIcon));
        okIconButton = new ImageButton(okIconDrawable);
        okIconButton.setPosition(viewport.getWorldWidth() / 2 - 48, viewport.getWorldHeight() / 2 + 16);
        okIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (exitDialog) {
                    savePixmapImage();
                    game.setScreen(new ConsoleScreen(game));
                    Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
                    System.out.println("Exit clicked!");
                }

            }
        });

        noIcon = new Texture("no.png");
        noIconDrawable = new TextureRegionDrawable(new TextureRegion(noIcon));
        noIconButton = new ImageButton(noIconDrawable);
        noIconButton.setPosition(viewport.getWorldWidth() / 2 - 24, viewport.getWorldHeight() / 2 + 16);
        noIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (exitDialog) {
                    exitDialog = false;
                    System.out.println("Stay clicked!");
                }
            }
        });

        confirmExitStage.addActor(okIconButton);
        confirmExitStage.addActor(noIconButton);

        Pixmap pm = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/HideCursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();
//        Gdx.input.setInputProcessor(this);

        // input processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(confirmExitStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void renderSpriteEditor(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //background gray
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(.3f, .3f, .3f, 1);
        renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
        renderer.end();

        batch.setProjectionMatrix(camera.combined);

        count = 0;
        color = 0;
        for (float item : paintBrush.leikrPalette.palette) {
            paintBrush.drawRect(count, (int) viewport.getWorldHeight() - 8, 8, 8, color, "filled");
            count += 10;
            color++;
        }

        // Outlines
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(7, 7, mainBoxWidth, mainBoxHeight);
        renderer.rect(143, 7, 66, 66);
        renderer.end();

        batch.begin();
        batch.setColor(Color.WHITE);

        //main sprite sheet
        if (texture != null) {
            batch.draw(texture, 8, 8);
        }
        if (zoomTexture != null) {
            batch.draw(zoomTexture, 144, 8, 64, 64);
        }

        drawText();

        batch.end();

        // draw gui buttons
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (exitDialog) {
            //background gray
            renderer.setProjectionMatrix(camera.combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0, 0, 0, 1);
            renderer.rect(viewport.getWorldWidth() / 2 - 100, viewport.getWorldHeight() / 2 + 12, 224, 60);
            renderer.end();
            batch.begin();
            drawExitText();
            batch.end();
            confirmExitStage.act(Gdx.graphics.getDeltaTime());
            confirmExitStage.draw();
        }

        // Draw custom cursor
        batch.begin();
        viewport.unproject(cursorCoords.set(Gdx.input.getX(), Gdx.input.getY()));
//        int tmpY = (int) (camera.viewportHeight - (cursorCoords.y));

        cursorX = (int) (cursorCoords.x);
        cursorY = (int) (cursorCoords.y - 8);
        batch.draw(cursor, cursorX, cursorY);
        batch.end();
    }

    public void drawExitText() {
        int fontX;
        int fontY;
        int x = (int) (viewport.getWorldWidth() / 2 - 98);
        int y = (int) (viewport.getWorldHeight() / 2+40);
        // Set the variable test for evaluating the x and y position of the ASCII set.
        String text = "Would you like exit (y/n)?";
        for (char C : text.toCharArray()) {
            fontX = ((int) C % 16) * 8;
            fontY = ((int) C / 16) * 8;
            batch.draw(font, x, y, fontX, fontY, 8, 8);
            x = x + 8;
        }
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
        pixmap.setBlending(Pixmap.Blending.None);
        texture = new Texture(pixmap);

        zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        zoomPixmap.setBlending(Pixmap.Blending.None);
        zoomTexture = new Texture(zoomPixmap);

        zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, 8, 8, 0, 0, 8, 8);
        zoomTexture.draw(zoomPixmap, 0, 0);
    }

    public void drawSelectedPixmapToMain() {
        if (actualX > 129) {

            zoomPixmap.setColor(drawColor);
            zoomPixmap.drawPixel(zoomX, zoomY);
            zoomTexture.draw(zoomPixmap, 0, 0);

            pixmap.drawPixmap(zoomPixmap, spriteIdX * 8, spriteIdY * 8, 0, 0, 8, 8);

            texture.draw(pixmap, 0, 0);
        } else {

            pixmap.setColor(drawColor);
            pixmap.drawPixel(actualX, actualY);
            texture.draw(pixmap, 0, 0);

            zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
            zoomPixmap.setBlending(Pixmap.Blending.None);

            zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, 8, 8, 0, 0, 8, 8);
            zoomTexture.draw(zoomPixmap, 0, 0);
        }

    }

    public void setDrawingCoords() {
        graphicsY = (int) (camera.viewportHeight - (coords.y));

        actualX = (int) (coords.x - 8);
        actualY = (int) (graphicsY - 104);

        zoomX = (actualX / 8) - 17;
        zoomY = (actualY / 8) - 8;
    }

    public void drawPixelsOnTouch(int button) {

        switch (button) {
            case 0:
                drawSelectedPixmapToMain();
                break;
            case 1:
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
                break;
            case 2:
                spriteIdX = (int) (actualX) / 8;
                spriteIdY = (actualY) / 8;
                spriteId = ((spriteIdY * 16) + (spriteIdX));

                zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
                zoomPixmap.setBlending(Pixmap.Blending.None);
                zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, 8, 8, 0, 0, 8, 8);
                zoomTexture.draw(zoomPixmap, 0, 0);
                break;
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
            if (exitDialog) {
                savePixmapImage();
                game.setScreen(new ConsoleScreen(game));
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }

            exitDialog = true;
            return false;
        }
        if (exitDialog) {
            switch (keycode) {
                case Keys.N:
                    exitDialog = false;
                    return true;
                case Keys.Y:
                    savePixmapImage();
                    game.setScreen(new ConsoleScreen(game));
                    Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
                    return true;
            }
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

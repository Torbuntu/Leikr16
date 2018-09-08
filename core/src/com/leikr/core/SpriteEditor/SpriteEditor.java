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
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Leikr;
import static com.leikr.core.Leikr.fileName;

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

    Texture spriteSheet;
    Texture zoomSpriteSheet;

    private final String firstSpriteSheet;
    private final String secondSpriteSheet;
    private final String thirdSpriteSheet;
    private final String fourthSpriteSheet;

    String selectedSpriteSheet;

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

    int zoomBoxWidth;
    int zoomBoxHeight;

    int spriteWidth = 8;
    int spriteHeight = 8;

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

    Texture bigSpriteIcon;
    TextureRegionDrawable bigSpriteIconDrawable;
    ImageButton bigSpriteIconButton;

    Texture smallSpriteIcon;
    TextureRegionDrawable smallSpriteIconDrawable;
    ImageButton smallSpriteIconButton;

    Texture tabIcon_0;
    Texture tabIcon_1;
    Texture tabIcon_2;
    Texture tabIcon_3;

    TextureRegionDrawable tabIconDrawable_0;
    TextureRegionDrawable tabIconDrawable_1;
    TextureRegionDrawable tabIconDrawable_2;
    TextureRegionDrawable tabIconDrawable_3;

    ImageButton tabIconButton_0;
    ImageButton tabIconButton_1;
    ImageButton tabIconButton_2;
    ImageButton tabIconButton_3;

    public SpriteEditor(Leikr game, SpriteEditorScreen speScreen) {
        this.game = game;
        batch = game.batch;
        sriteEditorScreen = speScreen;
        drawColor = Color.BLACK;
        coords = new Vector2();
        cursorCoords = new Vector2();
        graphicsY = 0;
        font = new Texture("LeikrFontA.png");
        cursor = new Texture(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/Cursor.png"));

        renderer = new ShapeRenderer();
        paintBrush = new PaintBrush(renderer, game);

        // gets the sprite sheet from the given fileName (which is the loaded game)
        firstSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + fileName + "_0.png";
        secondSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + fileName + "_1.png";
        thirdSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + fileName + "_2.png";
        fourthSpriteSheet = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + fileName + "_3.png";

        selectedSpriteSheet = firstSpriteSheet;
        setSelectedSpriteSheet(selectedSpriteSheet);

        actualX = 0;
        actualY = 0;
        zoomX = 0;
        zoomY = 0;

        mainBoxWidth = spriteSheet.getWidth() + 2;
        mainBoxHeight = spriteSheet.getHeight() + 2;

        zoomBoxWidth = 128;
        zoomBoxHeight = 128;

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

        bigSpriteIcon = new Texture("bigSpriteIcon.png");
        bigSpriteIconDrawable = new TextureRegionDrawable(new TextureRegion(bigSpriteIcon));
        bigSpriteIconButton = new ImageButton(bigSpriteIconDrawable);
        bigSpriteIconButton.setPosition(viewport.getWorldWidth() - 38, 0);
        bigSpriteIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Big Sprite selected.");
                spriteWidth = 16;
                spriteHeight = 16;

                zoomPixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
                zoomPixmap.setBlending(Pixmap.Blending.None);
                zoomSpriteSheet = new Texture(zoomPixmap);
                zoomPixmap.drawPixmap(pixmap, 0, 0, 16, 16, 0, 0, 16, 16);

                zoomSpriteSheet.draw(zoomPixmap, 0, 0);
            }
        });

        smallSpriteIcon = new Texture("smallSpriteIcon.png");
        smallSpriteIconDrawable = new TextureRegionDrawable(new TextureRegion(smallSpriteIcon));
        smallSpriteIconButton = new ImageButton(smallSpriteIconDrawable);
        smallSpriteIconButton.setPosition(viewport.getWorldWidth() - 48, 0);
        smallSpriteIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Small Sprite selected.");
                spriteWidth = 8;
                spriteHeight = 8;

                zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
                zoomPixmap.setBlending(Pixmap.Blending.None);
                zoomSpriteSheet = new Texture(zoomPixmap);
                zoomPixmap.drawPixmap(pixmap, 0, 0, 8, 8, 0, 0, 8, 8);
                zoomSpriteSheet.draw(zoomPixmap, 0, 0);
            }
        });

        tabIcon_0 = new Texture("tab_0.png");
        tabIconDrawable_0 = new TextureRegionDrawable(new TextureRegion(tabIcon_0));
        tabIconButton_0 = new ImageButton(tabIconDrawable_0);
        tabIconButton_0.setPosition(viewport.getWorldWidth() - 48, 10);
        tabIconButton_0.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 1");
                setSelectedSpriteSheet(firstSpriteSheet);
            }
        });
        tabIcon_1 = new Texture("tab_1.png");
        tabIconDrawable_1 = new TextureRegionDrawable(new TextureRegion(tabIcon_1));
        tabIconButton_1 = new ImageButton(tabIconDrawable_1);
        tabIconButton_1.setPosition(viewport.getWorldWidth() - 38, 10);
        tabIconButton_1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 2");
                setSelectedSpriteSheet(secondSpriteSheet);
            }
        });
        tabIcon_2 = new Texture("tab_2.png");
        tabIconDrawable_2 = new TextureRegionDrawable(new TextureRegion(tabIcon_2));
        tabIconButton_2 = new ImageButton(tabIconDrawable_2);
        tabIconButton_2.setPosition(viewport.getWorldWidth() - 28, 10);
        tabIconButton_2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 3");
                setSelectedSpriteSheet(thirdSpriteSheet);
            }
        });
        tabIcon_3 = new Texture("tab_3.png");
        tabIconDrawable_3 = new TextureRegionDrawable(new TextureRegion(tabIcon_3));
        tabIconButton_3 = new ImageButton(tabIconDrawable_3);
        tabIconButton_3.setPosition(viewport.getWorldWidth() - 18, 10);
        tabIconButton_3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 4");
                setSelectedSpriteSheet(fourthSpriteSheet);
            }
        });

        stage.addActor(tabIconButton_0);
        stage.addActor(tabIconButton_1);
        stage.addActor(tabIconButton_2);
        stage.addActor(tabIconButton_3);

        stage.addActor(saveIconButton);
        stage.addActor(undoIconButton);
        stage.addActor(eraserIconButton);
        stage.addActor(bigSpriteIconButton);
        stage.addActor(smallSpriteIconButton);

        confirmExitStage.addActor(okIconButton);
        confirmExitStage.addActor(noIconButton);

        Pixmap pm = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/HideCursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        // input processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(confirmExitStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    final void setSelectedSpriteSheet(String selected) {
        
        selectedSpriteSheet = selected;
        pixmap = new Pixmap(new FileHandle(selectedSpriteSheet));
        pixmap.setBlending(Pixmap.Blending.None);
        spriteSheet = new Texture(pixmap);

        zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        zoomPixmap.setBlending(Pixmap.Blending.None);
        zoomSpriteSheet = new Texture(zoomPixmap);

        zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, 8, 8, 0, 0, 8, 8);
        zoomSpriteSheet.draw(zoomPixmap, 0, 0);
    }

    public void renderSpriteEditor(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //background color
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(.1f, .2f, .2f, 1);
        renderer.rect(0, 0, Leikr.WIDTH, Leikr.HEIGHT);
        renderer.end();

        batch.setProjectionMatrix(camera.combined);

        count = 8;
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
        renderer.rect(spriteSheet.getWidth() + 11, 7, zoomBoxWidth + 2, zoomBoxHeight + 2);
        renderer.end();

        batch.begin();
        batch.setColor(Color.WHITE);

        //main sprite sheet
        if (spriteSheet != null) {
            batch.draw(spriteSheet, 8, 8);
        }
        if (zoomSpriteSheet != null) {
            batch.draw(zoomSpriteSheet, spriteSheet.getWidth() + 12, 8, zoomBoxWidth, zoomBoxHeight);
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
        int y = (int) (viewport.getWorldHeight() / 2 + 40);
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
        int x = 8;
        int y = spriteSheet.getHeight() + 12;
        int sheetOffset = 0;
        if(selectedSpriteSheet.contains("_0")){
            sheetOffset = 0+spriteId;
        }else if(selectedSpriteSheet.contains("_1")){
            sheetOffset = 256+spriteId;
        }else if(selectedSpriteSheet.contains("_2")){
            sheetOffset = 512+spriteId;
        }else if(selectedSpriteSheet.contains("_3")){
            sheetOffset = 768+spriteId;
        }
        // Set the variable test for evaluating the x and y position of the ASCII set.
        
        String text = "Sprite ID: " + sheetOffset;
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
        PixmapIO.writePNG(new FileHandle(selectedSpriteSheet), pixmap);
    }

    public void undoRecentEdits() {
        pixmap = new Pixmap(new FileHandle(selectedSpriteSheet));
        pixmap.setBlending(Pixmap.Blending.None);
        spriteSheet = new Texture(pixmap);

        zoomPixmap = new Pixmap(spriteWidth, spriteHeight, Pixmap.Format.RGBA8888);
        zoomPixmap.setBlending(Pixmap.Blending.None);
        zoomSpriteSheet = new Texture(zoomPixmap);

        zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, spriteWidth, spriteHeight, 0, 0, spriteWidth, spriteHeight);
        zoomSpriteSheet.draw(zoomPixmap, 0, 0);
    }

    public void drawSelectedPixmapToMain() {
        if (actualX > 129) {

            zoomPixmap.setColor(drawColor);
            zoomPixmap.drawPixel(zoomX, zoomY);
            zoomSpriteSheet.draw(zoomPixmap, 0, 0);

            pixmap.drawPixmap(zoomPixmap, spriteIdX * 8, spriteIdY * 8, 0, 0, spriteWidth, spriteHeight);

            spriteSheet.draw(pixmap, 0, 0);
        } else {

            pixmap.setColor(drawColor);
            pixmap.drawPixel(actualX, actualY);
            spriteSheet.draw(pixmap, 0, 0);

            zoomPixmap = new Pixmap(spriteWidth, spriteHeight, Pixmap.Format.RGBA8888);
            zoomPixmap.setBlending(Pixmap.Blending.None);

            zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, spriteWidth, spriteHeight, 0, 0, spriteWidth, spriteHeight);
            zoomSpriteSheet.draw(zoomPixmap, 0, 0);
        }

    }

    public void setDrawingCoords() {
        graphicsY = (int) (camera.viewportHeight - (coords.y));

        actualX = (int) (coords.x - 9);
        actualY = (int) (graphicsY - 104);

        if (spriteWidth == 16) {
            zoomX = (actualX / 8) - 16;
            zoomY = (actualY / 8);
        } else {
            zoomX = (actualX / 16) - 8;
            zoomY = (actualY / 16);
        }

        System.out.println(actualX + " : " + actualY);
        System.out.println(zoomX + " : " + zoomY);
    }

    public void drawPixelsOnTouch(int button) {

        switch (button) {
            case 0:
                drawSelectedPixmapToMain();
                break;
            case 1:
                if (coords.x >= 9 && coords.x <= 18) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(0));
                }
                if (coords.x >= 19 && coords.x <= 28) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(1));
                }
                if (coords.x >= 29 && coords.x <= 38) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(2));
                }
                if (coords.x >= 39 && coords.x <= 48) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(3));
                }
                if (coords.x >= 49 && coords.x <= 58) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(4));
                }
                if (coords.x >= 59 && coords.x <= 68) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(5));
                }
                if (coords.x >= 69 && coords.x <= 78) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(6));
                }
                if (coords.x >= 79 && coords.x <= 88) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(7));
                }
                if (coords.x >= 89 && coords.x <= 98) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(8));
                }
                if (coords.x >= 91 && coords.x <= 108) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(9));
                }
                if (coords.x >= 109 && coords.x <= 118) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(10));
                }
                if (coords.x >= 119 && coords.x <= 128) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(11));
                }
                if (coords.x >= 129 && coords.x <= 138) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(12));
                }
                if (coords.x >= 139 && coords.x <= 148) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(13));
                }
                if (coords.x >= 149 && coords.x <= 158) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(14));
                }
                if (coords.x >= 159 && coords.x <= 168) {
                    drawColor.set(paintBrush.leikrPalette.palette.get(15));
                }
                break;
            case 2:
                spriteIdX = (actualX) / 8;
                spriteIdY = (actualY) / 8;
                spriteId = ((spriteIdY * 16) + (spriteIdX));

                zoomPixmap = new Pixmap(spriteWidth, spriteHeight, Pixmap.Format.RGBA8888);
                zoomPixmap.setBlending(Pixmap.Blending.None);
                zoomPixmap.drawPixmap(pixmap, spriteIdX * 8, spriteIdY * 8, spriteWidth, spriteHeight, 0, 0, spriteWidth, spriteHeight);
                zoomSpriteSheet.draw(zoomPixmap, 0, 0);
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

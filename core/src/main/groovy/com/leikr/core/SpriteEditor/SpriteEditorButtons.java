/*
 * Copyright 2018 torbuntu.
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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.leikr.core.ConsoleDirectory.ConsoleScreen;
import com.leikr.core.Graphics.PaintBrush;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class SpriteEditorButtons {

    ImageButton tabIconButton_0;
    ImageButton tabIconButton_1;
    ImageButton tabIconButton_2;
    ImageButton tabIconButton_3;
    ImageButton saveIconButton;
    ImageButton undoIconButton;
    ImageButton eraserIconButton;
    ImageButton okIconButton;
    ImageButton noIconButton;
    ImageButton bigSpriteIconButton;
    ImageButton smallSpriteIconButton;

    Stage stage;

    Stage confirmExitStage;

    Viewport viewport;
    SpriteEditor spe;
    PaintBrush pBrush;

    SpriteEditorButtons(Viewport viewport, SpriteEditor spe, ShapeRenderer renderer, Leikr game) {
        this.viewport = viewport;
        this.spe = spe;
        this.pBrush = new PaintBrush(renderer, game);
        stage = new Stage(viewport);
        confirmExitStage = new Stage(viewport);

        saveIconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("saveIcon.png"))));
        saveIconButton.setPosition(viewport.getWorldWidth() - 18, 0);
        saveIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.savePixmapImage();
                System.out.println("Save clicked!");
            }
        });

        undoIconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("undoIcon.png"))));
        undoIconButton.setPosition((int) viewport.getWorldWidth() - 8, 0);
        undoIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.undoRecentEdits();
                System.out.println("Undo clicked!");
            }
        });

        eraserIconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("eraserIcon.png"))));
        eraserIconButton.setPosition((int) viewport.getWorldWidth() - 28, 0);
        eraserIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(0, 0, 0, -1);
                System.out.println("Eraser clicked!");
            }
        });

        okIconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("ok.png"))));
        okIconButton.setPosition(viewport.getWorldWidth() / 2 - 48, viewport.getWorldHeight() / 2 + 16);
        okIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (spe.exitDialog) {
                    spe.savePixmapImage();
                    spe.game.setScreen(new ConsoleScreen(spe.game));
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                    System.out.println("Exit clicked!");
                }

            }
        });

        noIconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("no.png"))));
        noIconButton.setPosition(viewport.getWorldWidth() / 2 - 24, viewport.getWorldHeight() / 2 + 16);
        noIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (spe.exitDialog) {
                    spe.exitDialog = false;
                    System.out.println("Stay clicked!");
                }
            }
        });

        bigSpriteIconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("bigSpriteIcon.png"))));
        bigSpriteIconButton.setPosition(viewport.getWorldWidth() - 38, 0);
        bigSpriteIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Big Sprite selected.");
                spe.spriteWidth = 16;
                spe.spriteHeight = 16;

                spe.zoomPixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
                spe.zoomPixmap.setBlending(Pixmap.Blending.None);
                spe.zoomSpriteSheet = new Texture(spe.zoomPixmap);
                spe.zoomPixmap.drawPixmap(spe.pixmap, 0, 0, 16, 16, 0, 0, 16, 16);

                spe.zoomSpriteSheet.draw(spe.zoomPixmap, 0, 0);
            }
        });

        smallSpriteIconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("smallSpriteIcon.png"))));
        smallSpriteIconButton.setPosition(viewport.getWorldWidth() - 48, 0);
        smallSpriteIconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Small Sprite selected.");
                spe.spriteWidth = 8;
                spe.spriteHeight = 8;

                spe.zoomPixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
                spe.zoomPixmap.setBlending(Pixmap.Blending.None);
                spe.zoomSpriteSheet = new Texture(spe.zoomPixmap);
                spe.zoomPixmap.drawPixmap(spe.pixmap, 0, 0, 8, 8, 0, 0, 8, 8);
                spe.zoomSpriteSheet.draw(spe.zoomPixmap, 0, 0);
            }
        });

        tabIconButton_0 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("tab_0.png"))));
        tabIconButton_0.setPosition(viewport.getWorldWidth() - 48, 10);
        tabIconButton_0.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 1");
                spe.setSelectedSpriteSheet(spe.firstSpriteSheet);
            }
        });
        tabIconButton_1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("tab_1.png"))));
        tabIconButton_1.setPosition(viewport.getWorldWidth() - 38, 10);
        tabIconButton_1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 2");
                spe.setSelectedSpriteSheet(spe.secondSpriteSheet);
            }
        });
        tabIconButton_2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("tab_2.png"))));
        tabIconButton_2.setPosition(viewport.getWorldWidth() - 28, 10);
        tabIconButton_2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 3");
                spe.setSelectedSpriteSheet(spe.thirdSpriteSheet);
            }
        });

        tabIconButton_3 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("tab_3.png"))));
        tabIconButton_3.setPosition(viewport.getWorldWidth() - 18, 10);
        tabIconButton_3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab 4");
                spe.setSelectedSpriteSheet(spe.fourthSpriteSheet);
            }
        });

        Pixmap plt0 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt0.setColor(new Color(pBrush.leikrPalette.palette.get(0)));
        plt0.fillRectangle(0, 0, 8, 8);
        ImageButton plt0btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt0))));
        plt0.dispose();
        plt0btn.setPosition(8, viewport.getWorldHeight() - 10);
        plt0btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(0));
            }
        });

        Pixmap plt1 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt1.setColor(new Color(pBrush.leikrPalette.palette.get(1)));
        plt1.fillRectangle(0, 0, 8, 8);
        ImageButton plt1btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt1))));
        plt1.dispose();
        plt1btn.setPosition(18, viewport.getWorldHeight() - 10);
        plt1btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(1));
            }
        });

        Pixmap plt2 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt2.setColor(new Color(pBrush.leikrPalette.palette.get(2)));
        plt2.fillRectangle(0, 0, 8, 8);
        ImageButton plt2btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt2))));
        plt2.dispose();
        plt2btn.setPosition(28, viewport.getWorldHeight() - 10);
        plt2btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(2));
            }
        });

        Pixmap plt3 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt3.setColor(new Color(pBrush.leikrPalette.palette.get(3)));
        plt3.fillRectangle(0, 0, 8, 8);
        ImageButton plt3btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt3))));
        plt3.dispose();
        plt3btn.setPosition(38, viewport.getWorldHeight() - 10);
        plt3btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(3));
            }
        });
        
        Pixmap plt4 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt4.setColor(new Color(pBrush.leikrPalette.palette.get(4)));
        plt4.fillRectangle(0, 0, 8, 8);
        ImageButton plt4btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt4))));
        plt4.dispose();
        plt4btn.setPosition(48, viewport.getWorldHeight() - 10);
        plt4btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(4));
            }
        });
        
        Pixmap plt5 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt5.setColor(new Color(pBrush.leikrPalette.palette.get(5)));
        plt5.fillRectangle(0, 0, 8, 8);
        ImageButton plt5btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt5))));
        plt5.dispose();
        plt5btn.setPosition(58, viewport.getWorldHeight() - 10);
        plt5btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(5));
            }
        });
        
        Pixmap plt6 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt6.setColor(new Color(pBrush.leikrPalette.palette.get(6)));
        plt6.fillRectangle(0, 0, 8, 8);
        ImageButton plt6btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt6))));
        plt6.dispose();
        plt6btn.setPosition(68, viewport.getWorldHeight() - 10);
        plt6btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(6));
            }
        });

        Pixmap plt7 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt7.setColor(new Color(pBrush.leikrPalette.palette.get(7)));
        plt7.fillRectangle(0, 0, 8, 8);
        ImageButton plt7btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt7))));
        plt7.dispose();
        plt7btn.setPosition(78, viewport.getWorldHeight() - 10);
        plt7btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(7));
            }
        });
                
        Pixmap plt8 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt8.setColor(new Color(pBrush.leikrPalette.palette.get(8)));
        plt8.fillRectangle(0, 0, 8, 8);
        ImageButton plt8btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt8))));
        plt8.dispose();
        plt8btn.setPosition(88, viewport.getWorldHeight() - 10);
        plt8btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(8));
            }
        });
        
        Pixmap plt9 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt9.setColor(new Color(pBrush.leikrPalette.palette.get(9)));
        plt9.fillRectangle(0, 0, 8, 8);
        ImageButton plt9btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt9))));
        plt9.dispose();
        plt9btn.setPosition(98, viewport.getWorldHeight() - 10);
        plt9btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(9));
            }
        });
        
        Pixmap plt10 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt10.setColor(new Color(pBrush.leikrPalette.palette.get(10)));
        plt10.fillRectangle(0, 0, 8, 8);
        ImageButton plt10btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt10))));
        plt10.dispose();
        plt10btn.setPosition(108, viewport.getWorldHeight() - 10);
        plt10btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(10));
            }
        });
        
        Pixmap plt11 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt11.setColor(new Color(pBrush.leikrPalette.palette.get(11)));
        plt11.fillRectangle(0, 0, 8, 8);
        ImageButton plt11btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt11))));
        plt11.dispose();
        plt11btn.setPosition(118, viewport.getWorldHeight() - 10);
        plt11btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(11));
            }
        });
        
        Pixmap plt12 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt12.setColor(new Color(pBrush.leikrPalette.palette.get(12)));
        plt12.fillRectangle(0, 0, 8, 8);
        ImageButton plt12btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt12))));
        plt12.dispose();
        plt12btn.setPosition(128, viewport.getWorldHeight() - 10);
        plt12btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(12));
            }
        });
        
        Pixmap plt13 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt13.setColor(new Color(pBrush.leikrPalette.palette.get(13)));
        plt13.fillRectangle(0, 0, 8, 8);
        ImageButton plt13btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt13))));
        plt13.dispose();
        plt13btn.setPosition(138, viewport.getWorldHeight() - 10);
        plt13btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(13));
            }
        });
        
        Pixmap plt14 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt14.setColor(new Color(pBrush.leikrPalette.palette.get(14)));
        plt14.fillRectangle(0, 0, 8, 8);
        ImageButton plt14btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt14))));
        plt14.dispose();
        plt14btn.setPosition(148, viewport.getWorldHeight() - 10);
        plt14btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(14));
            }
        });
        
        Pixmap plt15 = new Pixmap(8, 8, Pixmap.Format.RGB888);
        plt15.setColor(new Color(pBrush.leikrPalette.palette.get(15)));
        plt15.fillRectangle(0, 0, 8, 8);
        ImageButton plt15btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(plt15))));
        plt15.dispose();
        plt15btn.setPosition(158, viewport.getWorldHeight() - 10);
        plt15btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spe.drawColor.set(pBrush.leikrPalette.palette.get(15));
            }
        });
        
        
        stage.addActor(plt0btn);
        stage.addActor(plt1btn);
        stage.addActor(plt2btn);
        stage.addActor(plt3btn);
        stage.addActor(plt4btn);
        stage.addActor(plt5btn);
        stage.addActor(plt6btn);
        stage.addActor(plt7btn);
        stage.addActor(plt8btn);
        stage.addActor(plt9btn);
        stage.addActor(plt10btn);
        stage.addActor(plt11btn);
        stage.addActor(plt12btn);
        stage.addActor(plt13btn);
        stage.addActor(plt14btn);
        stage.addActor(plt15btn);

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
    }

    public void disposeButtons() {
        stage.dispose();
        confirmExitStage.dispose();
    }

}

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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import java.util.ArrayList;
import static com.leikr.core.Leikr.gameName;

/**
 *
 * @author tor
 */
public class LeikrPalette {

    public ArrayList<Integer> palette;
    Pixmap pixmap;

    public ArrayList<Integer> palette_0;
    public ArrayList<Integer> palette_1;
    public ArrayList<Integer> palette_2;
    public ArrayList<Integer> palette_3;
    Pixmap pixmap_0;
    Pixmap pixmap_1;
    Pixmap pixmap_2;
    Pixmap pixmap_3;

    public LeikrPalette() {
        palette = new ArrayList<>();
        palette_0 = new ArrayList<>();
        palette_1 = new ArrayList<>();
        palette_2 = new ArrayList<>();
        palette_3 = new ArrayList<>();

        if (Gdx.files.external("Leikr/ChipSpace/" + gameName + "/graphics/Palette_0.png").exists()) {
            pixmap = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/graphics/Palette_0.png"));

            pixmap_0 = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/graphics/Palette_0.png"));
            pixmap_1 = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/graphics/Palette_1.png"));
            pixmap_2 = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/graphics/Palette_2.png"));
            pixmap_3 = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + gameName + "/graphics/Palette_3.png"));

            for (int i = 0; i <= 15; i++) {
                palette.add(pixmap.getPixel(i, 0));
                palette_0.add(pixmap_0.getPixel(i, 0));
                palette_1.add(pixmap_1.getPixel(i, 0));
                palette_2.add(pixmap_2.getPixel(i, 0));
                palette_3.add(pixmap_3.getPixel(i, 0));
            }
        } else {
            pixmap = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/Palette.png"));
            for (int i = 0; i <= 15; i++) {
                palette.add(pixmap.getPixel(i, 0));
            }
        }

    }

    // default palette
    public LeikrPalette(String custom) {
        palette = new ArrayList<>();
        if (custom.length() > 2) {
            pixmap = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/" + custom + ".png"));
        } else {
            pixmap = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/Palette.png"));
        }
        for (int i = 0; i <= 15; i++) {
            palette.add(pixmap.getPixel(i, 0));
        }
    }

}

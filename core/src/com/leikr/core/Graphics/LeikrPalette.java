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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.leikr.core.Leikr;
import static com.leikr.core.Leikr.fileName;
import java.util.ArrayList;

/**
 *
 * @author tor
 */
public class LeikrPalette {

    public ArrayList<Integer> palette;
    Pixmap pixmap;

    public LeikrPalette() {
        palette = new ArrayList<>();
        if (Gdx.files.external("Leikr/ChipSpace/Palette.png").exists()) {
            pixmap = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/Palette.png"));

        } else {
            pixmap = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/OS/Palette.png"));
        }
        for (int i = 0; i <= 15; i++) {
            palette.add(pixmap.getPixel(i, 0));
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

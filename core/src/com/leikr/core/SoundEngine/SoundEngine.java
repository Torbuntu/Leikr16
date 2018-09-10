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
package com.leikr.core.SoundEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.leikr.core.Leikr;

/**
 *
 * @author tor
 */
public class SoundEngine {

    Leikr game;
    AudioDevice device;

    public SoundEngine(Leikr game) {
        this.game = game;
        device = Gdx.audio.newAudioDevice(44100, true);

    }

    public void soundEnginePlayBeep() {

        float[] buffer = new float[10000];
        for (int i = 0; i < 10000; i++) {
            buffer[i] = (float) Math.sin(1000 * (2 * Math.PI) * i / 44100);
        }

        new Thread() {
            @Override
            public void run() {
                device.writeSamples(buffer, 0, (int) buffer.length);
                
            }
        }.start();

    }

    public void disposeSoundEngine() {
        device.dispose();

    }

}

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
package com.leikr.core.SoundEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.leikr.core.Leikr;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.python.modules.time.Time;

/**
 *
 * @author tor
 */
public class SoundEngine {

    Leikr game;
    AudioDevice device;
    AudioFormat audioFormat;

    public SoundEngine(Leikr game) {
        this.game = game;
        device = Gdx.audio.newAudioDevice(44100, true);
        audioFormat = new AudioFormat(44100, 16, 1, true, true);
    }

    public String playSound(int id, float dur) {
        String file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + Leikr.GAME_NAME + "/" + "Audio/" + Leikr.GAME_NAME + "_" + id + ".wav";
        Sound tmp = Gdx.audio.newSound(new FileHandle(file));
        tmp.loop();
        Time.sleep(dur);
        tmp.stop();
        tmp.dispose();
        return "success";
    }

    public Sound getSfx(int id) {
        return Gdx.audio.newSound(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + Leikr.GAME_NAME + "/" + "Audio/" + Leikr.GAME_NAME + "_" + id + ".wav"));
    }

    public void playSfx(Sound snd) {
        snd.play();
    }

    public void playSfx(Sound snd, float vol) {
        snd.play(vol);
    }

    public void playSfx(Sound snd, float vol, float pit, float pan) {
        snd.play(vol, pit, pan);
    }

    public void disposeSoundEngine() {
        device.dispose();
    }

    public void playNewAudio(String wave, int frequency, int seconds) {
        BasicOscillator osc = new BasicOscillator();

        osc.setFrequency(frequency);
        switch (wave.toLowerCase()) {
            default:
            case "sine":
            case "sin":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SIN);
                break;
            case "saw":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SAW);
                break;
            case "square":
            case "squ":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SQU);
                break;
            case "tri":
            case "triangle":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.TRI);
                break;
            case "noise":
            case "noi":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.NOI);
                break;
            case "pha":
            case "phase":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.PHA);
                break;
        }

        SamplePlayer player = new SamplePlayer();

        player.setSampleProvider(osc);

        player.startPlayer();

        Time.sleep(seconds);

        player.stopPlayer();

    }

    public String writeAudioToDisk(String wave, int frequency, int seconds, int id) {
        BasicOscillator osc = new BasicOscillator();

        osc.setFrequency(frequency);
        switch (wave.toLowerCase()) {
            default:
            case "sine":
            case "sin":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SIN);
                break;
            case "saw":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SAW);
                break;
            case "square":
            case "squ":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SQU);
                break;
            case "tri":
            case "triangle":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.TRI);
                break;
            case "noise":
            case "noi":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.NOI);
                break;
            case "pha":
            case "phase":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.PHA);
                break;
        }
        if (id > 127) {
            return "ID is too large. Must be between 0 and 127";
        }
        try {
            byte[] sampleArray = new byte[44100 * seconds];
            osc.getWriteableSamples(sampleArray);
            AudioInputStream audioInStream = new AudioInputStream(new ByteArrayInputStream(sampleArray), audioFormat, sampleArray.length);
            String file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + Leikr.GAME_NAME + "/" + "audio/" + Leikr.GAME_NAME + "_" + id + ".wav";
            try {
                AudioSystem.write(audioInStream, AudioFileFormat.Type.WAVE, new File(file));
            } catch (Exception e) {
                return "Failed to write to file with id: " + id + " Error: " + e.getMessage();
            }
            return "Audio sample saved successfully with id: " + id;
        } catch (Exception e) {
            return "System error: " + e.getMessage();
        }
    }

}

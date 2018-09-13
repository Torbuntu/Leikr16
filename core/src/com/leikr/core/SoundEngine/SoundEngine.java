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
import static com.leikr.core.Leikr.fileName;
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

    public SoundEngine(Leikr game) {
        this.game = game;
        device = Gdx.audio.newAudioDevice(44100, true);

    }

    public void playSineTone(float tone, float dur) {

        float[] pcm_data = new float[44100 * 2];
        double L1 = 44100.0 / tone;
        for (int i = 0; i < pcm_data.length; i++) {
            pcm_data[i] = (float) (55 * Math.sin((i / L1) * Math.PI * 2));
        }

        //AudioFormat frmt = new AudioFormat(44100, 8, 1, true, true);
        //AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(pcm_data), frmt, pcm_data.length / frmt.getFrameSize());
        try {
            //AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("/home/tor/Desktop/test.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        device.writeSamples(pcm_data, 0, (int) dur);
    }

    public String exportAudioWav(float tone, float dur, String type, int id) {

        byte[] pcm_data = new byte[44100 * 2];
        switch (type.toLowerCase()) {
            case "sine":
            default:
                double L1 = 44100.0 / tone;
                for (int i = 0; i < pcm_data.length; i++) {
                    pcm_data[i] = (byte) (55 * Math.sin((i / L1) * Math.PI * 2));
                }
                break;
        }

        AudioFormat frmt = new AudioFormat(44100, 16, 1, true, true);
        AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(pcm_data), frmt, (long) dur);
        String file;
        if (id < 128) {
            file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav";

        } else {
            return "id is too large.";
        }
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(file));
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create sound. " + e.getMessage();
        }
    }
    
    public String playSound(int id, float dur){
        String file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav";
        Sound tmp = Gdx.audio.newSound(new FileHandle(file));
        tmp.play(1.0f);
        Time.sleep(dur);
        tmp.dispose();
        return "success";
    }
    
    public Sound getSfx(int id){
        return Gdx.audio.newSound(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav"));
    }
    public void playSfx(Sound snd){
        snd.play();
    }
    public void playSfx(Sound snd, float vol ){
        snd.play(vol);
    }
    public void playSfx(Sound snd, float vol, float pit, float pan ){
        snd.play(vol, pit, pan);
    }

    public void disposeSoundEngine() {
        device.dispose();

    }

}

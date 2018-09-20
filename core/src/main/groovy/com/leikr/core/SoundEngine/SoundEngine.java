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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public float[] generateSine(int freq, int dur) {
        int sampleFreq = 44100 / freq;
        float[] buffer = new float[44100 * dur];
        for (int i = 0; i < 44100 * dur; i++) {
            buffer[i] = (float) Math.sin(i / (sampleFreq / (Math.PI * 2))) * 127;
        }
        return buffer;
    }

    public byte[] generateSineTwo(int freq, int dur) {
        int sampleFreq = 44100 / freq;
        byte[] buffer = new byte[44100 * dur];
        for (int i = 0; i < 44100 * dur; i++) {
            buffer[i] = (byte) (Math.sin(i / (sampleFreq / (Math.PI * 2))) * 127);
        }
        return buffer;
    }

    public void playSineTone(float freq, int dur) {
        device = Gdx.audio.newAudioDevice(44100, true);
//        float freq = tone;
//        float sample = 44100f;
//        float[] buffer = new float[(int) sample];
//
//        for (int i = 0; i < sample; i++) {
//            buffer[i] = (float) 55 * (float) Math.sin(2 * Math.PI * freq / sample * i);
//        }

//        double length = 44100.0 / tone;
//        float[] pcm_data = new float[(int)length];
//
//        for (int i = 0; i < pcm_data.length; i++) {
//            pcm_data[i] = (float) (55 * Math.sin((i / length) * Math.PI * 2));
//        }
        //AudioFormat frmt = new AudioFormat(44100, 8, 1, true, true);
        //AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(pcm_data), frmt, pcm_data.length / frmt.getFrameSize());
        try {
            //AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("/home/tor/Desktop/test.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        float[] buffer = generateSine((int) freq, dur);
        device.writeSamples(buffer, 0, buffer.length);
        device.dispose();

    }

    public String exportAudioWav(int freq, int dur, String type, int id) {

        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(bas);
        byte[] buffer;
        switch (type.toLowerCase()) {
            case "sine":
            default:
                buffer = generateSineTwo(freq, dur);
                //buffer = generateSine(freq, dur);
                break;
        }

        AudioFormat frmt = new AudioFormat(44100, 16, 1, true, true);
        AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(buffer), frmt, buffer.length);
        String file;
        if (id < 128) {
            file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav";

        } else {
            return "id is too large.";
        }
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(file));
            return "New sound file successfully generated in " + fileName + "/audio/";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create sound. " + e.getMessage();
        }
    }

    public String playSound(int id, float dur) {
        String file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav";
        Sound tmp = Gdx.audio.newSound(new FileHandle(file));
        tmp.play(1.0f);
        Time.sleep(dur);
        tmp.dispose();
        return "success";
    }

    public Sound getSfx(int id) {
        return Gdx.audio.newSound(new FileHandle(Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav"));
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

    public void playNewAudio(int frequency, int seconds, String wave) {
        BasicOscillator osc = new BasicOscillator();

        osc.setFrequency(frequency);
        switch (wave.toLowerCase()) {
            case "saw":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SAW);

                break;
            case "sin":

                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SIN);
                break;
            case "square":
                osc.setOscWaveshape(BasicOscillator.WAVESHAPE.SQU);
                break;

        }

        SamplePlayer player = new SamplePlayer();

        player.setSampleProvider(osc);

        player.startPlayer();

        try {
            Thread.sleep(seconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(SoundEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

        player.stopPlayer();

    }

}

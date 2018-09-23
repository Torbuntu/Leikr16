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
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
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
    Synthesizer synth;
    final MidiChannel[] midiChan;
    Instrument[] instr;

    public SoundEngine(Leikr game) {
        this.game = game;
        device = Gdx.audio.newAudioDevice(44100, true);

        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(SoundEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        midiChan = synth.getChannels();
        midiChan[0].setMono(true);

        instr = synth.getDefaultSoundbank().getInstruments();
    }

    public void setInstrument(int id) {
        try {
            midiChan[0].programChange(0, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playNote(int note, int velocity, int duration) {
        midiChan[0].noteOn(note, velocity);
        try {
            Time.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        midiChan[0].allNotesOff();
    }

    public String playSound(int id, float dur) {
        String file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav";
        Sound tmp = Gdx.audio.newSound(new FileHandle(file));
        tmp.loop();
        Time.sleep(dur);
        tmp.stop();
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
        if(id > 127){
            return "ID is too large. Must be between 0 and 127";
        }
        try {
            byte[] sampleArray = new byte[44100 * seconds];
            osc.getWriteableSamples(sampleArray);
            AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, true);
            AudioInputStream audioInStream = new AudioInputStream(new ByteArrayInputStream(sampleArray), audioFormat, sampleArray.length);
            String file = Gdx.files.getExternalStoragePath() + "Leikr/ChipSpace/" + fileName + "/" + "audio/" + fileName + "_" + id + ".wav";
            try {
                AudioSystem.write(audioInStream, AudioFileFormat.Type.WAVE, new File(file));
            } catch (Exception e) {
                return "Failed to write to file with id: "+id + " Error: "+e.getMessage();
            }
            return "Audio sample saved successfully with id: "+id;
        } catch (Exception e) {
            return "System error: "+e.getMessage();
        }
    }

}

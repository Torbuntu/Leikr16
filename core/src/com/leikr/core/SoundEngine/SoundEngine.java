/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.SoundEngine;

import com.leikr.core.Leikr;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.WhiteNoise;
import com.jsyn.unitgen.UnitOscillator;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author tor
 */
public class SoundEngine {

    Leikr game;

    public SoundEngine(Leikr game) {
        this.game = game;
    }

    public void soundEnginePlayBeep(float dur, float freq, float amp, String oscType) {
        Synthesizer synth;
        UnitOscillator osc;
        LineOut lineOut;

        // Create a context for the synthesizer.
        synth = JSyn.createSynthesizer();

        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();

        // Add a tone generator.
        switch (oscType.toLowerCase()) {
            case "sine":
                synth.add(osc = new SineOscillator());
                break;
            case "triangle":
                synth.add(osc = new TriangleOscillator());
                break;
            case "square":
                synth.add(osc = new SquareOscillator());
                break;
            default:
                synth.add(osc = new SineOscillator());
                break;
        }

        // Add a stereo audio output unit.
        synth.add(lineOut = new LineOut());

        // Connect the oscillator to both channels of the output.
        osc.output.connect(0, lineOut.input, 0);
        osc.output.connect(0, lineOut.input, 1);

        // Set the frequency and amplitude for the sine wave.
        osc.frequency.set(freq);
        osc.amplitude.set(amp);

        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        lineOut.start();

        // Sleep while the sound is generated in the background.
        try {
            double time = synth.getCurrentTime();
            // Sleep for a few seconds.
            synth.sleepUntil(time + dur);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lineOut.stop();
        // Stop everything.
        synth.stop();
        //playTone();
    }

    public static void playTone() {

        try {
            createTone(1000, 100, 100);
        } catch (LineUnavailableException lue) {
            System.out.println(lue);
        }
    }

    /**
     * parameters are frequency in Hertz and volume
     *
     */
    public static void createTone(int Hertz, int volume, int time)
            throws LineUnavailableException {
        /**
         * Exception is thrown when line cannot be opened
         */

        float rate = 8000f;
        byte[] buf;
        AudioFormat audioF;

        buf = new byte[1];
        audioF = new AudioFormat(rate, 8, 1, true, false);
        //sampleRate, sampleSizeInBits,channels,signed,bigEndian

        SourceDataLine sourceDL = AudioSystem.getSourceDataLine(audioF);
        sourceDL = AudioSystem.getSourceDataLine(audioF);
        sourceDL.open(audioF);
        sourceDL.start();

        for (int i = 0; i < time*8; i++) {
            double angle = i / (rate / Hertz) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) *127.0 * volume);
            sourceDL.write(buf, 0, 1);
        }

        sourceDL.drain();
        sourceDL.stop();
        sourceDL.close();
    }

}

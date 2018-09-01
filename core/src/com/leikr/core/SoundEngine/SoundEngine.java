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

/**
 *
 * @author tor
 */
public class SoundEngine {

    Leikr game;

    public SoundEngine(Leikr game) {
        this.game = game;
    }

    public void doSomething() {
        Synthesizer synth;
        UnitOscillator osc;
        LineOut lineOut;
        WhiteNoise whiteNoise = new WhiteNoise();

        // Create a context for the synthesizer.
        synth = JSyn.createSynthesizer();

        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();

        // Add a tone generator.
//        synth.add(osc = new SineOscillator());
//        synth.add(osc = new SquareOscillator());
//        synth.add(osc = new TriangleOscillator());
        synth.add(whiteNoise);

        // Add a stereo audio output unit.
        synth.add(lineOut = new LineOut());

        // Connect the oscillator to both channels of the output.
//        osc.output.connect(0, lineOut.input, 0);
//        osc.output.connect(0, lineOut.input, 1);
        whiteNoise.output.connect(0, lineOut.input, 0);
        whiteNoise.output.connect(0, lineOut.input, 1);

        // Set the frequency and amplitude for the sine wave.
//        osc.frequency.set(345.0);
//        osc.amplitude.set(0.6);
//        whiteNoise.frequency.set(345.0);
        whiteNoise.amplitude.set(0.6);

        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        lineOut.start();

        System.out.println("You should now be hearing a sine wave. ---------");

        // Sleep while the sound is generated in the background.
        try {
            double time = synth.getCurrentTime();
            System.out.println("time = " + time);
            // Sleep for a few seconds.
            synth.sleepUntil(time + 1.0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stop playing. -------------------");
        // Stop everything.
        synth.stop();
    }

    public void soundEnginePlayBeep(float dur, float freq, String oscType) {
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
        osc.amplitude.set(0.6);

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

        // Stop everything.
        synth.stop();
    }

}

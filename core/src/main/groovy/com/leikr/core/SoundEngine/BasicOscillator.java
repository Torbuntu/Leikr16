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

// This class was modified from: http://www.drdobbs.com/jvm/music-components-in-java-creating-oscill/230500178?pgno=1

package com.leikr.core.SoundEngine;

import ddf.minim.ugens.Oscil;
import ddf.minim.ugens.Waves;

/**
 *
 * @author tor
 */
public class BasicOscillator implements SampleProviderIntfc {

    // Instance data
    private WAVESHAPE waveshape;
    private long periodSamples;
    private long sampleNumber;

    Oscil triWave;
    Oscil sinWave;
    Oscil squWave;
    Oscil sawWave;
    Oscil phaWave;

    /**
     * Waveshape enumeration
     */
    public enum WAVESHAPE {
        SIN, SQU, SAW, NOI, TRI, PHA
    }

    /**
     * Basic Oscillator Class Constructor
     *
     * Default instance has SIN waveshape at 1000 Hz
     */
    public BasicOscillator() {
        this.waveshape = WAVESHAPE.SIN;
        periodSamples = (long) (SamplePlayer.SAMPLE_RATE / 440.0);

        triWave = new Oscil(440, 0.5f, Waves.TRIANGLE);
        sinWave = new Oscil(440, 0.5f, Waves.SINE);
        squWave = new Oscil(440, 0.5f, Waves.SQUARE);
        sawWave = new Oscil(440, 0.5f, Waves.SAW);
        phaWave = new Oscil(440, 0.5f, Waves.QUARTERPULSE);
    }

    /**
     * Set waveshape of oscillator
     *
     * @param waveshape Determines the waveshape of this oscillator
     */
    public void setOscWaveshape(WAVESHAPE waveshape) {

        this.waveshape = waveshape;
    }

    /**
     * Set the frequency of the oscillator in Hz.
     *
     * @param frequency Frequency in Hz for this oscillator
     */
    public void setFrequency(double frequency) {

        periodSamples = (long) (SamplePlayer.SAMPLE_RATE / frequency);
    }

    /**
     * Return the next sample of the oscillator's waveform
     *
     * @return Next oscillator sample
     */
    protected double getSample() {

        double value;
        double x = sampleNumber / (double) periodSamples;

        switch (waveshape) {
            default:
            case SIN:
                value = sinWave.getWaveform().value((float) x);
                break;
            case SQU:
                value = squWave.getWaveform().value((float) x);
                break;
            case SAW:
                value = sawWave.getWaveform().value((float) x);
                break;
            case NOI:
                value = Math.random();
                break;
            case TRI:
                value = triWave.getWaveform().value((float) x);
                break;
            case PHA:
                value = phaWave.getWaveform().value((float)x);
                break;
        }
        sampleNumber = (sampleNumber + 1) % periodSamples;
        return value;
    }

    /**
     * Get a buffer of oscillator samples
     *
     * @param buffer Array to fill with samples
     *
     * @return Count of bytes produced.
     */
    @Override
    public int getSamples(byte[] buffer) {
        int index = 0;
        for (int i = 0; i < SamplePlayer.SAMPLES_PER_BUFFER; i++) {
            double ds = getSample() * Short.MAX_VALUE;
            short ss = (short) Math.round(ds);
            buffer[index++] = (byte) (ss >> 8);
            buffer[index++] = (byte) (ss & 0xFF);
        }
        return SamplePlayer.BUFFER_SIZE;
    }
    
    // writes the sampled audio to disk.
    public void getWriteableSamples(byte[] buffer) {
        int index = 0;
        for (int i = 0; i < (buffer.length/2); i++) {
            double ds = getSample() * Short.MAX_VALUE;
            short ss = (short) Math.round(ds);
            buffer[index++] = (byte) (ss >> 8);
            buffer[index++] = (byte) (ss & 0xFF);
        }
    }

}

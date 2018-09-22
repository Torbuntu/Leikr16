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

/**
 *
 * @author tor
 */
public class BasicOscillator implements SampleProviderIntfc {

    // Instance data
    private WAVESHAPE waveshape;
    private long periodSamples;
    private long sampleNumber;

    /**
     * Waveshape enumeration
     */
    public enum WAVESHAPE {
        SIN, SQU, SAW, NOI, TRI
    }

    /**
     * Basic Oscillator Class Constructor
     *
     * Default instance has SIN waveshape at 1000 Hz
     */
    public BasicOscillator() {
        this.waveshape = WAVESHAPE.SIN;
        periodSamples = (long) (SamplePlayer.SAMPLE_RATE / 440.0);
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
                value = Math.sin(2.0 * Math.PI * x);
                break;
            case SQU:
                if (sampleNumber < (periodSamples / 2)) {
                    value = 1.0;
                } else {
                    value = -1.0;
                }
                break;
            case SAW:
                value = 2.0 * (x - Math.floor(x + 0.5));
                break;
            case NOI:
                value = Math.random();
                break;
            case TRI:
//                value = 1 - Math.abs(x % (2) - 1); 
//                value = Math.abs((x++ % periodSamples) - 2);
//                value = Math.abs(2 - x % (4));
                value = (Math.abs(x-2)-1);
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

}

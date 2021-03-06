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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author tor
 */
public class SamplePlayer extends Thread {
    // AudioFormat parameters

    public static final int SAMPLE_RATE = 44100;
    private static final int SAMPLE_SIZE = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = true;

    // Chunk of audio processed at one time
    public static final int BUFFER_SIZE = 1000;
    public static final int SAMPLES_PER_BUFFER = BUFFER_SIZE / 2;
    
    
    // Instance data
    private AudioFormat format;
    private DataLine.Info info;
    private SourceDataLine auline;
    private boolean done;
    private byte[] sampleData = new byte[BUFFER_SIZE];
    private BasicOscillator provider;

    public SamplePlayer() {

        // Create the audio format we wish to use
        format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, SIGNED, BIG_ENDIAN);
        
        // Create dataline info object describing line format
        info = new DataLine.Info(SourceDataLine.class, format);
    }

    @Override
    public void run() {

        done = false;
        int nBytesRead = 0;

        try {
            // Get line to write data to
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
            auline.start();

            while ((nBytesRead != -1) && (!done)) {
                nBytesRead = provider.getSamples(sampleData);
                if (nBytesRead > 0) {
                    auline.write(sampleData, 0, nBytesRead);
                }
            }
        } catch (LineUnavailableException e) {
            e.getMessage();
        } finally {
            auline.drain();
            auline.close();
        }
    }

    public void startPlayer() {
        if (provider != null) {
            start();
        }
    }

    public void stopPlayer() {
        done = true;
    }

    public void setSampleProvider(BasicOscillator provider) {
        this.provider = provider;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import java.util.ArrayList;

/**
 *
 * @author tor
 */
public class LeikrPalette {
    public ArrayList<Integer> palette;
    Pixmap pixmap;
    
    
    public LeikrPalette(){
        palette = new ArrayList<>();
        pixmap = new Pixmap(new FileHandle(Gdx.files.getExternalStoragePath()+"Leikr/OS/Palette.png"));
        for(int i = 0; i <= 16; i++){
            palette.add(pixmap.getPixel(i, 0));
        }
    }

}

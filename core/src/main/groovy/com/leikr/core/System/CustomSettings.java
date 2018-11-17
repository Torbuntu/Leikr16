/*
 * Copyright 2018 . torbuntu
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
package com.leikr.core.System;

import com.leikr.core.Leikr;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author tor
 */
public class CustomSettings {

    public float fontRed = 1;
    public float fontGreen = 1;
    public float fontBlue = 1;

    public float bgRed = 0;
    public float bgGreen = 0;
    public float bgBlue = 0;
    
    public String fontName = "LeikrFontA.png";
    
    public float glyphWidth = 8;
    public float glyphHeight = 8;
    
    public String userDesktop = "userDesktop";
    public boolean startx = false;
    
    public String customPalette = "";
    
    
    public CustomSettings(){
        loadCustomSettings();
    }
        
    private void loadCustomSettings() {

        Properties prop = new Properties();
        try {
            InputStream stream = new FileInputStream(new File(Leikr.ROOT_PATH + "OS/settings.properties"));
            prop.load(stream);
            
            fontRed = (prop.getProperty("fontRed") != null) ? Float.parseFloat(prop.getProperty("fontRed")) : 1;
            fontGreen = (prop.getProperty("fontGreen") != null) ? Float.parseFloat(prop.getProperty("fontGreen")) : 1;
            fontBlue = (prop.getProperty("fontBlue") != null) ? Float.parseFloat(prop.getProperty("fontBlue")) : 1;
            
            bgRed = (prop.getProperty("bgRed") != null) ? Float.parseFloat(prop.getProperty("bgRed")) : 0;
            bgGreen = (prop.getProperty("bgGreen") != null) ? Float.parseFloat(prop.getProperty("bgGreen")) : 0;
            bgBlue = (prop.getProperty("bgBlue") != null) ? Float.parseFloat(prop.getProperty("bgBlue")) : 0;

            fontName = (prop.getProperty("font") != null) ? prop.getProperty("font") : "LeikrFontA.png";
            
            glyphWidth = (prop.getProperty("glyphWidth") != null) ? Float.parseFloat(prop.getProperty("glyphWidth")) : 8;
            glyphHeight = (prop.getProperty("glyphHeight") != null) ? Float.parseFloat(prop.getProperty("glyphHeight")) : 8;
            
            startx = (prop.getProperty("startx") != null) ? Boolean.parseBoolean(prop.getProperty("startx")) : false;
            
            userDesktop = (prop.getProperty("userDesktop") != null) ? prop.getProperty("userDesktop") : "userDesktop";
            
            customPalette = (prop.getProperty("customPalette") != null) ? prop.getProperty("customPalette") : "";
            
        } catch (IOException | NumberFormatException  ex ) {
            System.out.println(ex.getMessage());
        } 

    }
    
    

}

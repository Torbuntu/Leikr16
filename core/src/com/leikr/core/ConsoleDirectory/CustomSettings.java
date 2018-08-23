/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.ConsoleDirectory;

import com.leikr.core.Leikr;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tor
 */
public class CustomSettings {

    public float fontRed;
    public float fontGreen;
    public float fontBlue;

    public float bgRed;
    public float bgGreen;
    public float bgBlue;
    
    public String fontName;
    
    public float glyphWidth;
    public float glyphHeight;
    
    
    public CustomSettings(){
        loadCustomSettings();
    }
    
    
    public void loadCustomSettings() {

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
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ShellHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ShellHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    

}

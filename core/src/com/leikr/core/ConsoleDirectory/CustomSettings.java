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
    
    
    public CustomSettings(){
        loadCustomSettings();
    }
    
    
    public void loadCustomSettings() {

        Properties prop = new Properties();
        try {
            InputStream stream = new FileInputStream(new File(Leikr.ROOT_PATH + "OS/settings.properties"));
            prop.load(stream);
            System.out.println(prop.getProperty("fontRed"));

            if (prop.getProperty("fontRed") != null) {
                fontRed = Float.parseFloat(prop.getProperty("fontRed"));
            } else {
                fontRed = 1;
            }

            if (prop.getProperty("fontGreen") != null) {
                fontGreen = Float.parseFloat(prop.getProperty("fontGreen"));
            } else {
                fontGreen = 1;
            }

            if (prop.getProperty("fontBlue") != null) {
                fontBlue = Float.parseFloat(prop.getProperty("fontBlue"));
            } else {
                fontBlue = 1;
            }
            
            if(prop.getProperty("font") != null){
                fontName = prop.getProperty("font");
                System.out.println(fontName);
            }else{
                fontName = "LeikrFontA.png";
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ShellHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ShellHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    

}

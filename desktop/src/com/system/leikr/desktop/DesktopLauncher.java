package com.system.leikr.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.system.leikr.Leikr;

public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Leikr-16");
        config.setWindowedMode(260, 160);
        new Lwjgl3Application(new Leikr(), config);
    }
}

package com.torbuntu.leikr16.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.torbuntu.leikr16.Leikr16Console;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 256;
		config.height = 224;
		new LwjglApplication(new Leikr16Console(), config);
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.ConsoleDirectory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.leikr.core.Bios;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor
 */
public class SystemLoader {

    final GroovyClassLoader classLoader;
    Class systemMethodsClass;
    GroovyObject biosObject;

    GroovyScriptEngine groovyScriptEngine;
    Binding binding;
    String[] rootDirectory = new String[]{Gdx.files.getLocalStoragePath()};

    // The class of Bios. Eventually replace the systemMethodsClass and biosObject
    Bios bios = new Bios();

    private void initFileSystem() {
        String RootFileSystem = Gdx.files.getExternalStoragePath();
        new File(RootFileSystem + "LeikrVirtualDrive/").mkdir();
        new File(RootFileSystem + "LeikrVirtualDrive/" + "ChipSpace").mkdir();
        new File(RootFileSystem + "LeikrVirtualDrive/" + "OS").mkdir();
        try {
            new File(RootFileSystem + "LeikrVirtualDrive/OS/" + "Methods.groovy").createNewFile();
            new File(RootFileSystem + "LeikrVirtualDrive/ChipSpace/" + "LeikrGame.groovy").createNewFile();

        } catch (IOException ex) {
            Logger.getLogger(SystemLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RootFileSystem + "LeikrVirtualDrive/ChipSpace/LeikrGame/LeikrGame.groovy"))) {
            String gameContent = "import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine\n"
                    + "\n"
                    + "class LeikrGame extends LeikrEngine{\n"
                    + "\n"
                    + "    def void create(){\n"
                    + "        super.create();// Very important for initializing core engine variables.\n"
                    + "    }\n"
                    + "   \n"
                    + "    def void render(){\n"
                    + "        //Your render code here.\n"
                    + "        drawText(\"Welcome to Leikr!\", 50, 100);\n"
                    + "    }\n"
                    + "}";
            writer.write(gameContent);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RootFileSystem + "LeikrVirtualDrive/OS/Methods.groovy"))) {
            String methodsContent = "class Bios {\n"
                    + "    String BiosVersion = \"V0.0.1\";\n"
                    + "    String SystemName = \"Leikr 16\";    \n"
                    + "    \n"
                    + "    String printSystemInfo(){\n"
                    + "        \"System Name: $SystemName, Bios Version: $BiosVersion \";\n"
                    + "    }\n"
                    + "    \n"
                    + "    String testMethods(){\n"
                    + "        \"Test methods functional.\";\n"
                    + "    } \n"
                    + "}";
            writer.write(methodsContent);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public SystemLoader() throws IOException, InstantiationException, IllegalAccessException {
        classLoader = new GroovyClassLoader();
        if (!Gdx.files.external("LeikrVirtualDrive/").exists() || !Gdx.files.external("LeikrVirtualDrive/ChipSpace/").exists()) {
            initFileSystem();
        }
        systemMethodsClass = classLoader.parseClass(new File(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/Methods.groovy"));
        biosObject = (GroovyObject) systemMethodsClass.newInstance();

    }

    public String getBiosVersion() {
        return bios.getBiosVersion();
    }

    public Object runRegisteredMethod(String[] methodName) {
        Object result;

        switch (methodName[0]) {
            case "mkdir":
                result = bios.mkdir(methodName[1]);
                break;
            case "rm":
                if (methodName[1].equals("-rf")) {
                    result = bios.rmdir(methodName[2]);
                } else {
                    result = bios.rm(methodName[1]);
                }
                break;
            case "ls":
                result = bios.ls();
                break;
            case "exec":
                try {
                    GroovyObject tempObject = (GroovyObject) classLoader.parseClass(new File(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/OS/Methods.groovy")).newInstance();
                    String[] args = Arrays.copyOfRange(methodName, 2, methodName.length);
                    result = tempObject.invokeMethod(methodName[1], args);
                } catch (IOException | IllegalAccessException | InstantiationException | CompilationFailedException e) {
                    result = String.format("`%s` is not a known method. ", methodName[1]) + e.getMessage();
                }

                break;
            default:
                try {
                    if (methodName.length == 1) {

                        result = biosObject.invokeMethod(methodName[0], null);

                    } else {
                        String[] args = Arrays.copyOfRange(methodName, 0, methodName.length);
                        result = biosObject.invokeMethod(methodName[0], args);
                    }
                } catch (Exception e) {
                    result = e.getMessage();
                }
                break;
        }
        return result.toString();
    }

}

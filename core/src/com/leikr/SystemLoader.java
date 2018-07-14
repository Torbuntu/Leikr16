/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import java.util.Arrays;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor
 */
public class SystemLoader {

    final GroovyClassLoader classLoader;
    Class biosClass;
    GroovyObject biosObject;

    // The class of Bios. Eventually replace the biosClass and biosObject
    Bios bios = new Bios();

    public SystemLoader() throws IOException, InstantiationException, IllegalAccessException {
        classLoader = new GroovyClassLoader();
        biosClass = classLoader.parseClass(new File("Root/Bios.groovy"));
        biosObject = (GroovyObject) biosClass.newInstance();

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
                    Class tempClass;
                    if (!methodName[1].contains(".groovy")) {
                        tempClass = classLoader.parseClass(new File(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/" + methodName[1] + ".groovy"));
                    } else {
                        tempClass = classLoader.parseClass(new File(Gdx.files.getExternalStoragePath() + "LeikrVirtualDrive/" + methodName[1]));
                    }
                    GroovyObject tempObject = (GroovyObject) tempClass.newInstance();

                    String[] args = Arrays.copyOfRange(methodName, 3, methodName.length);
                    result = tempObject.invokeMethod(methodName[2], args);
                } catch (IOException | IllegalAccessException | InstantiationException | CompilationFailedException e) {
                    result = String.format("`%s` is not a known script or does not contain method `%s`. ", methodName[1], methodName[2]) + e.getMessage();
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

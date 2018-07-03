/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torbuntu.leikr;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author tor
 */
public class BiosLoader {
    final GroovyClassLoader classLoader;
    Class biosClass;
    GroovyObject biosObject;
    
    Bios bios = new Bios();
   
    
    public BiosLoader() throws IOException, InstantiationException, IllegalAccessException{
        classLoader = new GroovyClassLoader();
        biosClass = classLoader.parseClass(new File("LeikrVirtualDrive/Bios.groovy"));
        biosObject = (GroovyObject)biosClass.newInstance();        
        
    }
    
    public String getBiosVersion(){
        return bios.getBiosVersion();
    }
    
    public Object runRegisteredMethod(String methodName){
        Object result;

        if(methodName.substring(0, 5).equals("mkdir") ){
            String[] dirName = methodName.split(" ");
            bios.mkdir(dirName[1]);
            result = "success";
        }else{
            try{            
                result = biosObject.invokeMethod(methodName, new Object[] {});
            }catch(Exception e){
                result = e.getMessage();
            }
        }
        return result.toString();
    }
    
}

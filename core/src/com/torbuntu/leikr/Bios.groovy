/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.torbuntu.leikr

/**
 *
 * @author tor
 */
class Bios {
    String BiosVersion = "V0.0.1";
    String SystemName = "Leikr 16";
    
    
    
    String printSystemInfo(){
        "System Name: $SystemName, Bios Version: $BiosVersion ";
    }
    
    def mkdir(def name){
        new File("LeikrVirtualDrive/"+name).mkdir();
    }    
}
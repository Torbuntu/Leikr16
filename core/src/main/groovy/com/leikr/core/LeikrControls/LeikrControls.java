/*
 * Copyright 2018 torbuntu.
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
package com.leikr.core.LeikrControls;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.leikr.core.Leikr;
import com.leikr.core.LeikrEngine;

/**
 *
 * @author tor
 */
public class LeikrControls implements InputProcessor, ControllerListener {
    //keyboard buttons
    public boolean rightKeyPressed = false;
    public boolean leftKeyPressed = false;
    public boolean upKeyPressed = false;
    public boolean downKeyPressed = false;
    public boolean zKeyPressed = false;
    public boolean xKeyPressed = false;
    public boolean spaceKeyPressed = false;

    //controller buttons
    boolean buttonAisPressed = false;
    boolean buttonBisPressed = false;
    boolean buttonXisPressed = false;
    boolean buttonYisPressed = false;
    boolean bumperLeftPressed = false;
    boolean bumperRightPressed = false;

    //d-pad buttons
    boolean leftButtonPressed = false;
    boolean rightButtonPressed = false;
    boolean upButtonPressed = false;
    boolean downButtonPressed = false;
        
    Leikr game;
    LeikrEngine engine;
    public LeikrControls(Leikr game, LeikrEngine engine){
        this.game = game;
        this.engine = engine;
    }

    public boolean key(String name) {
        switch (name.toLowerCase()) {
            case "right":
                return rightKeyPressed;
            case "left":
                return leftKeyPressed;
            case "up":
                return upKeyPressed;
            case "down":
                return downKeyPressed;
            case "z":
                return zKeyPressed;
            case "x":
                return xKeyPressed;
            case "space":
                return spaceKeyPressed;
            default:
                return false;
        }
    }
    
    public boolean button(String name) {
        switch (name.toLowerCase()) {
            case "a":
                return buttonAisPressed;
            case "b":
                return buttonBisPressed;
            case "x":
                return buttonXisPressed;
            case "y":
                return buttonYisPressed;
            case "bumper_left":
                return bumperLeftPressed;
            case "bumper_right":
                return bumperRightPressed;
            case "left":
                return leftButtonPressed;
            case "right":
                return rightButtonPressed;
            case "up":
                return upButtonPressed;
            case "down":
                return downButtonPressed;
            default:
                return false;
        }
    }
    
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.RIGHT:
                rightKeyPressed = true;
                return true;
            case Keys.LEFT:
                leftKeyPressed = true;
                return true;
            case Keys.UP:
                upKeyPressed = true;
                return true;
            case Keys.DOWN:
                downKeyPressed = true;
                return true;
            case Keys.Z:
                zKeyPressed = true;
                return true;
            case Keys.X:
                xKeyPressed = true;
                return true;
            case Keys.SPACE:
                spaceKeyPressed = true;
                return true;
        }
        
        return false;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.ESCAPE) {
            game.beginConsole();
            engine.dispose();
            return true;
        }
        switch (keycode) {
            case Keys.RIGHT:
                rightKeyPressed = false;
                return true;
            case Keys.LEFT:
                leftKeyPressed = false;
                return true;
            case Keys.UP:
                upKeyPressed = false;
                return true;
            case Keys.DOWN:
                downKeyPressed = false;
                return true;
            case Keys.Z:
                zKeyPressed = false;
                return true;
            case Keys.X:
                xKeyPressed = false;
                return true;
            case Keys.SPACE:
                spaceKeyPressed = false;
                return true;
        }
        return false;
    }
    
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        // This is the dpad
        switch (value) {
            case north:
                upButtonPressed = true;
                break;
            case south:
                downButtonPressed = true;
                break;
            case east:
                rightButtonPressed = true;
                break;
            case west:
                leftButtonPressed = true;
                break;
            case center:
                upButtonPressed = false;
                downButtonPressed = false;
                rightButtonPressed = false;
                leftButtonPressed = false;
                break;
        }
        return true;
    }
    
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        switch (buttonCode) {
            case 0:
                buttonXisPressed = true;
                return true;
            case 1:
                buttonAisPressed = true;
                return true;
            case 2:
                buttonBisPressed = true;
                return true;
            case 3:
                buttonYisPressed = true;
                return true;
            case 4:
                bumperLeftPressed = true;
                return true;
            case 5:
                bumperRightPressed = true;
                return true;
        }
        return false;
    }
    
    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        switch (buttonCode) {
            case 0:
                buttonXisPressed = false;
                return true;
            case 1:
                buttonAisPressed = false;
                return true;
            case 2:
                buttonBisPressed = false;
                return true;
            case 3:
                buttonYisPressed = false;
                return true;
            case 4:
                bumperLeftPressed = false;
                return true;
            case 5:
                bumperRightPressed = false;
                return true;
        }
        return false;
    }
    
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        //axis 0 = x axis -1 = left 1 = right
        //axis 1 = y axis -1 = up 1 = down
        System.out.println("Axis moved: " + axisCode + " : " + (int) value);
        
        if ((int) value == 0) {
            leftButtonPressed = false;
            rightButtonPressed = false;
            upButtonPressed = false;
            downButtonPressed = false;
        }
        if (axisCode == 1) {
            if (value == 1) {
                downButtonPressed = true;
            } else if (value == -1) {
                upButtonPressed = true;
            }
            return true;
        } else {
            if (value == 1) {
                rightButtonPressed = true;
            } else if (value == -1) {
                leftButtonPressed = true;
            }
            return true;
        }
    }
    
    @Override
    public boolean keyTyped(char character) {
        
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    @Override
    public void connected(Controller controller) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void disconnected(Controller controller) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    
    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    
    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

}

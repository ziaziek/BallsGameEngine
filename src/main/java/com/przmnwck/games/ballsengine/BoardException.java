/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine;

/**
 *
 * @author Przemysław
 */
public class BoardException extends Exception{
    
    String message = "Board definition wrong. Check the minimum and/or the maximum number of fields allowed";
    @Override
    public String getMessage(){
        return message;
    }
}

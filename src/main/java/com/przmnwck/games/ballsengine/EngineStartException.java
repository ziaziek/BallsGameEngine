/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine;

/**
 *
 * @author Przemo
 */
public class EngineStartException extends Exception {
    
    @Override
    public String getMessage(){
        return "Engine couldn't start. Check that nessecary files are present and not corrupted";
    }
}

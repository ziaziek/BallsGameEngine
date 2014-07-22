/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine;

import java.util.EventObject;

/**
 *
 * @author Przemo
 */
public class EngineEvent extends EventObject {
    
    protected EngineState state = EngineState.CONTINUE;

    public void setState(EngineState state) {
        this.state = state;
    }
    protected int player;
    protected Engine engine;

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    
    public EngineState getState() {
        return state;
    }

    public int getPlayer() {
        return player;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    protected int row;
    protected int column;
    
    public EngineEvent(Engine source){
        super(source);
        engine=source;
    }
}

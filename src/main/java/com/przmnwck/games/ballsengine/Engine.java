/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine;

import com.przmnwck.games.ballsengine.interfaces.IEngineListener;
import com.przmnwck.games.ballsengine.interfaces.IRenderer;
import com.przmnwck.games.ballsengine.interfaces.IValuesProvider;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Przemo
 */
public class Engine implements IBoardListener {
    
    
    
    // First move of tha auto player. It's used for determining the strategy of the central points
    private boolean autoFirstMove = true;
    
    private Board board = null;
    private List<IEngineListener> engineListeners=null;
    
    public Board getBoard() {
        return board;
    }
    protected DecisionMaker dc = null;
    private int numberOfPlayers=0;

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    private int automaticPlayer=0;

    public int getAutomaticPlayer() {
        return automaticPlayer;
    }
    private int currentPlayer=1;

    public int getCurrentPlayer() {
        return currentPlayer;
    }
    private IRenderer rend = null;
    private IValuesProvider vp = null;
    
    public IValuesProvider getVp() {
        return vp;
    }

    public void setVp(IValuesProvider vp) {
        this.vp = vp;
    }
    
private boolean gameOver=false;

    public IRenderer getRend() {
        return rend;
    }

    public void setRend(IRenderer rend) {
        this.rend = rend;
    }
    
    /**
     * Construct and initiate the game engine
     * @param numberOfPlayers number of players
     * @param boardSize size of the board
     * @param automaticPlayer ordinal number of the automatic player
     * @throws com.przmnwck.games.ballsengine.BoardException
     */
    public Engine(int numberOfPlayers, int boardSize, int automaticPlayer) throws BoardException {
        this.numberOfPlayers=numberOfPlayers;
        board = new Board(boardSize);
        this.automaticPlayer=automaticPlayer;
        dc = new DecisionMaker(new Assesor(board), this.numberOfPlayers);
        engineListeners=new ArrayList<>(); 
        //The engine has to be added to the listeners after the decision maker to make sure that decisin maker current node hets updated before a move is made.
        board.addListener(this);
    }
    
    public int[] getInputValues() {
        int[] xy = new int[2];
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter field row");
        xy[0] = sc.nextInt();
        System.out.println("Enter field column");
        xy[1] = sc.nextInt();
        return xy;
    }

    public void addListener(IEngineListener l){
        engineListeners.add(l);
    }
    
    public void runSingle(){
            while (board.freeFields() > 0 && !gameOver) {
                int x = -1;
                int y = -1;
                System.out.println("Player " + currentPlayer + "'s turn.");
                while (!board.placeBall(currentPlayer, x, y) && !gameOver) {
                    if (currentPlayer != automaticPlayer) {
                        int[]p=null;
                        if(vp==null){
                          p = getInputValues();  
                        } else {
                            while(p==null || p[0]==-1){
                                try {
                                    p=vp.getInputValues();
                                    Thread.currentThread().wait(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }   
                        }                     
                        x = p[0];
                        y=p[1];
                    } else {
                        System.out.println("Automatic move.");
                        Point auto = dc.decideMove(automaticPlayer);
                        x = auto.x;
                        y = auto.y;
                    }
                }
                if(rend!=null){
                  rend.renderResult(board);  
                }          
                currentPlayer++;
                if (currentPlayer > numberOfPlayers) {
                    currentPlayer = 1;
                }
    }
    }

    
    private void notifyListeners(EngineEvent evt){
        for (IEngineListener l : engineListeners) {
            l.stateChanged(evt);
        }
    }
    
    @Override
    public void ballPlaced(int player, Point R) {
        gameOver = (dc.getAsses() != null && dc.getAsses().isPlayerWinning(player)) || board.freeFields()==0;
        EngineEvent evt = new EngineEvent(this);
        evt.player=player;
        if(gameOver){
            if(board.freeFields()>0){
              evt.state=EngineState.GAMEOVER;  
            } else {
                if(dc.getAsses().isPlayerWinning(player)){
                    evt.state=EngineState.GAMEOVER;
                } else {
                  evt.state=EngineState.DRAW;  
                }
            }         
        } else {
            evt.state = EngineState.CONTINUE;          
        }
        notifyListeners(evt);
        if (!gameOver && automaticPlayer > 0 && 1 + player % numberOfPlayers == automaticPlayer) {
            evt.setState(EngineState.WAIT);
            notifyListeners(evt);
            Point p = dc.decideMove(automaticPlayer, autoFirstMove);
            if (autoFirstMove) {
                autoFirstMove = false;
            }
            board.placeBall(automaticPlayer, p.x, p.y);
        }
    }
}

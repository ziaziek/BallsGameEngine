/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Przemysław
 */


public class Board implements Serializable, Cloneable{
    int size;
    int[][] fields;
    List<IBoardListener> boardListeners = null;

    public List<IBoardListener> getBoardListeners() {
        if(boardListeners==null){
            boardListeners = new ArrayList<IBoardListener>();
        }
        return boardListeners;
    }
    
    public int getSize() {
        return size;
    }

    /**
     * Fields of the board. The numbers in the array are numbers of players whose ball are in that specific field
     * @return array of players' balls
     */
    public int[][] getFields() {
        return fields;
    }
    
    /**
     * Create a rectangular board
     * @param board_size number of rows or columns of the board
     * @throws BoardException 
     */
    public Board(int board_size) throws BoardException{
        if(board_size>0 && board_size<100){
            size=board_size;
            fields = new int[size][size];
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    fields[i][j]=0;
                }
            }
        } else {
            throw new BoardException();
        }
    }
    
    /**
     * Place ball on the board
     * @param player number of player placing the ball
     * @param row number oof row of the field
     * @param column number of column of the field
     * @return True - ball has been placed successsfully, False - otherwise
     */
    public boolean placeBall(int player, int row, int column){
        if(row>=0 && column>=0 && player>0 && row<size && column<size){
            if(fields[row][column]==0){
                fields[row][column]=player;
                notifyBoardListeners(player);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Registers a board event listeners
     * @param l Listening object
     */
    public void addListener(IBoardListener l){
        if( !getBoardListeners().contains(l)){
            getBoardListeners().add(l);
        }        
    }
    
    public int freeFields(){
        int r=0;
        for(int i=0; i< getSize(); i++){
            for(int j=0;j<getSize();j++){
                if(fields[i][j]==0){
                    r++;
                }
            }
        }
        return r;
    }
    
    /**
     * Next field on  of the given coordinates. Currently only Left and Right directions
     * @param x
     * @param y
     * @param direction
     * @return vector of coordinates of the next field
     */
    public int[] getNextField(int row, int column, SearchDirections direction){
        int[] ret = new int[2];
        int xadd=1;
        if(direction== SearchDirections.LEFT){
            xadd=-1;
        }
        ret[1]=(column+xadd)%getSize();
        if(ret[1]<0){
                ret[1]+=getSize();
            }
        if((column+xadd)<this.getSize() && (column+xadd)>=0){
            ret[0]=row;
        } else {           
            ret[0]=(row+xadd)%getSize();
            if(ret[0]<0){
                ret[0]+=getSize();
            }
        }
        return ret;
    }
    
    protected void notifyBoardListeners(int p){
        for(IBoardListener l : getBoardListeners()){
            l.ballPlaced(p);
        }
    }
    
    /**
     * Clones the Board object, except for the listeners list
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException{
        try {
            Board b = new Board(this.getSize());
            for(int i=0;i<getSize();i++){
                for(int j=0; j<getSize(); j++){
                    b.fields[i][j] = this.fields[i][j];
                }
            }
            return b;
        } catch (BoardException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean isMovePossible(int x, int y){
        return x>=0 && y>=0 && x<getSize() && y<getSize() && fields[x][y]==0;
    }
    
    public Point[] getCentralPoints(){      
        double r= getSize()/2;
        int x0,x1,y0,y1;
        x0=(int)r-1;
        if(getSize()%2==0){
             x1=(int)r;
        } else{      
            if(getSize()==3){
                x0=1; x1=1;
            } else {
               x1=(int)r+1; 
            }        
        }
        y0=x0; y1=x1;
        return getPointsArray(x0, y0, x1, y1);
    }
    
    protected Point[] getPointsArray(int x0, int y0, int x1, int y1){
        Point[] ret=new Point[(x1-x0+1)*(y1-y0+1)];
        int counter=0;
        for(int p=x0; p<=x1; p++){
            for(int s=y0; s<=y1; s++){
                ret[counter]=new Point(p,s);
                counter++;
            }
        }
        return ret;
    }
}

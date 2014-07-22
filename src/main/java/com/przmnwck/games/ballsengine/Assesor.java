/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine;

/**
 * Ta klasa ocenia pozycję i decyduje, czy jest wygrana oraz kto wygrywa
 * @date 2014-06-07
 * @author Przemysław
 */
public class Assesor implements IBoardListener {
      
    int seq_winning;
    Board board = null;

    public Board getBoard() {
        return board;
    }
    
    /**
     * Creator
     * @param bSize Size of the board (number of fields in a row (column)
     */
    public Assesor(Board b){
        seq_winning=getNumberOfWinningSequence(b.getSize(), b.getSize());
        board = b;
        b.addListener(this);
    }
    
    /**
     * Creator
     * @param b Board
     * @param seqWin Number of balls in line that make a winning game
     */
    public Assesor(Board b, int seqWin){
        seq_winning=getNumberOfWinningSequence(b.getSize(), seqWin);
        board = b;
        b.addListener(this);
    }
    
    protected final int getNumberOfWinningSequence(int bSize, int sn){
        if(sn<=bSize){
            return (int)(0.8 *bSize)+1;
        } else {
            return bSize;
        }
    }
    
    /**
     * Asses the current position on the board and return the number of the winning player
     * @param b Board
     * @param numberOfPlayers number of players
     * @return number of the winning player
     */
    public int asses(Board b, int numberOfPlayers){
        int sq=0;
        int i=1;
        while(sq==0 && i<numberOfPlayers){
            if(!isPlayerWinning(b,i)){
                i++;
            }else{
                sq=i;
            }
        }
        return sq;
    }
    
    public boolean isPlayerWinning(int p){
        if(board!=null){
            return isPlayerWinning(board, p);
        } else {
            return false;
        }
    }
    
    public boolean isPlayerWinning(Board b, int p){
        int rsq=0, csq=0;

        for (int i = 0; i < b.getSize(); i++) {
            rsq = 0;
            csq = 0;
            for (int j = 0; j < b.getSize(); j++) {
                rsq = checkFields(b, i, j, p, rsq);
                csq = checkFields(b, j, i, p, csq);
                if (rsq == seq_winning || csq == seq_winning) {
                    return true;
                }
            }
        }
        
        //diagonals
        int kj, jk;
        for(int k=(-b.getSize()+1); k<=(b.getSize()-1); k++){
            csq=0;rsq=0;
            for(int i=0; i<b.getSize(); i++){
                kj=i+k; //right direction diagonals
                jk=b.getSize()-1-i-k; //left direction diagonals
                if(kj>=0 && kj< b.getSize()){
                    csq = checkFields(b, i, kj, p, csq);
                    if(csq==seq_winning){
                        return true;
                    }
            }
                if(jk>=0 && jk<b.getSize()){
                    rsq = checkFields(b, i, jk, p, rsq);
                    if(rsq==seq_winning){
                        return true;
                    }
                }
        }
        }
        return false;
    }

    private int checkFields(Board b, int i, int kj, int p, int csq) {
        if(b.getFields()[i][kj]==p){
            csq++;
        } else {
            csq=0;
        }
        return csq;
    }

    public void ballPlaced(int player) {
        if(isPlayerWinning(board, player)){
            //implement something more
            System.out.println("Player "+player+" wins!");
        }
    }
}

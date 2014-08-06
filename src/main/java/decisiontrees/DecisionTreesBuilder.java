/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees;

import com.przmnwck.games.ballsengine.Assesor;
import com.przmnwck.games.ballsengine.Board;
import com.przmnwck.games.ballsengine.BoardException;
import com.przmnwck.games.ballsengine.DecisionMaker;
import com.przmnwck.games.ballsengine.trees.BoardNode;
import com.przmnwck.games.ballsengine.trees.Tree;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Przemo
 */
public class DecisionTreesBuilder {
    
    public final String DECISION_TREES_EXTENSION=".dtf";
    int np;
    int bs;
    Tree currentTree;
    Assesor asses;
    
    public Tree getCurrentTree() {
        return currentTree;
    }
    
    public DecisionTreesBuilder(int numberOfPlayers, int boardSize){
        np=numberOfPlayers;
        bs = boardSize;
    }
    
    public Tree buildTree(){
        try {
            int player = 1; //start from scratch
            Board board = new Board(bs);
            asses= new Assesor(board);
            Map<Point, Integer> fp = new HashMap<Point, Integer>();
            BoardNode bn0=new BoardNode(0, new Point(-1,-1));
            currentTree= new Tree(bn0);
               for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if(board.isMovePossible(i, j)) {try {
                        //move possible at all
                        BoardNode bn = new BoardNode(new Point(i,j)); 
                        bn0.addChild(bn);
                          simBoard(bn, board, player, player, i,j, true, fp, new Point(i,j));                      
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(DecisionMaker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
               return currentTree;
        } catch (BoardException ex) {
            Logger.getLogger(DecisionTreesBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }      
        return null;
    }
    
        protected void simBoard(BoardNode bn, Board b, int playerToMove, final int currentPlayer, int row, int column, final boolean firstWon, Map<Point, Integer> fieldsMap, final Point startPoint) throws CloneNotSupportedException{
        Board lb=(Board) b.clone();
//        System.out.println("In simBoard. Level="+bn.getLevel());
//        System.out.println(decisionTree.getNodesOfLevel(1).size());
        if(!(lb.placeBall(playerToMove, row, column) && asses.isPlayerWinning(lb, playerToMove) && playerToMove==currentPlayer)){
            for (int a_row = 0; a_row < lb.getSize(); a_row++) {
                for (int a_column = 0; a_column < lb.getSize(); a_column++) {
                    if (!asses.isPlayerWinning(lb, playerToMove) && lb.isMovePossible(a_row, a_column)) {
                        BoardNode bnp = new BoardNode(new Point(a_row, a_column));
                        bn.addChild(bnp);
                        simBoard(bnp, lb, 1 + ((playerToMove) % np), currentPlayer, a_row, a_column, firstWon, fieldsMap, startPoint);                     
                    } else {
                        if(asses.isPlayerWinning(lb, playerToMove)){
                            bn.setWin(playerToMove);
                        }
                    }
                }
            }           
        }
        lb=null;
    }
        
    public void saveTree(Tree t, String dirLocation, int boardSize, int numberOfPlayers) throws FileNotFoundException, IOException{
        if(currentTree!=null && !currentTree.getNodesOfLevel(1).isEmpty()){
            FileOutputStream str = new FileOutputStream(getFileName(dirLocation, boardSize, numberOfPlayers));
            ObjectOutputStream ostr = new ObjectOutputStream(str);
            ostr.writeObject(currentTree);
            ostr.close();
            str.close();
        }
    }
    
    public void loadTree(int boardSize, int numberOfPlayers, String dirLocation) throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream istr = new FileInputStream(getFileName(dirLocation, boardSize, numberOfPlayers));
        ObjectInputStream oistr = new ObjectInputStream(istr);
        currentTree = (Tree) oistr.readObject();
    }

    private String getFileName(String dirLocation, int boardSize, int numberOfPlayers) {
        return dirLocation+"/dt_"+boardSize+"_"+numberOfPlayers+ DECISION_TREES_EXTENSION;
    }
    
}

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/**
 *
 * @author Przemo
 */
public class DecisionTreesBuilder {
    
    int np;
    int bs;
    Tree currentTree;
    Assesor asses;
    Logger log = Logger.getLogger(DecisionTreesBuilder.class.getName());
    
    public Tree getCurrentTree() {
        return currentTree;
    }
    
    public DecisionTreesBuilder(int numberOfPlayers, int boardSize){
        np=numberOfPlayers;
        bs = boardSize;
    }
    
    public Tree buildTree(){
        try {
            return buildTree(new Board(bs));
        } catch (BoardException ex) {
            Logger.getLogger(DecisionTreesBuilder.class.getName()).log(Level.ERROR, null, ex);
            return null;
        }
    }
    
    
    public Tree buildTree(Board boardGiven){
        if(boardGiven!=null){
            try {
                Board board = (Board) boardGiven.clone();
                log.info("Starting building tree for board");
                int player = 1; //start from scratch
                asses= new Assesor(board);
                BoardNode bn0=new BoardNode(0, new Point(-1,-1));
                currentTree= new Tree(bn0);
                for (int i = 0; i < board.getSize(); i++) {
                    for (int j = 0; j < board.getSize(); j++) {
                        if(board.isMovePossible(i, j)) 
                        {try {
                            //move possible at all
                            BoardNode bn = new BoardNode(new Point(i,j));
                            bn0.addChild(bn);
                            log.info("New child added to the tree");                      
                            simBoard(bn, board, player, player, i,j, true, new Point(i,j));
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(DecisionMaker.class.getName()).log(Level.ERROR, null, ex);
                        }
                        }
                    }
                }
                return currentTree;
            } catch (CloneNotSupportedException ex) {
                log.error(ex.getMessage());
            }
        }     
        return null;
    }
    
        protected void simBoard(BoardNode bn, Board b, int playerToMove, final int currentPlayer, int row, int column, final boolean firstWon, final Point startPoint) throws CloneNotSupportedException{
        Board lb=(Board) b.clone();
//        System.out.println("In simBoard. Level="+bn.getLevel());
//        System.out.println(decisionTree.getNodesOfLevel(1).size());
        if(!(lb.placeBall(playerToMove, row, column) && asses.isPlayerWinning(lb, playerToMove) && playerToMove==currentPlayer)){
            for (int a_row = 0; a_row < lb.getSize(); a_row++) {
                for (int a_column = 0; a_column < lb.getSize(); a_column++) {
                    if (!asses.isPlayerWinning(lb, playerToMove) && lb.isMovePossible(a_row, a_column)) {
                        BoardNode bnp = new BoardNode(new Point(a_row, a_column));
                        bn.addChild(bnp);
                        simBoard(bnp, lb, 1 + ((playerToMove) % np), currentPlayer, a_row, a_column, firstWon, startPoint);                     
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
        
    public String saveTree(Tree t, String dirLocation, int boardSize, int numberOfPlayers) throws FileNotFoundException, IOException{
        if(currentTree!=null && !currentTree.getNodesOfLevel(1).isEmpty()){
            String fn = getFileName(dirLocation, boardSize, numberOfPlayers);
            FileOutputStream str = new FileOutputStream(fn);
            ObjectOutputStream ostr = new ObjectOutputStream(str);
            ostr.writeObject(currentTree);
            ostr.close();
            str.close();
            return fn;
        } else{
            return null;
        }
    }
   

    
    private String getFileName(String dirLocation, int boardSize, int numberOfPlayers) {
        return dirLocation+"/dt_"+boardSize+"_"+numberOfPlayers+ new Date().getTime()+ DecisionTreesBuilderSettings.DECISION_TREES_EXTENSION;
    }
    
}

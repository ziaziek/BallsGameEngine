/*
 * The class will build a_row decision tree and will decide what move to make based
 * on that tree. Namely, the shortest path leading to victory is taken and the root node is returned
 */
package com.przmnwck.games.ballsengine;

import com.przmnwck.games.ballsengine.trees.BoardNode;
import com.przmnwck.games.ballsengine.trees.DecisionTreeSearchOption;
import com.przmnwck.games.ballsengine.trees.Tree;
import com.przmnwck.games.ballsengine.trees.TreeNode;
import com.przmnwck.games.ballsengine.trees.TreeNodeOperator;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Przemysław
 */
public class DecisionMaker {
    
    Assesor asses = null;
    Random r = null;
    private final int DECISION_SIZE_LIMIT=30;
    private final int MAX_DEFAULT_TREE_DEPTH=8;
    private double freeFieldsRandomMoves;
    private Map<TreeNode, List<TreeNode>> leavesOfTheNode;
    protected int treeDepth = MAX_DEFAULT_TREE_DEPTH;

    public int getTreeDepth() {
        if(asses!=null && asses.getBoard()!=null && asses.getBoard().freeFields()> freeFieldsRandomMoves){
            return 2;
        } else {
          return treeDepth;  
        }      
    }

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }
    
    Tree decisionTree=null;
    
    public Assesor getAsses() {
        return asses;
    }

    public int getNumberOfPlayers() {
        return np;
    }
    int np=0;
    
    public DecisionMaker(Assesor a, int numberOfPlayers){
        asses=a;
        np=numberOfPlayers;
        r = new Random();
        freeFieldsRandomMoves=assessFireFieldsRandomMoveFraction(asses.getBoard())* asses.getBoard().getSize()*asses.getBoard().getSize();
    }
    
    private double assessFireFieldsRandomMoveFraction(Board b){
        if(b!=null){
            if(b.getSize()==3){
                return 1;
            }
            if(b.getSize()==4){
                return .74;
            }
            if(b.getSize()==5){
                return .66;
            }
            return .55;
        }
        return 0.0;
    }
    
    public int[] decideMove(int player, boolean centralStrategyOn){
        int[] ret= null; 
        if(centralStrategyOn){
            Point[] cp = asses.getBoard().getCentralPoints();
            if (cp!=null){
                Random r = new Random();
                int s=Math.abs(r.nextInt()%cp.length);
                int counter=0;
                while(counter<cp.length && !asses.getBoard().isMovePossible(cp[s].x, cp[s].y)){
                    s=r.nextInt()%cp.length;
                    counter++;
                }
                if(counter<cp.length){
                    ret = new int[2];
                    ret[0]=cp[s].x; ret[1]=cp[s].y;
                }
            }
        }
        if(ret==null){
            return decideMove(player);
        } else {
            return ret;
        }
    }
    
    /**
     * Decide on what move to make
     * @param player number of the player for whom decision is made
     * @return vector of coordinates where to place the ball.
     */
    public int[] decideMove(int player) {
        int[] v = new int[2];
        Board b = null;
        
        try {
            b = (Board) asses.getBoard().clone();

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DecisionMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (b != null) {
            BoardNode bn0=new BoardNode(0, new Point(-1,-1));
            decisionTree= new Tree(bn0);
               for (int i = 0; i < b.getSize(); i++) {
                for (int j = 0; j < b.getSize(); j++) {
                    if(b.isMovePossible(i, j)) {try {
                        //move possible at all
                        BoardNode bn = new BoardNode(new Point(i,j)); 
                        bn0.addChild(bn);
                        if(b.freeFields()<=2*getTreeDepth()){
                          simBoard(bn, b, player, player, i,j, b.getSize()<DECISION_SIZE_LIMIT, new Point(i,j));  
                        }                      
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(DecisionMaker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } 
            
            //TODO: implement way of determining whether any opponent wins in one move and try to avoid it as priority
            //find the best point
            
            Point r = findBestPoint(player);
                v[0]=(int) r.getX();
                v[1]=(int) r.getY();           
        }
        return v;
    }
    
    
    protected boolean get_eval(BoardNode bn, int cp){
        if(bn.getChildren().isEmpty()){ 
            
            return bn.getWin()==cp;
        } else if(bn.getOperator()== TreeNodeOperator.OR){
            for(TreeNode b: bn.getChildren()){
                if(b instanceof BoardNode && get_eval((BoardNode)b, cp)){
                    return true;
                }
            }
            return false;
        } else if(bn.getOperator()==TreeNodeOperator.AND){
            for(TreeNode b: bn.getChildren()){
                if(b instanceof BoardNode && !get_eval((BoardNode)b, cp)){
                    return false;
                }
            }
            return true;
        }
        return false; //the node doesn't lead to victory, but can lead to a draw
    }
    
    protected TreeNode getDecisionBranchNode(int cp) {
        TreeNode minTn = null;
        List<TreeNode> firstLevelNodes = decisionTree.getNodesOfLevel(1);
        
        if (asses.getBoard().freeFields() >= freeFieldsRandomMoves) {
            //GET RANDOM NODE
            minTn = firstLevelNodes.get(Math.abs(r.nextInt()) % firstLevelNodes.size());
        } else {
            //FIND BEST NODE
            //TODO: use randomizer to loop through the nodes
            for (TreeNode tn : firstLevelNodes) {
                if (tn instanceof BoardNode && get_eval((BoardNode) tn, cp)) {
                    minTn = tn;
                    break;
                }
            }
        }
        return minTn;
    }
    
    /**
     * Find point whose path is shortest in the tree
     * @param fp
     * @return 
     */
    protected Point findBestPoint(int cp){
        //TODO: avoid branches that lead to win of the opponent on all leaves
        //find the quickest way to win
        TreeNode minTn=getDecisionBranchNode(cp);      
        if(minTn==null) {
            int ix=0;
            List<TreeNode> fl = decisionTree.getNodesOfLevel(1);
            if(r!=null){
                ix=Math.abs(r.nextInt()%fl.size());
            }
            minTn=decisionTree.getNodesOfLevel(1).get(ix);
        }
        if(minTn!=null && minTn instanceof BoardNode){
            return ((BoardNode)minTn).getStartPoint();
        }
        //shouldn't get here, but just in case and to make the compiler happy
        return new Point(-1,-1);
    }
    
    protected void simBoard(BoardNode bn, Board b, int playerToMove, final int currentPlayer, int row, int column, final boolean firstWon,  final Point startPoint) throws CloneNotSupportedException{
       
        Board lb1 = (Board) b.clone();
//        System.out.println("In simBoard. Level="+bn.getLevel());
//        System.out.println(decisionTree.getNodesOfLevel(1).size());
        if(lb1.placeBall(playerToMove, row, column) && asses.isPlayerWinning(lb1, playerToMove) ){ //mark the node as winning
            bn.setWin(playerToMove);
            //propagate to the parent from the leaf, so that this node isn't chosen
           if(bn.getParent()!=null && bn.getParent() instanceof BoardNode && playerToMove!=currentPlayer){
               ((BoardNode)bn.getParent()).setOperator(TreeNodeOperator.AND);
           }
        } else {
            Board lb = (Board) b.clone();
            if (!(lb.placeBall(playerToMove, row, column) && asses.isPlayerWinning(lb, playerToMove) && playerToMove == currentPlayer) && bn.getLevel() < getTreeDepth()) {
                for (int a_row = 0; a_row < lb.getSize(); a_row++) {
                    for (int a_column = 0; a_column < lb.getSize(); a_column++) {
                        if (!asses.isPlayerWinning(lb, playerToMove) && lb.isMovePossible(a_row, a_column)) {
                            BoardNode bnp = new BoardNode(new Point(a_row, a_column));
                            bn.addChild(bnp);
                            simBoard(bnp, lb, 1 + ((playerToMove) % getNumberOfPlayers()), currentPlayer, a_row, a_column, firstWon,  startPoint);
                        } else {
                            if (asses.isPlayerWinning(lb, playerToMove)) {
                                bn.setWin(playerToMove);
                            }
                        }
                    }
                }
            }
        }
    }
}

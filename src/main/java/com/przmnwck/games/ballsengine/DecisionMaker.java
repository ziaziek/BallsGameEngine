/*
 * The class will build a_row decision tree and will decide what move to make based
 * on that tree. Namely, the shortest path leading to victory is taken and the root node is returned
 */
package com.przmnwck.games.ballsengine;

import com.przmnwck.games.ballsengine.trees.BoardNode;
import decisiontrees.DecisionTreeSearchOption;
import com.przmnwck.games.ballsengine.trees.Tree;
import com.przmnwck.games.ballsengine.trees.TreeNode;
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
            Map<Point, Integer> fp = new HashMap<Point, Integer>();
            BoardNode bn0=new BoardNode(0, new Point(-1,-1));
            decisionTree= new Tree(bn0);
               for (int i = 0; i < b.getSize(); i++) {
                for (int j = 0; j < b.getSize(); j++) {
                    if(b.isMovePossible(i, j)) {try {
                        //move possible at all
                        BoardNode bn = new BoardNode(new Point(i,j)); 
                        bn0.addChild(bn);
                        if(b.freeFields()<=2*getTreeDepth()){
                          simBoard(bn, b, player, player, i,j, b.getSize()<DECISION_SIZE_LIMIT, fp, new Point(i,j));  
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
    
    protected TreeNode getDecisionBranchNode(int cp, DecisionTreeSearchOption option) {
        int m = -1;
        int k;
        TreeNode minTn = null;
        List<TreeNode> firstLevelNodes = decisionTree.getNodesOfLevel(1);
        if (asses.getBoard().freeFields() >= freeFieldsRandomMoves ) {
            //GET RANDOM NODE
            minTn = firstLevelNodes.get(Math.abs(r.nextInt()) % firstLevelNodes.size());
        } else {
            //FIND BEST NODE
            for (TreeNode tn : firstLevelNodes) {
                if (!containsAllLoosingPaths(tn, cp)){
                  if (option.equals(DecisionTreeSearchOption.SHORTEST_BRANCH)) {
                    k = decisionTree.getMinBranchLevel(tn);
                    if (((k - 1) % getNumberOfPlayers()) == 0 && (k < m || m == -1)) {
                        minTn = tn;
                        m = k;
                    }
                } else if (option.equals(DecisionTreeSearchOption.LONGEST_BRANCH)) {
                    //doesn't really matter who wins, just keep the game going, and have a_row chance to block any incomming winning possibility
                    k = decisionTree.getMaxBranchLevel(tn);
                    if ((k > m || m == -1) && (decisionTree.getMinBranchLevel(tn) - tn.getLevel()) >= getNumberOfPlayers()) {
                        minTn = tn;
                        m = k;
                    }
                }  
                }
            }
        }
        return minTn;
    }
    
    /**
     * Check if the given tree contains a path that leads to all loosing option
     * @param t Tree to look up to
     * @return 
     */
    protected boolean containsAllLoosingPaths(TreeNode tn, int cp) {
        Tree t = new Tree(tn);
        int ml = t.getMaxLevel();
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        t.fillNodesOfLevel(tn, nodes, ml - 2);
        if (!nodes.isEmpty()) {
            for (TreeNode tn1 : nodes) {
                List<TreeNode> leaves = new Tree(tn1).findAllLeaves();
                int s = 0;
                for (TreeNode n : leaves) {
                    if (n.getLevel() % getNumberOfPlayers() + 1 != cp) {
                        s++;
                    }
                }
                if (s == (ml * (ml - 1))) {
                    return true;
                }
            }  
        }       
        return false;
    }
    
    /**
     * Find point whose path is shortest in the tree
     * @param fp
     * @return 
     */
    protected Point findBestPoint(int cp){
        //TODO: avoid branches that lead to win of the opponent on all leaves
        //find the quickest way to win
        TreeNode minTn=getDecisionBranchNode(cp, DecisionTreeSearchOption.SHORTEST_BRANCH);      
        if(minTn==null){
            //try to find th longest solution then
            minTn=getDecisionBranchNode(cp, DecisionTreeSearchOption.LONGEST_BRANCH);          
        }
        if(minTn==null) {
            minTn=decisionTree.getNodesOfLevel(1).get(0);
        }
        if(minTn!=null && minTn instanceof BoardNode){
            return ((BoardNode)minTn).getStartPoint();
        }
        //shouldn't get here, but just in case and to make the compiler happy
        return new Point(-1,-1);
    }
    
    protected void simBoard(BoardNode bn, Board b, int playerToMove, final int currentPlayer, int row, int column, final boolean firstWon, Map<Point, Integer> fieldsMap, final Point startPoint) throws CloneNotSupportedException{
        Board lb=(Board) b.clone();
//        System.out.println("In simBoard. Level="+bn.getLevel());
//        System.out.println(decisionTree.getNodesOfLevel(1).size());
        if(!(lb.placeBall(playerToMove, row, column) && asses.isPlayerWinning(lb, playerToMove) && playerToMove==currentPlayer) && bn.getLevel()<getTreeDepth()){
            for (int a_row = 0; a_row < lb.getSize(); a_row++) {
                for (int a_column = 0; a_column < lb.getSize(); a_column++) {
                    if (!asses.isPlayerWinning(lb, playerToMove) && lb.isMovePossible(a_row, a_column)) {
                        BoardNode bnp = new BoardNode(new Point(a_row, a_column));
                        bn.addChild(bnp);
                        simBoard(bnp, lb, 1 + ((playerToMove) % getNumberOfPlayers()), currentPlayer, a_row, a_column, firstWon, fieldsMap, startPoint);                     
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
}

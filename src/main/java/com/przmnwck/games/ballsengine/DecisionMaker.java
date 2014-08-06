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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Przemysław
 */
public class DecisionMaker implements IBoardListener {
    
    Assesor asses = null;
    Random r = null;
    private final int DECISION_SIZE_LIMIT=30;
    private final int MAX_DEFAULT_TREE_DEPTH=8;
    private double freeFieldsRandomMoves;
    private Map<TreeNode, List<TreeNode>> leavesOfTheNode;
    protected int treeDepth = MAX_DEFAULT_TREE_DEPTH;
    protected TreeNode currentNode=null;

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
    
    public DecisionMaker(Assesor a, int numberOfPlayers, Tree decisionTree){
        asses=a;
        np=numberOfPlayers;
        this.decisionTree= decisionTree;
        currentNode=decisionTree.getNodesOfLevel(0).get(0);
        r = new Random();
        freeFieldsRandomMoves=assessFreeFieldsRandomMoveFraction(asses.getBoard())* asses.getBoard().getSize()*asses.getBoard().getSize();
        asses.getBoard().addListener(this);
    }
    
    private double assessFreeFieldsRandomMoveFraction(Board b){
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
            decisionTree= new Tree(currentNode);
            
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
        List<TreeNode> firstLevelNodes = decisionTree.getNodesOfLevel(currentNode.getLevel()+1);
        if (asses.getBoard().freeFields() >= freeFieldsRandomMoves ) {
            //GET RANDOM NODE
            minTn = firstLevelNodes.get(Math.abs(r.nextInt()) % firstLevelNodes.size());
        } else {
            //FIND BEST NODE
            for (TreeNode tn : firstLevelNodes) {
                  if (option.equals(DecisionTreeSearchOption.SHORTEST_BRANCH)) {
                    k = decisionTree.getMinBranchLevel(tn)-currentNode.getLevel();
                    if (((k - 1) % getNumberOfPlayers()) == 0 && (k < m || m == -1)) {
                        minTn = tn;
                        m = k;
                    }
                } else if (option.equals(DecisionTreeSearchOption.LONGEST_BRANCH)) {
                    //doesn't really matter who wins, just keep the game going, and have a_row chance to block any incomming winning possibility
                    k = decisionTree.getMaxBranchLevel(tn)-currentNode.getLevel();
                    if ((k > m || m == -1) && (decisionTree.getMinBranchLevel(tn) - tn.getLevel()) >= getNumberOfPlayers()) {
                        minTn = tn;
                        m = k;
                    }
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
        TreeNode minTn=getDecisionBranchNode(cp, DecisionTreeSearchOption.SHORTEST_BRANCH);      
        if(minTn==null){
            //try to find th longest solution then
            minTn=getDecisionBranchNode(cp, DecisionTreeSearchOption.LONGEST_BRANCH);          
        }
        if(minTn==null) {
            minTn=decisionTree.getNodesOfLevel(currentNode.getLevel()+1).get(0);
        }
        if(minTn!=null && minTn instanceof BoardNode){
            return ((BoardNode)minTn).getStartPoint();
        }
        //shouldn't get here, but just in case and to make the compiler happy
        return new Point(-1,-1);
    }

    public void ballPlaced(int player, Point r) {
        List<TreeNode> fnodes = decisionTree.getNodesOfLevel(currentNode.getLevel() + 1);
        for (TreeNode tn : fnodes) {
            if (tn instanceof BoardNode && ((BoardNode) tn).getStartPoint().x == r.x && ((BoardNode) tn).getStartPoint().y == r.y) {
                currentNode = tn;
                break;
            }
        }
    }
}

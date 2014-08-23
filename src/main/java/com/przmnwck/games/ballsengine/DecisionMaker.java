/*
 * The class will build a_row decision tree and will decide what move to make based
 * on that tree. Namely, the shortest path leading to victory is taken and the root node is returned
 */
package com.przmnwck.games.ballsengine;

import com.przmnwck.games.ballsengine.trees.BoardNode;
import com.przmnwck.games.ballsengine.trees.Tree;
import com.przmnwck.games.ballsengine.trees.TreeNode;
import decisiontrees.DecisionTreeSearchOption;
import decisiontrees.DecisionTreesBuilder;
import java.awt.Point;
import java.util.List;
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
    protected int treeDepth = MAX_DEFAULT_TREE_DEPTH;

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }
    
    private Tree decisionTree=null; 

    public Tree getDecisionTree() {
        if(decisionTree==null){
            decisiontrees.DecisionTreesBuilder builder = new DecisionTreesBuilder(getNumberOfPlayers(), asses.getBoard().getSize());
            decisionTree=builder.buildTree(asses.getBoard());    
        }
        return decisionTree;
    }
    
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
        asses.getBoard().addListener(this);
    }
    
    public Point decideMove(int player, boolean centralStrategyOn){
        Point ret= null; 
        if(centralStrategyOn){
            Point[] cp = asses.getBoard().getCentralPoints();
            ret = getRandomField(cp);
        }
        if(ret==null){
            return decideMove(player);
        } else {
            return ret;
        }
    }

    private Point getRandomField(Point[] fieldsToLook) {
        Point ret = null;
        if (fieldsToLook!=null){
            Random randomNumber = new Random();
            int s=Math.abs(randomNumber.nextInt()%fieldsToLook.length);
            int counter=0;
            while(counter<fieldsToLook.length && !asses.getBoard().isMovePossible(fieldsToLook[s].x, fieldsToLook[s].y)){
                s=randomNumber.nextInt()%fieldsToLook.length;
                counter++;
            }
            if(counter<fieldsToLook.length){
                ret = fieldsToLook[s];
            }
        }
        return ret;
    }
    
    /**
     * Decide on what move to make
     * @param player number of the player for whom decision is made
     * @return vector of coordinates where to place the ball.
     */
    public Point decideMove(int player) {
        Board b = null;
        
        try {
            b = (Board) asses.getBoard().clone();

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DecisionMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (b != null) {
            return findBestPoint(player);         
        }
        return null;
    }
    
    protected TreeNode getDecisionBranchNode(int cp, DecisionTreeSearchOption option) {
        int m = -1;
        int k;
        TreeNode minTn = null;
       
        if (asses.getBoard().freeFields() >= decisiontrees.DecisionTreesBuilderSettings.getBoardFreeFieldsSetting(asses.getBoard().getSize(), getNumberOfPlayers()) ) {
            //GET RANDOM FIELD
            minTn = new BoardNode(getRandomField(asses.getBoard().getPointFields()));
        } else {
            //FIND BEST NODE
             List<TreeNode> firstLevelNodes = getDecisionTree().getNodesOfLevel(1);
            for (TreeNode tn : firstLevelNodes) {
                  if (option.equals(DecisionTreeSearchOption.SHORTEST_BRANCH)) {
                    k = getDecisionTree().getMinBranchLevel(tn);
                    if (((k - 1) % getNumberOfPlayers()) == 0 && (k < m || m == -1)) {
                        minTn = tn;
                        m = k;
                    }
                } else if (option.equals(DecisionTreeSearchOption.LONGEST_BRANCH)) {
                    //doesn't really matter who wins, just keep the game going, and have a_row chance to block any incomming winning possibility
                    k = getDecisionTree().getMaxBranchLevel(tn);
                    if ((k > m || m == -1) && (getDecisionTree().getMinBranchLevel(tn) - tn.getLevel()) >= getNumberOfPlayers()) {
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
     * @param cp current player
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
            minTn=getDecisionTree().getNodesOfLevel(1).get(0);
        }
        if(minTn!=null && minTn instanceof BoardNode){
            return ((BoardNode)minTn).getStartPoint();
        }
        //shouldn't get here, but just in case and to make the compiler happy
        return new Point(-1,-1);
    }

    @Override
    public void ballPlaced(int player, Point r) {
        //TODO: Think of removing this method and the IBoardListener interface from this class
        List<TreeNode> fnodes = getDecisionTree().getNodesOfLevel( 1);
        for (TreeNode tn : fnodes) {
            if (tn instanceof BoardNode && ((BoardNode) tn).getStartPoint().x == r.x && ((BoardNode) tn).getStartPoint().y == r.y) {
                break;
            }
        }
    }
}

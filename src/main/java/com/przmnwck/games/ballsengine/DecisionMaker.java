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
import org.apache.log4j.LogManager;

/**
 *
 * @author Przemysław
 */
public class DecisionMaker {
    
    Assesor asses = null;
    Random r = null;
    private final int DECISION_SIZE_LIMIT=30;
    private final int MAX_DEFAULT_TREE_DEPTH=8;
    protected int treeDepth = MAX_DEFAULT_TREE_DEPTH;
    org.apache.log4j.Logger lg = null; 
    
    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }
    
    //private Tree decisionTree=null; 

    public Tree gcreateDecisionTree() {
        return new DecisionTreesBuilder(getNumberOfPlayers(), asses.getBoard().getSize()).buildTree(asses.getBoard());
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
        lg=LogManager.getLogger(DecisionMaker.class.getName());
    }
    
    public Point decideMove(int player, boolean centralStrategyOn){
        Point ret= null; 
        if(centralStrategyOn){
            Point[] cp = asses.getBoard().getCentralPoints();
            ret = getRandomField(cp);
            String msg = "Random field ";
            if (ret!=null){
                msg+="("+ret.x+","+ret.y+")";
            }
            lg.info(msg);
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
                s=Math.abs(randomNumber.nextInt()%fieldsToLook.length);
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
            lg.error("Board not cloned.", ex);
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
            lg.info("Still random fields, as free fields condition has not been met.");
            lg.info("Free fields setting: "+ decisiontrees.DecisionTreesBuilderSettings.getBoardFreeFieldsSetting(asses.getBoard().getSize(), getNumberOfPlayers()));
            lg.info("Board free fields: "+ asses.getBoard().freeFields());
            minTn = new BoardNode(getRandomField(asses.getBoard().getPointFields()));
        } else {
            //FIND BEST NODE
            lg.info("Finding best decision tree option.");
            Tree tree = gcreateDecisionTree();
             List<TreeNode> firstLevelNodes = tree.getNodesOfLevel(1);
             lg.info("Number of Level 1 nodes: "+ firstLevelNodes.size());
             lg.info("Search option: "+ option.toString());           
            for (TreeNode tn : firstLevelNodes) {
                  if (option.equals(DecisionTreeSearchOption.SHORTEST_BRANCH)) {
                    k = tree.getMinBranchLevel(tn);
                    if (((k - 1) % getNumberOfPlayers()) == 0 && (k < m || m == -1)) {
                        minTn = tn;
                        m = k;
                    }
                } else if (option.equals(DecisionTreeSearchOption.LONGEST_BRANCH)) {
                    //doesn't really matter who wins, just keep the game going, and have a_row chance to block any incomming winning possibility
                    k = tree.getMaxBranchLevel(tn);
                    if ((k > m || m == -1) && (tree.getMinBranchLevel(tn) - tn.getLevel()) >= getNumberOfPlayers()) {
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
        lg.info("Trying to find the best way to win or draw.");
        TreeNode minTn=getDecisionBranchNode(cp, DecisionTreeSearchOption.SHORTEST_BRANCH);      
        if(minTn==null){
            //try to find th longest solution then
            minTn=getDecisionBranchNode(cp, DecisionTreeSearchOption.LONGEST_BRANCH);          
        }
        if(minTn==null) {
            minTn=gcreateDecisionTree().getNodesOfLevel(1).get(0);
        }
        if(minTn!=null && minTn instanceof BoardNode){
            return ((BoardNode)minTn).getStartPoint();
        }
        //shouldn't get here, but just in case and to make the compiler happy
        return new Point(-1,-1);
    }

}

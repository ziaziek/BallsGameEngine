/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine.trees;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Przemo
 */

public class Tree implements Serializable{
    private TreeNode rootNode=null;
    
    public Tree(){
        this(new TreeNode(0));
    }
    
    public Tree(TreeNode root){
        rootNode=root;
    }
    
    /**
     * Returns the highest level of all within the tree
     * @return 
     */
    public int getMaxLevel(){
        return getMaxLeafLevel(rootNode, -1);
    }
    
    /**
     * Return the minimum level in the tree
     * @return 
     */
    public int getMinLevel(){
        return getMinLeafLevel(rootNode, -1);
    }
    
    protected int getMaxLeafLevel(TreeNode tn, int initialValue){
        int k=-1; int mv = -1;
        if(!tn.getChildren().isEmpty()){
            for(TreeNode tnch: tn.getChildren()){
               k= getMaxLeafLevel(tnch, k); 
               if(k>mv ){
                   mv= k;
               }
            }
            return mv;
        } else {
            return tn.getLevel();
        }
    }
    
    protected int getMinLeafLevel(TreeNode tn, int initialValue){
        int k=-1; int mv = -1;
        if(!tn.getChildren().isEmpty()){
            for(TreeNode tnch: tn.getChildren()){
               k= getMinLeafLevel(tnch, k); 
               if(k<mv || mv==-1){
                   mv= k;
               }
            }
            return mv;
        } else {
            return tn.getLevel();
        }
    }
    
    /**
     * Returns the longest branch from the tree node down
     * @param n
     * @return 
     */
    public int getMaxBranchLevel(final TreeNode n){
        Tree tmpTree = new Tree(n);
        return tmpTree.getMaxLevel();
    }
   
    public int getMinBranchLevel(final TreeNode n){
        Tree tmpTree = new Tree(n);
        return tmpTree.getMinLevel();
    }
    public List<TreeNode> getNodesOfLevel(final int level){
        List<TreeNode> ret = new ArrayList<TreeNode>();
        fillNodesOfLevel(rootNode, ret, level);
        return ret;
    }
    
    public void fillNodesOfLevel(final TreeNode n, List<TreeNode> addTo, int level) {
        if (n.getLevel() == level) {
            addTo.add(n);
        } else {
            if (!n.getChildren().isEmpty()) {
                for (TreeNode tn : n.getChildren()) {
                    fillNodesOfLevel(tn, addTo, level);
                }
            }
        }
    }
    
    public List<TreeNode> findAllLeaves(){
        List<TreeNode> ret = new ArrayList<TreeNode>();
        findLeavesOfNode(rootNode, ret);
        return ret;
    }
    
    protected void findLeavesOfNode(TreeNode tn, List<TreeNode> addTo){
        if(tn.getChildren().isEmpty()){
            addTo.add(tn);
        } else {
            for(TreeNode t: tn.getChildren()){
                findLeavesOfNode(t, addTo);
            }
        }
    }
}

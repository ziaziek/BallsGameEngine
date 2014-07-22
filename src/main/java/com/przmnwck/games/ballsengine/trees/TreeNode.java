/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine.trees;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Przemo
 */
public class TreeNode {
    private int level=0;
    private TreeNode parent=null;
    
    public int getLevel() {
        return level;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode getPrevSibling() {
        return prevSibling;
    }
    private TreeNode prevSibling=null;
    private List<TreeNode> children=null;

    public List<TreeNode> getChildren() {
        return children;
    }
    
    
    public TreeNode(){
        children = new ArrayList<TreeNode>();
    }
    
    public TreeNode(int Level){
        this();
        level=Level;
        
    }
    
    public void addChild(TreeNode n){
        if(!children.isEmpty()){
            n.prevSibling=children.get(children.size()-1);
        }     
        n.level=this.level+1;
        n.parent=this;
        children.add(n);
    }
    
    public void addChild(){
        addChild(new TreeNode());
    }
}

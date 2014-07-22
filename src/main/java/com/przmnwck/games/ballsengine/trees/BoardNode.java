/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine.trees;

import java.awt.Point;

/**
 *
 * @author Przemo
 */
public class BoardNode extends TreeNode {
    
    private  Point startPoint;

    public Point getStartPoint() {
        return startPoint;
    }
    
    public BoardNode(Point p){
        startPoint=p;
    }
    
    public BoardNode(int Level, Point point){
        super(Level);
        startPoint=point;
    }
}

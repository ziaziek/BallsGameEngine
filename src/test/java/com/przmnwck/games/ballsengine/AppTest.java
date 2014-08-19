package com.przmnwck.games.ballsengine;

import com.przmnwck.games.ballsengine.trees.Tree;
import com.przmnwck.games.ballsengine.trees.TreeNode;
import decisiontrees.DecisionTreesBuilder;
import java.awt.Point;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class);
    }
    
//    public void testBoard(){
//        try {
//            Board b = new Board(10);
//            assertEquals(10, b.getSize());
//            assertEquals(10, b.getFields().length);
//            assertTrue(b.placeBall(1, 0, 0));
//            assertFalse(b.placeBall(0, 1, 1));
//            assertFalse(b.placeBall(1, 100, 0));
//            assertFalse(b.placeBall(1, 2, 101));
//            assertFalse(b.placeBall(1, 0, 0));
//            assertFalse(b.isMovePossible(0, 0));
//            assertTrue(b.isMovePossible(1, 4));
//            
//        } catch (BoardException ex) {
//            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
//    public void testBoardNextFields() throws BoardException{
//        Board b = new Board(3);
//        //right
//        assertEquals(0, b.getNextField(0, 0, SearchDirections.RIGHT)[0]);
//            assertEquals(1, b.getNextField(0, 0, SearchDirections.RIGHT)[1]);
//            assertEquals(1, b.getNextField(0, 2, SearchDirections.RIGHT)[0]);
//            assertEquals(0, b.getNextField(2, 2, SearchDirections.RIGHT)[0]);
//            assertEquals(0, b.getNextField(2, 2, SearchDirections.RIGHT)[1]);
//            //left
//          assertEquals(2, b.getNextField(0, 0, SearchDirections.LEFT)[0]);
//          assertEquals(2, b.getNextField(0, 0, SearchDirections.LEFT)[1]);
//          assertEquals(1, b.getNextField(0, 2, SearchDirections.LEFT)[1]);
//          assertEquals(2, b.getNextField(1, 0, SearchDirections.LEFT)[1]);
//          assertEquals(0, b.getNextField(1, 0, SearchDirections.LEFT)[0]);
//    }
//    
//    
//    public void testAssesorAsses(){
//        try {
//            int np=2;
//            Board b = new Board(3);
//            Assesor a = new Assesor(b);
//            assertEquals(0, a.asses(b, np));
//            assertTrue(b.placeBall(1, 0, 0));
//            assertEquals(0, a.asses(b, np));
//            assertTrue(b.placeBall(1, 0, 1));
//            assertTrue(b.placeBall(1, 0, 2));
//            assertEquals(1, a.asses(b, np));
//        } catch (BoardException ex) {
//            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
//    
//    public void testAssesorRows(){
//        try {
//            Board b = new Board(3);
//            Assesor a= new Assesor(b);
//            b.placeBall(1, 0, 0);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(1, 2, 0);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 1,0);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(1, 0, 1);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 2, 0);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(1, 0, 2);
//            assertTrue(a.isPlayerWinning(1));
//            
//        } catch (BoardException ex) {
//            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
//        }       
//    }
//    
//    public void testAssesorColumns(){
//        try {
//            Board b = new Board(3);
//            Assesor a= new Assesor(b);
//            b.placeBall(1, 0, 0);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(1, 0, 1);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 1,0);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(2, 0, 1);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 2, 0);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(1, 0, 2);
//            assertTrue(a.isPlayerWinning(1));
//            
//        } catch (BoardException ex) {
//            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }
//    
//    public void testAssesorRightDiagonals(){
//        try {
//            Board b = new Board(3);
//            Assesor a= new Assesor(b);
//            b.placeBall(1, 0, 0);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(1, 1, 1);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 1,0);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(1, 0, 1);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 2, 0);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(1, 2, 2);
//            assertTrue(a.isPlayerWinning(1));
//            
//        } catch (BoardException ex) {
//            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }
//    
//    public void testAssesorLeftDisgonals(){
//        try {
//            Board b = new Board(3);
//            Assesor a= new Assesor(b);
//            b.placeBall(1, 0, 0);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(1, 1, 0);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 0, 2);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(1, 0, 1);
//            assertFalse(a.isPlayerWinning(1));
//            b.placeBall(2, 2, 0);
//            assertFalse(a.isPlayerWinning(2));
//            b.placeBall(2, 1, 1);
//            assertTrue(a.isPlayerWinning(2));
//            
//        } catch (BoardException ex) {
//            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }
//  
    
    
    public void testBoardCentralArea() throws BoardException{
        Board b = new Board(3);
        Point[] ps = b.getCentralPoints();
        assertNotNull(ps);
        assertEquals(1, ps.length);
        assertEquals(1, ps[0].x);
        assertEquals(1, ps[0].y);
        for(int i=4; i<=10; i++){
            Board bx = new Board(i);
            Point[] pss=bx.getCentralPoints();
            if(bx.getSize()%2==0){
                assertEquals(4, pss.length);               
            } else {
                assertEquals(9, pss.length);               
            }
            assertEquals((int)(bx.getSize()/2) - 1, pss[0].x);
            assertEquals((int)(bx.getSize()/2) - 1, pss[0].y);
        }
    }
    
    
    public void testDecisionMaker() throws BoardException{
        int np=2;
        Board b = new Board(3);
        Assesor as = new Assesor(b);
        DecisionTreesBuilder builder = new DecisionTreesBuilder(np, b.getSize());
        DecisionMaker dc = new DecisionMaker(as, np, builder.buildTree());
        assertNotNull(b);
        assertNotNull(as);
        assertNotNull(dc);
        assertNotNull(as.getBoard());
        assertNotNull(dc.getAsses());
        assertEquals(np, dc.getNumberOfPlayers());
        //Fun
        assertTrue(b.placeBall(1, 0, 0));
        assertTrue(b.placeBall(2, 1, 1));
        assertEquals(0,as.asses(b, np)); //No player is winning
        //now it's player 1's turn
        int[] R = dc.decideMove(1);
        assertEquals(2, R.length);
        assertFalse(-1==R[0]);
        assertFalse(-1==R[1]);
        System.out.println(R[0]+","+R[1]);
    }
    
    public void testTree(){
        Tree T = new Tree();
        assertEquals(0, T.getMaxLevel());
        assertEquals(1, T.getNodesOfLevel(0).size());
        TreeNode root = T.getNodesOfLevel(0).get(0);
        root.addChild();
        assertEquals(1, T.getMaxLevel());
        root.addChild();
        assertEquals(1, T.getMaxLevel());
        assertEquals(2, T.getNodesOfLevel(1).size());
        List<TreeNode> ns = T.getNodesOfLevel(1);
        TreeNode tn = ns.get(0);
        int i;
        for(i=0; i<5; i++){
           tn.addChild();
           assertEquals(1, tn.getChildren().size());
           tn=tn.getChildren().get(0);
           assertEquals(i+2, T.getMaxLevel());
        }
        assertNull(root.getParent());
        assertEquals(i+1, T.getMaxBranchLevel(root));
        assertEquals(ns.get(1).getLevel(), T.getMaxBranchLevel(ns.get(1)));
        assertEquals(ns.get(0).getLevel(), T.getMinLevel());
        assertEquals(6, new Tree(ns.get(0).getChildren().get(0)).getMaxLevel());
    }
    
//    public void testDTBuilder() throws FileNotFoundException, IOException, ClassNotFoundException{
//        int np =2; int bs = 3;
//        String dir = "D:/Trees";
//        DecisionTreesBuilder builder = new DecisionTreesBuilder(np, bs);
//        builder.loadTree(bs, np, dir);
//        assertNotNull(builder.getCurrentTree());
//        assertFalse(builder.getCurrentTree().getNodesOfLevel(1).isEmpty());
//        assertTrue(builder.getCurrentTree().getMaxLevel()>0);
//    }
}

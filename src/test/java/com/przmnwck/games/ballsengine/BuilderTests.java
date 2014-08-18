/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Przemo
 */
public class BuilderTests extends TestCase {
    
    public BuilderTests(String testName) {
        super(testName);
    }
    
        public static Test suite()
    {
        return new TestSuite( BuilderTests.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
   
    
    public void testOccupationMatrix() {
        List<int[]> r;
        int bs = 3;
        int np = 1;
        for (int b = bs; b < 6; b++) {
            for (int n = np; n < 5; n++) {
                r = decisiontrees.DecisionTreesBuilderHelper.createOccupationMatrixList(b, n);
                assertFalse(r.isEmpty());
                assertEquals(org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient(b * b, n), r.size());
            }
        }
    }
    
    public void testConverToBoard() throws BoardException{
        int bs=3;
        int np=2;
        int mtg=2;
        List<Board> bList =decisiontrees.DecisionTreesBuilderHelper.createOccupiedBoardsList(bs, mtg, np);
        assertTrue(!bList.isEmpty());
        assertEquals(org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient(bs * bs, mtg), bList.size());
    }
}

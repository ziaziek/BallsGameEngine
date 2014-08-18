/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Przemo
 */
public class DecisionTreesBuilderSettings {
    
    private static Map<Integer, Integer> boardSizeFreeFields = null;
    public static final String DECISION_TREES_EXTENSION=".dtf";
    public static final String INDEX_FILE = "index.dtf";
    public static final String TREES_DIRECTORY = "D:/Trees";
    
    public static int getBoardFreeFieldsSetting(int boardSize){
        return boardSize*(boardSize-1);
    }
}

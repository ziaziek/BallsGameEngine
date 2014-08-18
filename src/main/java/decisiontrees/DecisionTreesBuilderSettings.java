/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees;

/**
 *
 * @author Przemo
 */
public class DecisionTreesBuilderSettings {
   
    public static final String DECISION_TREES_EXTENSION=".dtf";
    public static final String INDEX_FILE = "index.dtf";
    public static final String TREES_DIRECTORY = "D:/Trees";
    
    public static int getBoardFreeFieldsSetting(int boardSize){
        return boardSize*(boardSize-1);
    }
    
    public static int getMaxPlayers(int boardSize){
        switch(boardSize){
            case 3: return 2;
            case 4: case 5: return 3;
            default: return 8; 
        }
    }
}

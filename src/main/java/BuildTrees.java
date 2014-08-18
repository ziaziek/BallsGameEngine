
import com.przmnwck.games.ballsengine.Board;
import com.przmnwck.games.ballsengine.BoardException;
import decisiontrees.DecisionTreesBuilder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Przemo
 */
public class BuildTrees {
    static final int MIN_NUMBER_OF_PLAYERS=2;
    static final int MAX_BOARD_SIZE = 10;
    static final int MIN_BOARD_SIZE=3;
    static Map<Board, String> indexMap = null;
    public static void main(String[] args) {
        //build trees for different combinationsof number of players and field sies
        indexMap = new HashMap<Board, String>();
        DecisionTreesBuilder builder;
        for (int i = MIN_BOARD_SIZE; i < MAX_BOARD_SIZE; i++) {
            for (int p = MIN_NUMBER_OF_PLAYERS; p <= decisiontrees.DecisionTreesBuilderSettings.getMaxPlayers(i); p++) {
                try {
                    for (Board currentBoard : decisiontrees.DecisionTreesBuilderHelper.createOccupiedBoardsList(i, decisiontrees.DecisionTreesBuilderSettings.getBoardFreeFieldsSetting(i), p)) {
                        builder = new DecisionTreesBuilder(p, i);
                        System.out.println("DecisionTreeBuilder for np=" + p + ", bs=" + i + " ready.");
                        builder.buildTree(currentBoard);
                        System.out.println("Tree built.");
                        try {
                            indexMap.put(currentBoard, builder.saveTree(builder.getCurrentTree(), decisiontrees.DecisionTreesBuilderSettings.TREES_DIRECTORY, i, p));
                            System.out.println("Tree created and saved for np=" + p + ", bs=" + i);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                } catch (BoardException ex) {
                    Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (indexMap != null && !indexMap.isEmpty()) {
            saveMap(indexMap);
        }
    }
    
    private static void saveMap(Map toSave) {
        try {
            ObjectOutputStream ostr = new ObjectOutputStream(new FileOutputStream(decisiontrees.DecisionTreesBuilderSettings.INDEX_FILE));
            ostr.writeObject(toSave);
            ostr.close();
        } catch (IOException ex) {
            Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

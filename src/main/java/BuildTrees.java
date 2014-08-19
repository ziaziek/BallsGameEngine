
import com.przmnwck.games.ballsengine.Board;
import com.przmnwck.games.ballsengine.BoardException;
import com.przmnwck.games.ballsengine.impl.BoardRenderer;
import com.przmnwck.games.ballsengine.interfaces.IRenderer;
import decisiontrees.DecisionTreesBuilder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Przemo
 */
public class BuildTrees {
    public static Logger log = org.apache.log4j.Logger.getLogger(BuildTrees.class.getName());
    static final int MIN_NUMBER_OF_PLAYERS=2;
    static final int MAX_BOARD_SIZE = 5;
    static final int MIN_BOARD_SIZE=3;
    static Map<Board, String> indexMap = null;
    public static void main(String[] args) {
        org.apache.log4j.PropertyConfigurator.configure(BuildTrees.class.getResource("log4j.properties"));
        //build trees for different combinationsof number of players and field sies
        indexMap = new HashMap<>();
        DecisionTreesBuilder builder;
        IRenderer rend = new BoardRenderer();
        for (int i = MIN_BOARD_SIZE; i <= MAX_BOARD_SIZE; i++) {
            for (int p = MIN_NUMBER_OF_PLAYERS; p <= decisiontrees.DecisionTreesBuilderSettings.getMaxPlayers(i); p++) {
                try {
                    for (Board currentBoard : decisiontrees.DecisionTreesBuilderHelper.createOccupiedBoardsList(i, i*i- decisiontrees.DecisionTreesBuilderSettings.getBoardFreeFieldsSetting(i,p), p)) {
                        builder = new DecisionTreesBuilder(p, i);
                        rend.renderResult(currentBoard);
                        log.info("DecisionTreeBuilder for np=" + p + ", bs=" + i + " ready.");
                        builder.buildTree(currentBoard);
                        log.info("Tree built.");
                        try {
                            indexMap.put(currentBoard, builder.saveTree(builder.getCurrentTree(), decisiontrees.DecisionTreesBuilderSettings.TREES_DIRECTORY, i, p));
                            System.out.println("Tree created and saved for np=" + p + ", bs=" + i);
                        } catch (FileNotFoundException ex) {
                            log.error( ex.getMessage());
                        } catch (IOException ex) {
                            log.error(ex.getMessage());
                        }
                    }

                } catch (BoardException ex) {
                    Logger.getLogger(BuildTrees.class.getName()).log(Level.ERROR, null, ex);
                }
            }
        }
        if (indexMap != null && !indexMap.isEmpty()) {
            saveMap(indexMap);
            
        }
    }
    
    private static void saveMap(Map toSave) {
        try {
            try (ObjectOutputStream ostr = new ObjectOutputStream(new FileOutputStream(decisiontrees.DecisionTreesBuilderSettings.TREES_DIRECTORY+ "/"+decisiontrees.DecisionTreesBuilderSettings.INDEX_FILE))) {
                ostr.writeObject(toSave);
                ostr.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(BuildTrees.class.getName()).log(Level.ERROR, null, ex);
        }
    }
}

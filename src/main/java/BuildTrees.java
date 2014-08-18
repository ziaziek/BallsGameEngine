
import com.przmnwck.games.ballsengine.Engine;
import decisiontrees.DecisionTreesBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
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
    public static void main(String[] args){
        //build trees for different combinationsof number of players and field sies
        int[] boardSizes = new int[]{3, 4, 5, 6, 7, 8};
        int[] nop = new int[] {2, 3, 3, 4, 4, 4}; //maximum number of players
        DecisionTreesBuilder builder;
        for (int i = 0; i < boardSizes.length; i++) {
            for (int p = MIN_NUMBER_OF_PLAYERS; p <= nop[i]; p++) {
                builder = new DecisionTreesBuilder(p, boardSizes[i]);
                System.out.println("DecisionTreeBuilder for np="+p+", bs="+boardSizes[i]+" ready.");
                builder.buildTree();
                System.out.println("Tree built.");
                try {
                    builder.saveTree(builder.getCurrentTree(), decisiontrees.DecisionTreesBuilderSettings.TREES_DIRECTORY, boardSizes[i], p);
                    System.out.println("Tree created and saved for np="+p+", bs="+boardSizes[i]);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}

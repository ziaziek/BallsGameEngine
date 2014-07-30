
import decisiontrees.DecisionTreesBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    
    public static void main(String[] args){
        DecisionTreesBuilder builder = new DecisionTreesBuilder(2, 3);
        builder.buildTree();
        try {
            builder.saveTree(builder.getCurrentTree(), "D:/Trees", 3, 2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuildTrees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

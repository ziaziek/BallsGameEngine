/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees;

import com.przmnwck.games.ballsengine.Board;
import com.przmnwck.games.ballsengine.BoardException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Przemo
 */
public class DecisionTreesBuilderHelper {
    
        public static List<int[]> createOccupationMatrixList(int boardSize, int numberOfMovesToGo){
        List<int[]> ret = new ArrayList<int[]>();
        int w = boardSize * boardSize;
        fill(0, 0, w, numberOfMovesToGo, new int[numberOfMovesToGo], ret);
        return ret;
    }
    
    protected static void fill(int startValue, int shiftValue, int maxValue, int ntg, int[] v, List<int[]>list){
        if(shiftValue<ntg){            
            for(int i=startValue; i<maxValue; i++){
                v[shiftValue]=i;
                fill(i+1,shiftValue+1 , maxValue, ntg, v.clone(), list);
            }
        } else {
            list.add(v.clone());
        }
    }
    
    protected static Board convertIndexMatrixToBoard(int[]v, int numberOfPlayers) throws BoardException{
        Board b = new Board(v.length);
        int player=0;
        for(int x: v){
            b.placeBall(player, (int) Math.floor(x/b.getSize()), x%b.getSize());
            player++;
            if(player>numberOfPlayers){
                player=1;
            }
        }
        return b;       
    }
    
    public static List<Board> createOccupiedBoardsList(int boardSize, int movesToGo, int numberOfPlayers) throws BoardException{
        List<Board> ret = new ArrayList<Board>();
        for(int[] v: createOccupationMatrixList(boardSize, movesToGo)){
            ret.add(convertIndexMatrixToBoard(v, numberOfPlayers));
        }
        return ret;
    }
}

package SubSetSum;

import Board.*;

import java.util.List;

public class SSSBoardInitializer implements BoardInitializer {
    private List<Integer> dimensions;
    private List<Integer> totalSet;

    //Constructor 
    public SSSBoardInitializer(List<Integer> dimensions, List<Integer> totalSet){
        this.dimensions = dimensions;
        this.totalSet = totalSet;
    }

    public Board getInitializedBoard(){
        //Get board reference
        Board board = new Board(dimensions);

        //Iterate through all the cells and initialize LMHContent
        List<Cell> cells = board.getCells();
        
        //Iterate through all the cells and initialize LMHContent
        for (Cell cell:cells){
            //Initialize LMHContent
            SubSetSumContent content = new SubSetSumContent();

            //get cell coords
            List<Integer> coords = cell.getCoords();
            //If row or col == 1, set value of content to 0
            if ((coords.get(0) == 1 || coords.get(1) == 1)){
                content.setValue(0);
            }

            //If col == 0, and row >= 1, set value of content to totalSet.get(row-1)
            if (coords.get(0) == 0 && coords.get(1) >= 2){
                content.setValue(totalSet.get(coords.get(1)-2));
            }

            //If row == 0, and col >= 1, set value of content to col
            if (coords.get(1) == 0 && coords.get(0) >= 1){
                content.setValue(coords.get(0)-1);
            }

            //Set cell content
            cell.setContent(content);
        }      
        return board;
    }
}

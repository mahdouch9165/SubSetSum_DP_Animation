// Purpose: Interface for displaying the board.
package SubSetSum;

import java.util.Arrays;

import Board.*;

public class SSSTerminalBoardDisplay implements BoardDisplay{
    private Board board;
    private int order;

    public SSSTerminalBoardDisplay(Board board, int order){
        this.board = board;
        this.order = order;
    }

    public void displayBoard(){
        // Get board dimensions (LMH is 2D, and will be implemented as such, the dimensionality options are for flexibility)
        int x_dim = board.getDimensions().get(0);
        int y_dim = board.getDimensions().get(1);

        //Row separator Builder
        String rowSeparator = "+";
        String rowSeparatorUnit = "-";
        int order = (int) Math.log10(this.order) + 1;
        for (int i = 0; i < x_dim; i++) {
            for (int j = 0; j < order; j++) {
                rowSeparator += rowSeparatorUnit;
            }
            rowSeparator += "+";
        }
        System.out.println(rowSeparator);

        // Print board
        for (int i = 0; i < y_dim; i++) {
            String row = "";
            for (int j = 0; j < x_dim; j++) {
                Cell cell = board.getCellByCoords(Arrays.asList(j, i));
                SubSetSumContent cellContent = (SubSetSumContent) cell.getContent();
                // if (cellContent.hasValue()){
                //     row += "| " + cellContent.getValue() + " ";
                // }
                // else {
                //     row += "|   ";
                // }
                if (cellContent.hasValue()){
                    //if is being viewed color green
                    if (cellContent.isViewed()){
                        if (cellContent.isTarget()){
                            row += "|\u001B[31m"+ cellContent.getValue() + "\u001B[0m";
                        }
                        else{
                            row += "|\u001B[32m" + cellContent.getValue() + "\u001B[0m";
                        }
                    }
                    else{
                        row += "|" + cellContent.getValue() + "";
                    }
                }
                else {
                    // if it is a target add a red colored *
                    if (cellContent.isTarget()){
                        row += "|\u001B[31m*\u001B[0m";
                    }
                    else{
                        row += "| ";
                    }
                }
                int cellCountOrder;
                if (cellContent.getValue() == 0) {
                    cellCountOrder = 1;
                }else{
                    cellCountOrder = (int) Math.log10(cellContent.getValue())+1;
                }
                int cellCountPadding = order - cellCountOrder;
                for (int k = 0; k < cellCountPadding; k++) {
                    //row += " ";
                    row += " ";
                }
            }
            row += "|";
            System.out.println(row);
            System.out.println(rowSeparator);
        }

    }
}

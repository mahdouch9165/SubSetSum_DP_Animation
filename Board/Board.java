package Board;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Config.GlobalConfig;

import java.util.ArrayList;
import java.util.Arrays;

//Represents a grouping of cells
public class Board {
    //Board is a list of cells
    //Holding a list of cells, does not prevent dimensionality, it actually allows for dynamic dimensionality
    //Especially if dimensions are also stored in a list (dynamic)
    private List<Cell> cells;
    private List<Integer> dimensions;
    private Map<Cell, Set<Cell>> graph;

    //When a board is made it must be passed dimensions
    public Board(List<Integer> dimensions) {
        //Validate dimensions
        validateDimensions(dimensions);

        //Set dimensions
        this.dimensions = dimensions;

        //Build cells
        this.cells = cellsBuilder(dimensions);
    }

    //Get dimensions, cant set them
    public List<Integer> getDimensions() {
        return dimensions;
    }

    //Get cells, cant set them, dont have to worry about them being changed
    //Since only cell content can be set from Cell class
    public List<Cell> getCells() {
        return cells;
    }

    public Cell getCellByNum(int num) {
        //Make sure num is valid
        if (num < 1 || num > cells.size()) {
            throw new IllegalArgumentException("Cell num must be at least 1. Faulty cell num: " + num);
        }
        return cells.get(num - 1);
    }

    public Cell getCellByCoords(List<Integer> coords) {
        //Make sure coords are valid
        if (coords.size() != dimensions.size()) {
            throw new IllegalArgumentException("Coords must have same dimension count as board. Faulty coords: " + coords);
        }
        for (int i = 0; i < coords.size(); i++) {
            if (coords.get(i) < 0 || coords.get(i) >= dimensions.get(i)) {
                throw new IllegalArgumentException("Coords must be within board dimensions. Faulty coords: " + coords);
            }
        }
        //Get cell num encoding, dimension count encoding should be an array of length dimensionCount, of all 1s
        int [] dimension_count_encoding = getDimensionCountEncoding();
        //Get num
        int num = 0;
        for (int i = 0; i < dimensions.size(); i++) {
            num += coords.get(i) * dimension_count_encoding[i];
        }
        num++;
        return getCellByNum(num);
    }

    public Set<Cell> getNeighbors(Cell cell) {
        //Make sure cell is valid
        if (!cells.contains(cell)) {
            throw new IllegalArgumentException("Cell must be on board. Faulty cell: " + cell);
        }
        return graph.get(cell);
    }
    // Builds a list of cells based on dimensions (Proud of this one lol)
    // What this means, is that you can get a multi-dimensional board!
    // Try it for yourself:
    // Go to global config and change MAX_BOARD_DIMENSIONS to any number you want > 1
    // Go to main use this code snippet inside the main function

    // List<Integer> dimensions = new ArrayList<>(Arrays.asList(3,3,3));
    // Board board = new Board(dimensions);
    // System.out.println(board);

    //Cell builder helper function
    private List<Cell> cellsBuilder(List<Integer> dimensions){
        //At this point we know dimensions are valid
        //We can use them to build cells
        int dimensionCount = dimensions.size();
        int totalCellCount = 1;

        //Get total cell count
        for (int i = 0; i < dimensionCount; i++) {
            totalCellCount *= dimensions.get(i);
        }

        //Make a list of cells
        List<Cell> cells = new ArrayList<Cell>(totalCellCount);
        List<List<Integer>> coords = new ArrayList<List<Integer>>(totalCellCount);

        for (int i = 0; i < totalCellCount; i++) {
            Integer[] currCoords = new Integer[dimensionCount];
            Arrays.fill(currCoords, 0);
            coords.add(Arrays.asList(currCoords));
        }

        //Right product is 1, left product is equivalent to total cell count
        int[] rightProduct = {1};
        int[] leftProduct = {totalCellCount};

        //Iterate backwards through dimensions to create coords for each cell
        for (int i = dimensionCount - 1; i >= 0; i--) {
            int currDimensionSize = dimensions.get(i);
            rightProduct[0] *= currDimensionSize;
            leftProduct[0] /= currDimensionSize;
            //Iterate through cells
            int index = 0;
            for (int j = 0; j < rightProduct[0]; j++){
                int coord = j % currDimensionSize;
                for (int k = 0; k < leftProduct[0]; k++) {
                    List<Integer> currCoords = coords.get(index);
                    currCoords.set(i, coord);
                    index++;
                }
            }
        }
        
        //Get cell num encoding, dimension count encoding should be an array of length dimensionCount, of all 1s
        int [] dimension_count_encoding = getDimensionCountEncoding();

        //Iterate through coords to create cells
        for (int i = 0; i < totalCellCount; i++) {
            //Get coords
            List<Integer> currCoords = coords.get(i);
            //Get num
            int num = 0;
            for (int j = 0; j < dimensionCount; j++) {
                num += currCoords.get(j) * dimension_count_encoding[j];
            }
            num++;
            //Make cell
            Cell currCell = new Cell(currCoords, num);
            //Add cell to list
            cells.add(currCell);
        }

        //Return cells
        return cells;
    }

    //Get dimension count encoding
    public int[] getDimensionCountEncoding(){
        int dimensionCount = dimensions.size();
        int [] dimension_count_encoding = new int[dimensionCount];
        dimension_count_encoding[0] = 1;
        for (int i = 1; i<dimensionCount; i++){
            dimension_count_encoding[i] = dimension_count_encoding[i-1] * dimensions.get(i-1);
        }
        return dimension_count_encoding;
    }

    //Validate dimensions
    private void validateDimensions(List<Integer> dimensions) {
        //Make sure dimension count is at least 1
        if (dimensions.size() < 1) {
            throw new IllegalArgumentException("Dimension count must be at least 1. Faulty dimension count: " + dimensions.size());
        }
        if (dimensions.size() > GlobalConfig.MAX_BOARD_DIMENSIONS) {
            throw new IllegalArgumentException("Dimension count must be at most " + GlobalConfig.MAX_BOARD_DIMENSIONS + ". Faulty dimension count: " + dimensions.size());
        }
        //Make sure dimensions are all at least 1
        for (int i = 0; i < dimensions.size(); i++) {
            if (dimensions.get(i) < 1) {
                throw new IllegalArgumentException("Dimensions must be at least 1. Faulty dimension: " + dimensions.get(i) + " at index: " + i);
            }
            if (dimensions.get(i) > GlobalConfig.MAX_DIMENSION_SIZE) {
                throw new IllegalArgumentException("Dimensions must be at most " + GlobalConfig.MAX_DIMENSION_SIZE + ". Faulty dimension: " + dimensions.get(i) + " at index: " + i);
            }
        }
    }

    //Set graph
    public void setGraph(Map<Cell, Set<Cell>> graph) {
        this.graph = graph;
    }

    //TEMPORARY
    @Override
    public String toString() {
        String returnString = "";
        for (Cell cell : cells) {
            returnString += cell.toString() + "\n";
        }
        //Remove last newline
        returnString = returnString.substring(0, returnString.length() - 1);
        return returnString;
    }
}

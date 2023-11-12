package SubSetSum;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import Board.Board;

public class SubSetSum {
    //Board and display
    private Board board;
    private SSSBoardInitializer initializer;
    private SSSTerminalBoardDisplay display;

    //Numerical values
    private Set<Integer> totalSet;
    private List<Integer> totalList;
    private int minSizeBound = 7;
    private int maxSizeBound = 10;
    private int minwBound = 6;
    private int maxwBound = 14;
    private int size;
    private int targetW;
    private int maxSum;
    private double pause = 0.2;
    private String pauseType = "manual";
    private List<Set<Integer>> solutions;

    public SubSetSum(){
        init();
    }

    public void start(){
        boolean restart = true;
        while (restart){
            callAlgorithm();
            clearTerminal();
            displayBoard();
            displayProblemInfo();
            printSolutions();
            restart = restartRequest();
            if (restart){
                reset();
            }
        }
    }

    private void init(){
        this.totalSet = new HashSet<>();
        //generate a random size between min and max
        this.size = (int) (Math.random() * (maxSizeBound - minSizeBound)) + minSizeBound;
        maxSum = 0;
        for (int i = 0; i < this.size; i++) {
            //generate a random number between minwBound and maxwBound
            int num = (int) (Math.random() * (maxwBound - minwBound)) + minwBound;
            if (!totalSet.contains(num)) {
                this.totalSet.add(num);
                maxSum += num;
            }
        }
        //generate a random target W between 1 and maxSum
        this.targetW = (int) (Math.random() * maxSum) + 1;

        //Generate a 2-d array of size set x W, us as array
        List<Integer> dimensions = new ArrayList<>(Arrays.asList(this.targetW+2, this.totalSet.size()+2));

        //Sort the set
        List<Integer> sortedList = new ArrayList<>(this.totalSet);
        sortedList.sort((a, b) -> a - b);
        this.totalList = sortedList;

        this.initializer = new SSSBoardInitializer(dimensions, sortedList);
        this.board = initializer.getInitializedBoard();
        this.display = new SSSTerminalBoardDisplay(this.board, this.maxSum);

        //Add 0 to the beginning of the list
        sortedList.add(0, 0);

        //Initialize solutions
        this.solutions = new ArrayList<>();
    }

    public void displayBoard(){
        this.display.displayBoard();
    }

    public void callAlgorithm(){
        //SubSetSum Algorithm
        for (int j = 1; j<this.totalSet.size()+1; j++){
            for (int w = 1; w <this.targetW+1; w++){
                //set target on w, j
                int targetx = w;
                int targety = j;
                SubSetSumContent targetContent = (SubSetSumContent) this.board.getCellByCoords(Arrays.asList(getOffsetCoord(targetx), getOffsetCoord(targety))).getContent();
                targetContent.setTarget();
                int max;
                int wj = this.totalList.get(j);

                //Get opt1 info
                int opt1x = w-wj;
                int opt1y = j-1;
                int opt1;
                SubSetSumContent opt1Content;
                if (opt1x >= 0) {
                    // Set as being viewed
                    opt1Content = (SubSetSumContent) this.board.getCellByCoords(Arrays.asList(getOffsetCoord(opt1x), getOffsetCoord(opt1y))).getContent();
                    opt1Content.setViewed();
                    // Get opt1 val
                    opt1 = wj + queryBoard(opt1x, opt1y);
                } else {
                    // If the item is too heavy to be included, set opt1 to 0
                    opt1Content = null;
                    opt1 = 0;
                }

                //Get opt2 info
                int opt2x = w;
                int opt2y = j-1;
                //Set as being viewed
                SubSetSumContent opt2Content = (SubSetSumContent) this.board.getCellByCoords(Arrays.asList(getOffsetCoord(opt2x), getOffsetCoord(opt2y))).getContent();
                opt2Content.setViewed();
                //Get opt2 val
                int opt2 = queryBoard(opt2x, opt2y);

                //Obtain correct max
                if (opt1 > opt2){
                    max = opt1;
                }
                else if (opt1 < opt2){
                    max = opt2;
                }
                else{
                    max = opt1;
                }

                //Display board
                displayBoard();

                //Display comparison info
                displayComparisonInfo(opt1x, opt1y, opt2x, opt2y, wj, opt1, opt2, max, false);

                //Pause
                pause();

                //clear terminal
                clearTerminal();

                //Set target content as max
                targetContent.setValue(max);
                targetContent.setViewed();

                //Check solution
                // if (max == this.targetW){
                //     //If solution, add to solutions
                //     Set<Integer> solution = new HashSet<>();
                //     int x = w;
                //     int y = j;
                //     while (x > 0 && y > 0){
                //         SubSetSumContent content = (SubSetSumContent) this.board.getCellByCoords(Arrays.asList(getOffsetCoord(x), getOffsetCoord(y))).getContent();
                //         if (content.getValue() == 0){
                //             y--;
                //         }
                //         else{
                //             solution.add(this.totalList.get(y));
                //             x -= this.totalList.get(y);
                //             y--;
                //         }
                //     }
                //     this.solutions.add(solution);
                // }

                if (max == this.targetW){
                    // If solution, reconstruct the subset
                    Set<Integer> solution = new HashSet<>();
                    int x = w; // This should be initialized to targetW if you're checking after filling the DP table.
                    int y = j; // This should be initialized to the size of the set.
                    while (x > 0 && y > 0){
                        SubSetSumContent content = (SubSetSumContent) this.board.getCellByCoords(Arrays.asList(getOffsetCoord(x), getOffsetCoord(y))).getContent();
                        int currentItemWeight = this.totalList.get(y); // Ensure y is not out of bounds for totalList.
                        if (currentItemWeight == 0) {
                            y--;
                            continue;
                        }
                        if (x - currentItemWeight >= 0) {
                            SubSetSumContent previousContent = (SubSetSumContent) this.board.getCellByCoords(Arrays.asList(getOffsetCoord(x - currentItemWeight), getOffsetCoord(y - 1))).getContent();
                            if (content.getValue() - previousContent.getValue() == currentItemWeight) {
                                solution.add(currentItemWeight);
                                x -= currentItemWeight;
                            }
                        }
                        y--;
                    }
                    
                    int sumOfSolution = solution.stream().mapToInt(Integer::intValue).sum();
                    if (sumOfSolution == this.targetW && !containsCombination(this.solutions, solution)) {
                        this.solutions.add(solution);
                    }
                }

                //Display board
                displayBoard();

                //Display comparison info
                displayComparisonInfo(opt1x, opt1y, opt2x, opt2y, wj, opt1, opt2, max, true);

                //Pause
                pause();

                //clear terminal
                clearTerminal();
                //Unset all
                targetContent.unsetTarget();
                targetContent.unsetViewed();
                if (opt1Content != null){
                    opt1Content.unsetViewed();
                }
                opt2Content.unsetViewed();
            }
        }
    }

    private void waitForEnter(){
        System.out.println("Press Enter to continue");
        while (true){
            try {
                int input = System.in.read();
                // ASCII code for newline is 10
                if (input == 10){
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pause(){
        if (this.pauseType.equals("auto")){
            pause(this.pause);
        }
        else if (this.pauseType.equals("manual")){
            waitForEnter();
        }
        else{
            System.out.println("Invalid pause type");
        }
    }

    private void pause(double seconds){
        try {
            Thread.sleep((long)(seconds*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void clearTerminal(){
        System.out.print("\033[H\033[2J");
    }

    //get offset X
    private int getOffsetCoord(int k){
        return k+1;
    }

    //Takes into account the offset of the board
    private int queryBoard(int w, int j){
        return ((SubSetSumContent) this.board.getCellByCoords(Arrays.asList(w+1, j+1)).getContent()).getValue();
    }

    public String toString(){
        return "Total Set: " + this.totalList + "\nTarget W: " + this.targetW;
    }

    public void reset(){
        init();
    }

    public void visitBoard(int w, int j){
        SubSetSumContent content = (SubSetSumContent) this.board.getCellByCoords(Arrays.asList(w+1, j+1)).getContent();
        content.setViewed();
        content.setTarget();
        displayBoard();
        content.unsetViewed();
        content.unsetTarget();
    }

    private void displayComparisonInfo(int opt1x, int opt1y, int opt2x, int opt2y, int wj, int opt1, int opt2, int max, boolean printMax){
        displayProblemInfo();
        //Check if opt1 is valid
        if (opt1x >= 0) {
            System.out.println("Opt1 Coords: " + opt1x + ", " + opt1y);
            System.out.println("Opt1: " + wj + " + " + queryBoard(opt1x, opt1y) + " = " + opt1);
        } else {
            //Account for the fact that opt1 is invalid in coord print
            System.out.println("Opt1 currently invalid");
        }
        System.out.println("Opt2 Coords: " + opt2x + ", " + opt2y);
        System.out.println("Opt2: " + queryBoard(opt2x, opt2y) + " = " + opt2);
        System.out.println("Max: " + max);
        if (max == this.targetW){
            if (printMax){
                System.out.println("Solution found: " + this.solutions.get(this.solutions.size()-1));
                //Check if length of solutions is greater than 1
                if (this.solutions.size() > 1){
                    System.out.println("Previous solutions: ");
                    for (int i = 0; i < this.solutions.size()-1; i++){
                        System.out.println(this.solutions.get(i));
                    }
                }
            }
            else{
                System.out.println("Solution found!");
            }
        }
        else{
            System.out.println("No new solution found");
            if (this.solutions.size() > 0){
                System.out.println("Previous solutions: ");
                for (Set<Integer> solution : this.solutions){
                    System.out.println(solution);
                }
            }
        }

    }

    public void setPause(double seconds){
        this.pause = seconds;
    }

    public void setPauseType(String type){
        if (type.equals("auto")){
            this.pauseType = "auto";
        }
        else if (type.equals("manual")){
            this.pauseType = "manual";
        }
        else{
            System.out.println("Invalid pause type");
        }
    }

    public void displayProblemInfo(){
        System.out.println(toString());
    }

    public boolean restartRequest(){
        System.out.println("Would you like to restart? (q-Quit, Enter-Restart)");
        boolean valid = false;
        while (!valid){
            try {
                int input = System.in.read();
                if (input == 113){
                    return false;
                }
                else if (input == 10){
                    return true;
                }
                else{
                    System.out.println("Invalid input");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void printSolutions(){
        System.out.println("Solutions: ");
        if (this.solutions.size() == 0){
            System.out.println("No solutions found");
        }
        else{
            for (Set<Integer> solution : this.solutions){
                System.out.println(solution);
            }
        }
    }

    private boolean containsCombination(List<Set<Integer>> listOfSets, Set<Integer> targetSet) {
        for (Set<Integer> set : listOfSets) {
            if (set.equals(targetSet)) {
                return true;
            }
        }
        return false;
    }
}

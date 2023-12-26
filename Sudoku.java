/*

    Description: This file contains fillRandom method which
    fills given sudoku randomly but each 3*3 square is correct and
    display method that shows sudoku board.

    Author: Zehra İnöz
    Contact: zehraiinozz@gmail.com

    Last Update: 29.11.2023 00.05

 */

import java.awt.*;
import java.util.*;

import static java.lang.Math.exp;

public class Sudoku {
    private final ArrayList<Point> initializedIndices = new ArrayList<>();
    private final ArrayList<Point> lastChanges = new ArrayList<>();
    public int[][] sudoku = {
                      {0,0,6,0,0,0,0,0,0},
                      {0,8,0,0,5,4,2,0,0},
                      {0,4,0,0,9,0,0,7,0},
                      {0,0,7,9,0,0,3,0,0},
                      {0,0,0,0,8,0,4,0,0},
                      {6,0,0,0,0,0,1,0,0},
                      {2,0,3,0,0,0,0,0,1},
                      {0,0,0,5,0,0,0,4,0},
                      {0,0,8,3,0,0,5,0,2}
                      };

    //Fills empty cells randomly but each 3*3 square is correct in itself
    public  void fillRandom(){

        //List of numbers that will be placed
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            numbers.add(i);
        }
        //Travels sudoku
        for(int row = 0; row < 9; row+=3){

            for(int col = 0; col < 9; col+=3){

                //Recreate all possible numbers
                var numbersCopy = (ArrayList<Integer>)numbers.clone();

                //Locates the numbers in different indexes
                Collections.shuffle(numbersCopy);

                constructPossibleDigits(numbersCopy, row, row + 3, col,col + 3);

                //Travels 3*3 squares
                for(int subrow = row; subrow < row + 3; subrow++){

                    for(int subcol = col; subcol < col + 3; subcol++){

                        if(sudoku[subrow][subcol] == 0){
                               //Fills empty cell with random possible number and remove it
                               sudoku[subrow][subcol] = numbersCopy.remove(0);
                        }else {
                            initializedIndices.add(new Point(subrow,subcol));
                        }
                    }
                }
            }
        }

    }

    //Remove numbers that already in 3*3 square
    private void constructPossibleDigits(ArrayList<Integer> list, int rStart, int rEnd, int cStart, int cEnd){

        for(int i = rStart; i < rEnd; i++){

            for(int j = cStart; j < cEnd; j++){

                if(list.contains(sudoku[i][j])){

                    list.remove((Integer) sudoku[i][j]);
                }
            }
        }
    }

    //Shows sudoku board
    public void display() {

        for(int row = 0; row < 9; row++){

            if(row % 3 == 0){
                System.out.println("-------------------------");

            }
            for(int col = 0; col < 9; col++){

                if(col % 3 == 0){
                    System.out.print("| ");
                }
                System.out.print(sudoku[row][col] + " ");
            }
            System.out.println("|");

        }
        System.out.println("-------------------------");

    }

    public int cost(){
        int cost = 0;

        for (int i = 0; i < sudoku.length; i++) {
            cost+= sudoku.length - countUniqueElements(sudoku[i]);
        }

        for (int col = 0; col < sudoku[0].length; col++) {
            int[] columnValues = new int[sudoku.length];
            for (int row = 0; row < sudoku.length; row++) {
                columnValues[row] = sudoku[row][col];
            }
            cost += columnValues.length - countUniqueElements(columnValues);
        }

        return cost;

    }

    private  int countUniqueElements(int[] array) {
        Set<Integer> uniqueElements = new HashSet<>();
        for (int element : array) {
            uniqueElements.add(element);
        }
        return uniqueElements.size();
    }

    public void generateSuccessor(){
        lastChanges.clear();
        Random random = new Random();
        int randSquare = random.nextInt(9);
        ArrayList<Point> rsPoints = new ArrayList<>();

        for (int i = randSquare / 3 * 3; i < randSquare / 3 * 3 + 3; i++) {
            for (int j = randSquare % 3 * 3; j < randSquare % 3 * 3 + 3; j++) {
                //System.out.print(sudoku[i][j] + " ");
                rsPoints.add(new Point(i,j));
            }
            //System.out.println();
        }

        Point firstPt = null;
        Point secondPt = null;

        while(firstPt == null || secondPt == null){
            int randPoint = random.nextInt(9);
            if (initializedIndices.contains(rsPoints.get(randPoint))){
                continue;
            }else {
                if (firstPt == null){
                    firstPt = rsPoints.get(randPoint);
                    lastChanges.add(firstPt);
                }
                else if (rsPoints.get(randPoint) != firstPt){
                    secondPt = rsPoints.get(randPoint);
                    lastChanges.add(secondPt);
                }
            }
        }

        int temp = sudoku[firstPt.x][firstPt.y];
        sudoku[firstPt.x][firstPt.y] = sudoku[secondPt.x][secondPt.y];
        sudoku[secondPt.x][secondPt.y] = temp;

    }
    public void undo(){
        Point firstPt = lastChanges.get(0);
        Point secondPt = lastChanges.get(1);
        int temp = sudoku[firstPt.x][firstPt.y];
        sudoku[firstPt.x][firstPt.y] = sudoku[secondPt.x][secondPt.y];
        sudoku[secondPt.x][secondPt.y] = temp;
    }
    public void simulatedAnnealing(){
        fillRandom();
        display();
        double K = 1;
        double tempMax = 1.0/3;
        double tempMin = 0;
        double coolingRate = 0.99999999999999999999995;
        int maxIterations = 1000000;

        int oldCost = cost();
        int iter = 0;
        for (double temp = tempMax; temp >=tempMin ; temp*=coolingRate) {
            for (int j = 0; j < maxIterations; j++) {
                generateSuccessor();
                int newCost = cost();
                int delta = newCost - oldCost;
                if (delta > 0) {
                    if (Math.random() > exp(-delta / (K * temp))) {
                        undo();
                    } else {
                        oldCost = newCost;
                    }
                } else {
                    oldCost = newCost;
                }
                if (j % 100000 == 0&&j != 0){
                    System.out.println("Iter : " + j + " - Fault Score : " + oldCost);
                    //display();
                }
                if (cost() == 0){
                    System.out.println("Fault Score of final Solution: "+oldCost);
                    System.out.println("Found in "+(iter+j)+". iteration");
                    break;
                }
            }

            if (cost() == 0) {
                break;
            }
            iter += maxIterations;
            if (iter % 100000 == 0){
                System.out.println("Iter : " + iter + " - Fault Score : " + oldCost);
                //display();
            }

        }
    }
}

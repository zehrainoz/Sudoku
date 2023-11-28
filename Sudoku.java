/*

    Description: This file contains fillRandom method which
    fills given sudoku randomly but each 3*3 square is correct and
    display method that shows sudoku board.

    Author: Zehra İnöz
    Contact: zehraiinozz@gmail.com

    Last Update: 29.11.2023 00.05

 */

import java.util.ArrayList;
import java.util.Collections;

public class Sudoku {

    private int[][] sudoku = {
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
}

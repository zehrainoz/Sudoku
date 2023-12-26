public class Main {
    public static void main(String[] args) {

        Sudoku newSudo = new Sudoku();
        //newSudo.fillRandom();
        System.out.println("Initial Board:");
        //newSudo.display();


        newSudo.simulatedAnnealing();
        System.out.println("\nFinal Solution:");
        newSudo.display();
    }

}
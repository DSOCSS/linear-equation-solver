package main;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Arthur Zarins
 */
public class LinearSolver {
    // when logOperations is true, all elementary row operations are logged
    private boolean logOperations = false;

    public void enableLogging() {
        logOperations = true;
    }

    /**
     * Operation 1. Swapping two rows
     */
    public void swapRows(ArrayList<ArrayList<Double>> matrix, int row1, int row2) {
        if (row1 == row2) return; // no effect
        for (int i = 0; i < matrix.get(row1).size(); i++) {
            double row1val = matrix.get(row1).get(i);
            double row2val = matrix.get(row2).get(i);
            matrix.get(row1).set(i, row2val);
            matrix.get(row2).set(i, row1val);
        }
        if (logOperations) {
            System.out.println("Swapping rows #" + row1 + " and #" + row2);
            printMatrix(matrix);
        }
    }

    /**
     * Operation 2. Multiplying a row by a nonzero scalar
     *
     * @throws Exception scalar = 0
     */
    public void scaleRow(ArrayList<ArrayList<Double>> matrix, int row, double scalar) throws Exception {
        if (scalar == 1) return; // no effect
        if (scalar == 0) throw new Exception("Attempted to multiply row by zero");
        for (int i = 0; i < matrix.get(row).size(); i++) {
            matrix.get(row).set(i, matrix.get(row).get(i) * scalar);
        }
        if (logOperations) {
            System.out.println("Scaling row #" + row + " by " + scalar);
            printMatrix(matrix);
        }
    }

    /**
     * Operation 3. Adding rows
     *
     * @param row_i     the row that will be changed
     * @param row_delta the row that will be added to row
     * @param c         scalar multiple of row_delta
     *                  row_i -> row_i + (c * row_delta)
     */
    private void addRows(ArrayList<ArrayList<Double>> matrix, int row_i, int row_delta, double c) {
        for (int i = 0; i < matrix.get(row_i).size(); i++) {
            Double ogValue = matrix.get(row_i).get(i);
            Double addedValue = c * matrix.get(row_delta).get(i);
            matrix.get(row_i).set(i, ogValue + addedValue);
        }
        if (logOperations) {
            System.out.println("Row #" + row_i + " -> row #" + row_i + " + row #" + row_delta + " * " + c);
            printMatrix(matrix);
        }
    }

    private static void printMatrix(ArrayList<ArrayList<Double>> matrix) {
        for (ArrayList<Double> row : matrix) {
            System.out.print("[");
            for (int i = 0; i < row.size(); i++) {
                System.out.print(row.get(i) + " \t");
                // add line between variables and constants
                if (i == row.size() - 2)
                    System.out.print("| ");
            }
            System.out.println("]");
        }
    }

    /**
     * Solve matrix by converting to reduced row echelon form
     */
    public void solveMatrix(ArrayList<ArrayList<Double>> matrix) {
        try {
            //convert to row echelon form
            convertToRowEchelon(matrix);

            if (logOperations) System.out.println("\nConverting to Reduced Row Echelon form");
            convertToReducedRowEchelon(matrix);

            //round the values in the matrix
            roundMatrix(matrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void convertToRowEchelon(ArrayList<ArrayList<Double>> matrix) throws Exception {
        int pivotCol = 0;
        int pivotRow = 0;
        while (pivotRow < matrix.size() && pivotCol < matrix.get(0).size()) {
            //begin below the pivot row, search for a pivot
            boolean pivotFound = false;
            for (int row = pivotRow; row < matrix.size(); row++) {
                if (matrix.get(row).get(pivotCol) != 0) {
                    //place this row at the top
                    swapRows(matrix, pivotRow, row);
                    pivotFound = true;
                    break;
                }
            }
            if (pivotFound) {
                //scale the pivot to 1
                scaleRow(matrix, pivotRow, (1 / matrix.get(pivotRow).get(pivotCol)));
                //eliminate all elements under the pivot
                for (int row = pivotRow + 1; row < matrix.size(); row++) {
                    addRows(matrix, row, pivotRow, -matrix.get(row).get(pivotCol));
                }
                pivotRow++; // move onto the next row
            }
            pivotCol++; // move to next column
        }
    }

    public void convertToReducedRowEchelon(ArrayList<ArrayList<Double>> matrix) throws Exception {
        //begin at last row, back substitution
        for (int pivotRow = matrix.size() - 1; pivotRow >= 0; pivotRow--) {
            //find pivot column
            for (int col = 0; col < matrix.get(pivotRow).size(); col++) {
                if (matrix.get(pivotRow).get(col) == 1) {
                    //reduce all rows above the pivot row
                    for (int row = 0; row < pivotRow; row++) {
                        addRows(matrix, row, pivotRow, -matrix.get(row).get(col));
                    }
                    break;
                }
            }

        }
    }

    /**
     * Round all elements to 5 decimal places
     */
    private static void roundMatrix(ArrayList<ArrayList<Double>> matrix) {
        final double multi = Math.pow(10, 5);
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).size(); c++) {
                double ogValue = matrix.get(r).get(c);
                matrix.get(r).set(c, Math.round(ogValue * multi) / multi);
            }
        }
    }

    public static boolean existsSolution(ArrayList<ArrayList<Double>> matrix) {
        int C = matrix.get(0).size();
        int R = matrix.size();
        boolean solutionRow;

        for (int r = 0; r < R; r++){
            if (matrix.get(r).get(C - 1) != 0) {
                solutionRow = false;
                for (int c = 0; c < C - 1; c++) {
                    if (matrix.get(r).get(c) != 0) {
                        solutionRow = true;
                    }
                }
                if (!solutionRow) return false;
            }
        }
        return true;
    }

    public static Double calcDeterminant (ArrayList<ArrayList<Double>> matrix){
        int n = matrix.size();
        //base case
        if (n == 1){
            return matrix.get(0).get(0);
        }
        double total = 0.0;
        for (int i = 0; i < n; i++) {
            ArrayList<ArrayList<Double>> sub = new ArrayList<>();
            //creates appropriate submatrix
            for (int r = 1; r < n; r++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int c = 0; c < n; c++) {
                    if (c != i) {
                        row.add(matrix.get(r).get(c));
                    }
                }
                sub.add((ArrayList) row.clone());
            }
            //recursively calculates determinant
            if (i % 2 == 0) {
                total += matrix.get(0).get(i) * calcDeterminant(sub);
            }
            else{
                total -= matrix.get(0).get(i) * calcDeterminant(sub);
            }
        }
        return total;
    }

    public static boolean existsInversion (ArrayList<ArrayList<Double>> matrix){
        return calcDeterminant(matrix) != 0.0;
    }

    public static ArrayList<ArrayList<Double>> invertMatrix (ArrayList<ArrayList<Double>> matrix){
        int n = matrix.size();
        LinearSolver rref = new LinearSolver();
        ArrayList<ArrayList<Double>> inverted = new ArrayList<>();
        //creates augmented matrix
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (r == c) {
                    matrix.get(r).add(1.0);
                }
                else {
                    matrix.get(r).add(0.0);
                }
            }
        }
        try{
            //convert to rref
            rref.convertToRowEchelon(matrix);
            rref.convertToReducedRowEchelon(matrix);
            //separates the second half of the rref matrix
            for (int i = 0; i < n; i++){
                ArrayList<Double> row = new ArrayList<>();
                for (int j = n; j < 2 * n; j++){
                    row.add(matrix.get(i).get(j));
                }
                inverted.add((ArrayList) row.clone());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return inverted;
    }

    /**
     * Main method gathers user input and solves the given system of linear equations
     */
    public static void main(String[] args) {
        // initialize an empty system of equations
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();

        System.out.println("Welcome! Keep typing in equations, then type 'END' when you are done.\n" +
                "When typing 'END', you can also include a '-l' tag to log each row operation.");
        Scanner input = new Scanner(System.in);
        int columnSize = -1;
        String in = input.nextLine();
        // repeatedly get user input until user types "END"
        while (!in.startsWith("END")) {
            ArrayList<String> inputRow = new ArrayList<>(List.of(in.split(" ")));
            try {
                // convert the input into numbers, add to matrix
                ArrayList<Double> numberRow = new ArrayList<>();
                for (String elem : inputRow) numberRow.add(Double.parseDouble(elem));

                if(columnSize == -1) columnSize = numberRow.size(); //update # columns in matrix
                if (numberRow.size() != columnSize) throw new IOException("Wrong number of columns");

                matrix.add(numberRow); // add valid row to matrix
            } catch (IOException e){
                System.out.println("Each row of your matrix needs to be size " + columnSize + " (# of items in first row)");
            } catch (Exception e) {
                System.out.println("Please input numbers, separated with spaces");
            }
            in = input.nextLine();
        }
        System.out.println("\nOriginal Matrix:");
        printMatrix(matrix);

        LinearSolver solver = new LinearSolver();
        if (in.contains("-l")) solver.enableLogging();
        if (in.contains("-l")) System.out.println();
        solver.solveMatrix(matrix);

        System.out.println("\nSimplified Matrix:");
        printMatrix(matrix);
        input.close();
    }
}
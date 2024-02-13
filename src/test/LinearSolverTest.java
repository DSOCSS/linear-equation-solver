package test;

import main.LinearSolver;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test cases for LinearSolver
 *
 * @author Arthur Zarins
 * */

class LinearSolverTest {

    @Test
    void swapTwoRows(){
        //original matrix
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        matrix.add(new ArrayList<>(List.of(4.0, 5.0, 6.0)));
        matrix.add(new ArrayList<>(List.of(7.0, 8.0, 9.0)));

        //intended matrix after row swap of rows 0, 1
        ArrayList<ArrayList<Double>> finalMatrix = new ArrayList<>();
        finalMatrix.add(new ArrayList<>(List.of(4.0, 5.0, 6.0)));
        finalMatrix.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        finalMatrix.add(new ArrayList<>(List.of(7.0, 8.0, 9.0)));

        // swap rows with logging disabled
        LinearSolver solver = new LinearSolver();
        solver.swapRows(matrix, 0,1);
        assertEquals(finalMatrix, matrix);
    }

    @Test
    void solveMatrixWithOneSolution(){
        //original matrix
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(List.of(3.0, -7.0, 4.0, 10.0)));
        matrix.add(new ArrayList<>(List.of(1.0, -2.0, 1.0, 3.0)));
        matrix.add(new ArrayList<>(List.of(2.0, -1.0, 2.0, 6.0)));

        //matrix in reduced row echelon form
        ArrayList<ArrayList<Double>> RREFmatrix = new ArrayList<>();
        RREFmatrix.add(new ArrayList<>(List.of(1.0, 0.0, 0.0, 2.0)));
        RREFmatrix.add(new ArrayList<>(List.of(0.0, 1.0, 0.0, 0.0)));
        RREFmatrix.add(new ArrayList<>(List.of(0.0, 0.0, 1.0, 1.0)));

        // solve matrix with logging disabled
        LinearSolver solver = new LinearSolver();
        solver.solveMatrix(matrix);
        assertEquals(RREFmatrix, matrix);
    }

    @Test
    void isSolution() {
        //matrix with solution
        ArrayList<ArrayList<Double>> testMatrix1 = new ArrayList<>();
        testMatrix1.add(new ArrayList<>(List.of(3.0, -7.0, 4.0, 10.0)));
        testMatrix1.add(new ArrayList<>(List.of(0.0, 0.0, 1.0, 3.0)));
        testMatrix1.add(new ArrayList<>(List.of(2.0, -1.0, 2.0, 6.0)));

        //matrix without solution
        ArrayList<ArrayList<Double>> testMatrix2 = new ArrayList<>();
        testMatrix2.add(new ArrayList<>(List.of(1.0, 0.0, 0.0, 2.0)));
        testMatrix2.add(new ArrayList<>(List.of(0.0, 0.0, 0.0, 3.0)));
        testMatrix2.add(new ArrayList<>(List.of(0.0, 0.0, 1.0, 1.0)));

        //test
        assertEquals(true, LinearSolver.existsSolution(testMatrix1));
        assertEquals(false, LinearSolver.existsSolution(testMatrix2));
    }
    /**
     *
     * 2 -2 -3 0 -2
     * 3 -3 -2 5 7
     * 1 -1 -2 -1 3
     * infinite results
     */
}
package test;

import main.LinearSolver;
import org.junit.jupiter.api.Test;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit 5 test cases for LinearSolver
 *
 * @author Arthur Zarins
 * @author Muneeb Chaudhary
 * */

class LinearSolverTest {

    /**
     * Test cases for elementary row operations
     * */

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
    void scaleRowByConstant(){
        //original matrix
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        matrix.add(new ArrayList<>(List.of(4.0, 5.0, 6.0)));
        matrix.add(new ArrayList<>(List.of(7.0, 8.0, 9.0)));

        //intended matrix ofter scaling row 1 by 2
        ArrayList<ArrayList<Double>> finalMatrix = new ArrayList<>();
        finalMatrix.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        finalMatrix.add(new ArrayList<>(List.of(8.0, 10.0, 12.0)));
        finalMatrix.add(new ArrayList<>(List.of(7.0, 8.0, 9.0)));

        // scale row 1 by 2 with logging disabled
        LinearSolver solver = new LinearSolver();
        try {
            solver.scaleRow(matrix, 1, 2);
        } catch(Exception e){
            e.printStackTrace();
        }
        assertEquals(finalMatrix, matrix);
    }

    @Test
    void addMultipleOfOneRowToAnother(){
        //original matrix
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        matrix.add(new ArrayList<>(List.of(4.0, 5.0, 6.0)));
        matrix.add(new ArrayList<>(List.of(7.0, 8.0, 9.0)));

        //intended matrix after adding 3 * row 1 to row 2
        ArrayList<ArrayList<Double>> finalMatrix = new ArrayList<>();
        finalMatrix.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        finalMatrix.add(new ArrayList<>(List.of(4.0, 5.0, 6.0)));
        finalMatrix.add(new ArrayList<>(List.of(19.0, 23.0, 27.0)));

        // swap rows with logging disabled
        LinearSolver solver = new LinearSolver();
        solver.addRows(matrix, 2,1,3);
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
        assertTrue(LinearSolver.existsSolution(testMatrix1));
        assertFalse(LinearSolver.existsSolution(testMatrix2));
    }

    @Test
    void calculateVectorSpaceSolution(){
        // original matrix
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(List.of(1.0, 2.0, 2.0, 2.0, 1.0)));
        matrix.add(new ArrayList<>(List.of(2.0, 4.0, 6.0, 8.0, 5.0)));
        matrix.add(new ArrayList<>(List.of(3.0, 6.0, 8.0, 10.0, 6.0)));

        // solution space
        ArrayList<ArrayList<Double>> solutionSpace = new ArrayList<>();
        solutionSpace.add(new ArrayList<>(List.of(-2.0, 0.0, 1.5, 0.0)));
        solutionSpace.add(new ArrayList<>(List.of(-2.0, 1.0, 0.0, 0.0)));
        solutionSpace.add(new ArrayList<>(List.of(2.0, 0.0, -2.0, 1.0)));

        LinearSolver solver = new LinearSolver();
        solver.solveMatrix(matrix);
        assertEquals(solutionSpace, LinearSolver.findSolutionSpace(matrix));
    }

    @Test
    void determinant() {
        //matrix
        ArrayList<ArrayList<Double>> testMatrix = new ArrayList<>();
        testMatrix.add(new ArrayList<>(List.of(2.0, 3.0, 7.0, 2.0)));
        testMatrix.add(new ArrayList<>(List.of(4.0, 8.0, 9.0, -5.0)));
        testMatrix.add(new ArrayList<>(List.of(1.0, 6.0, 3.0, -8.0)));
        testMatrix.add(new ArrayList<>(List.of(-7.0, -6.0, 3.0, 4.0)));

        //test
        assertEquals(-400.0, LinearSolver.calcDeterminant(testMatrix));
    }

    @Test
    void inverse() {
        //test matrix
        ArrayList<ArrayList<Double>> testMatrix = new ArrayList<>();
        testMatrix.add(new ArrayList<>(List.of(4.0, 3.0)));
        testMatrix.add(new ArrayList<>(List.of(2.0, 1.0)));
        //expected result matrix
        ArrayList<ArrayList<Double>> resultMatrix = new ArrayList<>();
        resultMatrix.add(new ArrayList<>(List.of(-0.5, 1.5)));
        resultMatrix.add(new ArrayList<>(List.of(1.0, -2.0)));
        //test
        LinearSolver env = new LinearSolver();
        assertEquals(resultMatrix, LinearSolver.invertMatrix(testMatrix));
    }
}
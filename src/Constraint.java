package com.company;

public class Constraint {

    static int[] assignedrow = new int[20];
    static int[] assignedcol = new int[20];

    static Cell[] colConstraints = new Cell[20];
    static Cell[] rowConstraints = new Cell[20];

    public static boolean isConstraint(Cell[][] cells, int rowC, int colC, int row, int column) {
        int sumCol = 0;
        int sumRow = 0;
        int assignVariablesCOl = 0;
        int assignVariablesRow = 0;

        for (int i = 1; i < row; i++) {
            if (cells[i][colC].value == cells[rowC][colC].value && i != rowC && cells[i][colC].type == 0)
                return true;
        }
        for (int j = 1; j < column; j++) {
            if (cells[rowC][j].value == cells[rowC][colC].value && j != colC && cells[rowC][j].type == 0)
                return true;
        }

        for (int i = colConstraints[colC].i + 1; i < row - 1; i++) {
            if (cells[i][colC].type == 0 && cells[i][colC].value != 0) {
                assignVariablesCOl++;
                sumCol += cells[i][colC].value;
            }
            if (sumCol > colConstraints[colC].value) {
                return true;
            }
            if (assignVariablesCOl + assignedcol[colC] == row) {
                if (sumCol != colConstraints[colC].value)
                    return true;
            }
        }


        // check horizontally

        for (int j = rowConstraints[rowC].j + 1; j < column; j++) {
            if (cells[rowC][j].type == 0 && cells[rowC][j].value != 0) {
                assignVariablesRow++;
                sumRow += cells[rowC][j].value;
            }
            if (sumRow > rowConstraints[rowC].value) {
                return true;
            }
            if (assignVariablesRow + assignedrow[rowC] == column) {
                if (sumRow != rowConstraints[rowC].value) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void countAssigned(Cell[][] cells, int row, int column) {
        for (int i = 0; i < row; i++) {
            int a = 0;
            for (int j = 0; j < column; j++) {
                if (cells[i][j].type == 1) {
                    a++;
                }
            }
            assignedrow[i] = a;
        }
        for (int i = 0; i < row; i++) {
            int a = 0;
            for (int j = 0; j < column; j++) {
                if (cells[j][i].type == 1) {
                    a++;
                }
            }
            assignedcol[i] = a;
        }
    }

    public static void findConstraint(Cell[][] cells, int row, int column) {
        colConstraints[0] = new Cell();
        rowConstraints[0] = new Cell();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (cells[i][j].value != -1 && colConstraints[j] == null) {
                    colConstraints[j] = cells[i][j];
                }
            }
        }
        for (int j = 0; j < column; j++) {
            for (int i = 0; i < row; i++) {
                if (cells[i][j].value != -1 && rowConstraints[i] == null) {
                    rowConstraints[i] = cells[i][j];
                }
            }
        }
    }
}

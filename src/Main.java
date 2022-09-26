package com.company;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int column = in.nextInt();
        int row = in.nextInt();
        Cell[][] cells = new Cell[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                cells[i][j] = new Cell(in.nextInt());
                cells[i][j].i = i;
                cells[i][j].j = j;
            }
        }
        CSP csp = new CSP();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (cells[i][j].value != 0) {
                    csp.setAssignVariables(cells[i][j], cells);
                }
            }
        }
        Constraint.countAssigned(cells, row, column);
        Constraint.findConstraint(cells, row, column);
        System.out.println("cps : " + csp.CSPBacktracking(cells, row, column));
    }
}

package com.company;

import java.util.*;

public class CSP {

    Hashtable<Integer, Boolean> assignedCells = new Hashtable();
    int rows;
    int cols;
    boolean ac3 = false;

    public boolean CSPBacktracking(Cell[][] cells, int row, int col) {

        boolean result;
        rows = row;
        cols = col;


        if (isCompleteAssignment(cells, row, col)) {
            System.out.println("solution is find");
            printSolution(cells);
            System.exit(0);
        }

        if (!ac3) {
            AC3(cells);
            ac3 = true;
            for (int i = 1; i < rows; i++) {
                for (int j = 1; j < cols; j++) {
                    if (cells[i][j].type == 0 && cells[i][j].remainingValues.size() == 0 &&
                            !(assignedCells.containsKey(cells[i][j].hash(cells, i, j)))) {
                        System.out.println("AC3 fail");
                        return false;
                    }
                }
            }
        }

        Cell selectedCell = MRVSelectVariable(cells);

        ArrayList<Integer> remaining_value = LCVOrdering(selectedCell, cells);

        for (int i = 0; i < remaining_value.size(); i++) {

            System.out.println("value selected:" + remaining_value.get(i));
            selectedCell.value = remaining_value.get(i);
            boolean constraint = Constraint.isConstraint(cells, selectedCell.i, selectedCell.j, rows, cols);
            if (!constraint) {

                copyFromRemainingValue(cells, selectedCell.i, selectedCell.j);
                selectedCell.removeValues(selectedCell.value);

                assignedCells.put(selectedCell.hash(cells, selectedCell.i, selectedCell.j), true);
                forwardChecking(cells, selectedCell);


                for (int z = 1; z < row; z++) {
                    for (int j = 1; j < col; j++) {
                        if (cells[z][j].type == 0 && cells[z][j].remainingValues.size() == 0 &&
                                !(assignedCells.containsKey(cells[z][j].hash(cells, z, j)))) {
                            assignedCells.remove(selectedCell.hash(cells, selectedCell.i, selectedCell.j), true);
                            copyFromStack(cells, selectedCell.i, selectedCell.j);
                            selectedCell.value = 0;
                            undoForwardChecking(cells, selectedCell);
                            return false;
                        }
                    }
                }

                result = CSPBacktracking(cells, row, col);

                if (result) {
                    System.out.println("solution is find");
                    printSolution(cells);
                    System.exit(0);
                } else {
                    assignedCells.remove(selectedCell.hash(cells, selectedCell.i, selectedCell.j), true);
                    copyFromStack(cells, selectedCell.i, selectedCell.j);
                    undoForwardChecking(cells, selectedCell);
                    selectedCell.value = 0;
                }

            } else {
                selectedCell.value = 0;
            }
        }
        return false;
    }

    public void AC3(Cell[][] cells) {

        boolean contradiction = false;
        Queue<Cell> queue = initializeQueue(cells);
        while (!queue.isEmpty() && !contradiction) {

            Cell x = queue.poll();
            //check vertically , direction code = 1
            for (int i = 1; i < rows; i++) {
                if (cells[i][x.j].type == 0 && !(assignedCells.containsKey(cells[i][x.j].hash(cells, i, x.j))) && i != x.i) {
                    Cell y = cells[i][x.j];
                    if (removeValues(cells, x, y, 1)) {
                        if (y.remainingValues.size() == 0) {
                            contradiction = true;
                        }
                        queue.add(y);
                    }
                }
            }
            //check horizontally , direction code = 0
            for (int j = 1; j < cols; j++) {
                if (cells[x.i][j].type == 0 && (!assignedCells.containsKey(cells[x.i][j].hash(cells, x.i, j))) && j != x.j) {
                    Cell y = cells[x.i][j];
                    if (removeValues(cells, x, y, 0)) {
                        if (y.remainingValues.size() == 0) {
                            contradiction = true;
                        }
                        queue.add(y);
                    }
                }
            }
        }
    }

    private boolean removeValues(Cell[][] cells, Cell x, Cell y, int direction) {

        boolean removed = false;

        for (int i = 0; i < y.remainingValues.size(); i++) {

            copyFromRemainingValue(cells, x.i, x.j);
            ArrayList<Integer> xCopyRemainingValues = x.listStack.pop();
            if (xCopyRemainingValues.contains(y.remainingValues.get(i))) {
                xCopyRemainingValues.remove(xCopyRemainingValues.indexOf(y.remainingValues.get(i)));
            }
            int uselessValue = 0;
            if (direction == 1) {
                int sumCol = 0;
                for (int a = Constraint.colConstraints[x.j].i + 1; a < rows; a++) {
                    if (cells[a][x.j].type == 0) {
                        sumCol += cells[a][x.j].value;
                    }
                }
                sumCol += y.remainingValues.get(i);
                uselessValue = Constraint.colConstraints[x.j].value - sumCol;
            } else {
                int sumRow = 0;
                for (int j = Constraint.rowConstraints[x.i].j + 1; j < cols; j++) {
                    if (cells[x.i][j].type == 0) {
                        sumRow += cells[x.i][j].value;
                    }
                }
                sumRow += y.remainingValues.get(i);
                uselessValue = Constraint.rowConstraints[x.i].value - sumRow;
            }
            for (int a = 0; a < xCopyRemainingValues.size(); a++) {
                if (xCopyRemainingValues.get(a) > uselessValue) {
                    xCopyRemainingValues.subList(a, xCopyRemainingValues.size()).clear();
                }
            }

            if (xCopyRemainingValues.size() == 0) {
                y.removeValues(y.remainingValues.get(i));
                removed = true;
            }
        }
        return removed;
    }

    private Queue<Cell> initializeQueue(Cell[][] cells) {
        Queue<Cell> queue = new LinkedList<>();
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (cells[i][j].type == 0 && !(assignedCells.containsKey(cells[i][j].hash(cells, i, j)))) {
                    queue.add(cells[i][j]);
                }
            }
        }
        return queue;
    }

    private void undoForwardChecking(Cell[][] cells, Cell selectedCell) {

        for (int i = 1; i < rows; i++) {
            if (cells[i][selectedCell.j].type == 0 && i != selectedCell.i &&
                    !(assignedCells.containsKey(cells[i][selectedCell.j].hash(cells, i, selectedCell.j)))) {
                copyFromStack(cells, i, selectedCell.j);
            }

        }
        for (int j = 1; j < cols; j++) {
            if (cells[selectedCell.i][j].type == 0 && j != selectedCell.j &&
                    !(assignedCells.containsKey(cells[selectedCell.i][j].hash(cells, selectedCell.i, j)))) {
                copyFromStack(cells, selectedCell.i, j);
            }
        }
    }

    private void forwardChecking(Cell[][] cells, Cell selectedCell) {

        for (int i = 1; i < rows; i++) {
            if (cells[i][selectedCell.j].type == 0 && i != selectedCell.i &&
                    !(assignedCells.containsKey(cells[i][selectedCell.j].hash(cells, i, selectedCell.j)))) {

                copyFromRemainingValue(cells, i, selectedCell.j);
                cells[i][selectedCell.j].removeValues(selectedCell.value);
            }
        }

        for (int j = 1; j < cols; j++) {
            if (cells[selectedCell.i][j].type == 0 && j != selectedCell.j &&
                    !(assignedCells.containsKey(cells[selectedCell.i][j].hash(cells, selectedCell.i, j)))) {

                copyFromRemainingValue(cells, selectedCell.i, j);
                cells[selectedCell.i][j].removeValues(selectedCell.value);
            }
        }

        //check vertically
        int sumCol = 0;
        for (int i = Constraint.colConstraints[selectedCell.j].i + 1; i < rows; i++) {
            if (cells[i][selectedCell.j].value != -1 && cells[i][selectedCell.j].type == 0) {
                sumCol += cells[i][selectedCell.j].value;
            }
        }
        int uselessValue = Constraint.colConstraints[selectedCell.j].value - sumCol;
        for (int i = Constraint.colConstraints[selectedCell.j].i + 1; i < rows; i++) {
            if (cells[i][selectedCell.j].value != -1 && cells[i][selectedCell.j].type == 0 &&
                    !(assignedCells.containsKey(cells[i][selectedCell.j].hash(cells, i, selectedCell.j)))) {

                for (int a = 0; a < cells[i][selectedCell.j].remainingValues.size(); a++) {
                    if (cells[i][selectedCell.j].remainingValues.get(a) > uselessValue) {
                        cells[i][selectedCell.j].removeFromIndex(a);
                        break;
                    }
                }
            }
        }
        // check horizontally
        int sumRow = 0;
        for (int j = Constraint.rowConstraints[selectedCell.i].j + 1; j < cols; j++) {
            if (cells[selectedCell.i][j].type == 0) {
                sumRow += cells[selectedCell.i][j].value;
            }
        }
        uselessValue = Constraint.rowConstraints[selectedCell.i].value - sumRow;

        for (int j = Constraint.rowConstraints[selectedCell.i].j + 1; j < cols; j++) {
            if (cells[selectedCell.i][j].value != -1 && cells[selectedCell.i][j].type == 0) {
                for (int a = 0; a < cells[selectedCell.i][j].remainingValues.size(); a++) {
                    if (cells[selectedCell.i][j].remainingValues.get(a) > uselessValue) {
                        cells[selectedCell.i][j].removeFromIndex(a);
                        break;
                    }
                }
            }
        }

    }

    public void copyFromRemainingValue(Cell[][] cells, int i, int j) {

        ArrayList<Integer> remaining_copy = new ArrayList<>();
        for (int a = 0; a < cells[i][j].remainingValues.size(); a++) {
            remaining_copy.add(cells[i][j].remainingValues.get(a));
        }
        cells[i][j].listStack.push(remaining_copy);
    }

    public void copyFromStack(Cell[][] cells, int i, int j) {

        cells[i][j].remainingValues.clear();
        ArrayList<Integer> remaining_copy = cells[i][j].listStack.pop();
        for (int a = 0; a < remaining_copy.size(); a++) {
            cells[i][j].remainingValues.add(remaining_copy.get(a));
        }
    }


    private void printSolution(Cell[][] cells) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j].value + " ");
            }
            System.out.print("\n");
        }
    }

    private ArrayList<Integer> LCVOrdering(Cell selectedCell, Cell[][] cells) {

        HashMap<Integer, Integer> leastConstraint = new HashMap<>();

        for (int i = 0; i < selectedCell.remainingValues.size(); i++) {

            int countConstraint = 0;

            // check vertically
            for (int a = 1; a < rows; a++) {
                if (cells[a][selectedCell.j].type == 0 && a != selectedCell.i &&
                        !(assignedCells.containsKey(cells[a][selectedCell.j].hash(cells, a, selectedCell.j)))) {
                    if (cells[a][selectedCell.j].remainingValues.contains(selectedCell.remainingValues.get(i))) {
                        countConstraint++;
                    }
                }
            }
            // check horizontally
            for (int j = 1; j < cols; j++) {
                if (j != selectedCell.j && cells[selectedCell.i][j].type == 0 &&
                        !(assignedCells.containsKey(cells[selectedCell.i][j].hash(cells, selectedCell.i, j)))) {
                    if (cells[selectedCell.i][j].remainingValues.contains(selectedCell.remainingValues.get(i))) {
                        countConstraint++;
                    }
                }
            }

            int sumCol = 0;

            for (int a = Constraint.colConstraints[selectedCell.j].i + 1; a < rows; a++) {
                if (cells[a][selectedCell.j].value != -1 && cells[a][selectedCell.j].type == 0) {
                    sumCol += cells[a][selectedCell.j].value;
                }
            }
            sumCol += selectedCell.remainingValues.get(i);

            int uselessValue = Constraint.colConstraints[selectedCell.j].value - sumCol;
            for (int k = Constraint.colConstraints[selectedCell.j].i + 1; k < rows; k++) {
                if (cells[k][selectedCell.j].type == 0 && k != selectedCell.i &&
                        !(assignedCells.containsKey(cells[k][selectedCell.j].hash(cells, k, selectedCell.j)))) {

                    for (int a = 0; a < cells[k][selectedCell.j].remainingValues.size(); a++) {
                        if (cells[k][selectedCell.j].remainingValues.get(a) > uselessValue && cells[k][selectedCell.j].remainingValues.get(a) != selectedCell.remainingValues.get(i)) {
                            countConstraint += (cells[k][selectedCell.j].remainingValues.size() - a);
                            break;
                        }
                    }
                }
            }

            // check horizontally
            int sumRow = 0;
            for (int j = Constraint.rowConstraints[selectedCell.i].j + 1; j < cols; j++) {
                if (cells[selectedCell.i][j].type == 0) {
                    sumRow += cells[selectedCell.i][j].value;
                }
            }
            sumRow = selectedCell.remainingValues.get(i);
            uselessValue = Constraint.rowConstraints[selectedCell.i].value - sumRow;


            for (int j = Constraint.rowConstraints[selectedCell.i].j + 1; j < cols; j++) {
                if (j != selectedCell.j && cells[selectedCell.i][j].type == 0
                        && !(assignedCells.containsKey(cells[selectedCell.i][j].hash(cells, selectedCell.i, j)))) {
                    for (int a = 0; a < cells[selectedCell.i][j].remainingValues.size(); a++) {
                        if (cells[selectedCell.i][j].remainingValues.get(a) > uselessValue && cells[selectedCell.i][j].remainingValues.get(a) != selectedCell.remainingValues.get(i)) {
                            countConstraint += (cells[selectedCell.i][j].remainingValues.size() - a);
                            break;
                        }
                    }
                }
            }
            leastConstraint.put(selectedCell.remainingValues.get(i), countConstraint);
        }
        System.out.println("HashMap in lcv: " + leastConstraint);
        ArrayList<Integer> sortedKeys = new ArrayList<>(leastConstraint.keySet());
        Collections.sort(sortedKeys);
        return sortedKeys;
    }


    private Cell MRVSelectVariable(Cell[][] cells) {

        int min = Integer.MAX_VALUE;
        Cell minRemainingCellValue = new Cell();
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (cells[i][j].remainingValues.size() < min &&
                        !(assignedCells.containsKey(cells[i][j].hash(cells, i, j)))) {
                    minRemainingCellValue = cells[i][j];
                    min = cells[i][j].remainingValues.size();
                }
            }
        }
        System.out.println("selected cell : " + minRemainingCellValue.i + " " + minRemainingCellValue.j + " " + minRemainingCellValue.remainingValues.size() + " " + minRemainingCellValue.remainingValues);
        return minRemainingCellValue;
    }

    private boolean isCompleteAssignment(Cell[][] cells, int row, int col) {
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (cells[i][j].value == 0)
                    return false;
            }
        }
        return true;
    }


    public void setAssignVariables(Cell cell, Cell[][] cells) {
        cell.type = 1;
        cell.remainingValues.clear();
        cell.remainingValues.add(cell.value);
        int hash = cell.hash(cells, cell.i, cell.j);
        assignedCells.put(hash, true);
    }
}

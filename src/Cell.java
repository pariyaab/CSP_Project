package com.company;

import java.util.ArrayList;
import java.util.Stack;

public class Cell {
    int type = 0; // 0 for blank cells and 1 for sumcelss
    int i;
    int j;
    int value;

    ArrayList<Integer> remainingValues = new ArrayList<>();

    Stack<ArrayList<Integer>> listStack = new Stack<>();

    public Cell(int value) {
        this.value = value;
        remainingValues.add(1);
        remainingValues.add(2);
        remainingValues.add(3);
        remainingValues.add(4);
        remainingValues.add(5);
        remainingValues.add(6);
        remainingValues.add(7);
        remainingValues.add(8);
        remainingValues.add(9);

    }

    public Cell() {
    }

    public void removeValues(int value) {
        if (remainingValues.contains(value)) {
            remainingValues.remove(remainingValues.indexOf(value));
        }
    }

    public void removeFromIndex(int index){
        remainingValues.subList(index,remainingValues.size()).clear();
    }
    public int hash(Cell[][] cells, int ii, int jj) {

        int hash = java.util.Arrays.deepHashCode(cells);
        int k = Integer.valueOf(String.valueOf(ii) + String.valueOf(jj));
        hash += k;
        return hash;
    }
}

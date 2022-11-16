# CSP_Project
Constraint satisfaction problem solver in kakrud game (simple version) is implemented in this project with JAVA language.

## Project Structure
In this project we have three classes :
* Cell 
* Constraint
* CSP

In the cell class, I modeled every cell as a variable with a range of 0-9 valid numbers, and moreover, each cell had an attribute with name type and value 0 or 1. 0 means that this cell should be filled by the program, and 1 means that this cell is filled now.
Constraints are being checked in the constraint class and in this class we have three methods countAssigned, findConstraint, isConstraint and I will explain below what each of them does.
countAssigned counts the number of cells that are filled now and we do not change the value of them in other words, the type them is equal to 0.
findConstraint finds the value of constraints. For example, find that the sum of all cells' values should be 5 in the first row.
isConstraint gives a cell as input and checks whether it can break satisfaction or not.


# How does this project work?

First of all CSPBacktracking algorithms run and the AC3 algorithm call just for once.
After that MRVSelection function call for the assigned minimum remaining value to the selected cell and after that LCVOrdering call.
This function checks that with this value of a cell, how many values remain with other cells that have constraints with it and sort them descending.

After that, we get a copy of all values for backtracking later and call forwardChecking.
This function removes value from the list of allowed numbers of other cells that already are in the same column or row with the selected cell.

If one cell the size of the remaining values is equal to 0, then we call undoForwarchecking.

Eventually, after all of the actions, I mentioned before and repeat them for all cells and find the correct answer for our game, print it.

for example correct answer for 5*5 map is :

![image](https://user-images.githubusercontent.com/46067199/196256365-cf02b838-db05-4a6a-8ed3-ce8bf32dadd1.png)



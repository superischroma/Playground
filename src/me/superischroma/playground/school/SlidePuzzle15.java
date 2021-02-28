package me.superischroma.playground.school;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SlidePuzzle15
{
    private static final byte LEFT = 0, RIGHT = 1, ABOVE = 2, BELOW = 3;

    /**
     * The puzzle that the player navigates
     */
    private static int[][] puzzle = new int[4][4];

    /**
     * The current row and column of the blank space.
     */
    private static int blankRow = 3, blankCol = 3;

    /**
     * Fills puzzle as follows:
     * 1   2   3   4
     * 5   6   7   8
     * 9   10  11  12
     * 13  14  15  0
     *
     * calls the scramble method after puzzle is filled
     */
    public static void create()
    {
        for (int i = 0, c = 1; i < puzzle.length; i++)
        {
            for (int j = 0; j < puzzle[i].length; j++, c++)
                puzzle[i][j] = (c <= 15 ? c : 0);
        }
    }

    /**
     * Swaps puzzle[blankRow][blankCol] with puzzle[a][b]
     * AND THEN sets blankRow to a and blankCol to b
     */
    public static void swap(int a, int b)
    {
        int val = puzzle[blankRow][blankCol];
        puzzle[blankRow][blankCol] = puzzle[a][b];
        puzzle[a][b] = val;
        blankRow = a;
        blankCol = b;
    }

    /**
     * Completes 100 swaps between puzzle[blankRow][blankCol] and
     * a randomly generated adjacent cell (left, right, above, or below).
     * Make sure that the randomly generated adjacent cell is in bounds.
     */
    public static void scramble()
    {
        for (int i = 0; i < 100; i++)
        {
            byte direction;
            int[] loc;
            do
            {
                direction = (byte) ThreadLocalRandom.current().nextInt(LEFT, BELOW + 1);
                loc = getLocationOfMove(direction);
            }
            while (loc[0] < 0 || loc[0] >= puzzle.length || loc[1] < 0 || loc[1] >= puzzle[0].length);
            swap(loc[0], loc[1]);
        }
    }

    /**
     * Gets the location that would be moved to if the direction was chosen
     * @param direction The specified direction.
     * @return A 2-element integer array containing the location of the movement
     */
    private static int[] getLocationOfMove(byte direction)
    {
        int[] location = new int[]{blankRow, blankCol};
        switch (direction)
        {
            case LEFT: location[1] = blankCol - 1; break;
            case RIGHT: location[1] = blankCol + 1; break;
            case ABOVE: location[0] = blankRow - 1; break;
            case BELOW: location[0] = blankRow + 1; break;
        }
        return location;
    }

    /**
     * Outputs the puzzle, with a blank space for value 0. (Use \t between all elements on a row)
     * Possible outcome of display method (0 is located at [2][2]):
     * 1	 2	 3 	4
     * 5	 6	 10	8
     * 13 9		   7
     * 11 14  15	12
     */
    public static void display()
    {
        for (int[] is : puzzle)
        {
            for (int i : is)
                System.out.print((i != 0 ? (i > 9 ? i : i + " ") : "  ") + " ");
            System.out.println();
        }
    }

    /**
     * Returns true if puzzle is organized as follows:
     * 1   2   3   4
     * 5   6   7   8
     * 9   10  11  12
     * 13  14  15  0
     *
     * Returns false otherwise
     */
    public static boolean won()
    {
        int c = 1;
        for (int[] is : puzzle)
        {
            for (int i : is)
            {
                if ((c <= 15 && i == 0) || (i != c && c <= 15))
                    return false;
                c++;
            }
        }
        return true;
    }

    /**
     * Generates a new solvable puzzle and allows player to move pieces one at a time
     * until numbers 1 through 15 are restored in ascending order.
     */
    /*
    public static void main(String[] args){
        create();
        System.out.println(won());
        display();
        scramble();
        display();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter W to move up, A to move left, S to move down, D to move right");
        String key = input.next();
        while(!won()){
            System.out.println();
            switch(key.toUpperCase().charAt(0)){
                case 'S' :  //swap with piece above blank
                    if (blankRow > 0){
                        swap(blankRow - 1, blankCol);
                    }
                    else{
                        System.out.println("Error: out of bounds");
                    }
                    break;
                case 'D' : //swap with piece to left of blank
                    if (blankCol > 0){
                        swap(blankRow, blankCol - 1);
                    }
                    else{
                        System.out.println("Error: out of bounds");
                    }
                    break;

                case 'W' :  //swap with piece below blank
                    if (blankRow < puzzle.length - 1){
                        swap(blankRow + 1, blankCol);
                    }
                    else{
                        System.out.println("Error: out of bounds");
                    }
                    break;

                case 'A' : //swap with piece to right of blank
                    if (blankCol < puzzle[0].length - 1){
                        swap(blankRow, blankCol + 1);
                    }
                    else{
                        System.out.println("Error: out of bounds");
                    }
                    break;

                default: System.out.println("Error: didn't recognize input");
            }
            display();
            if (!won()){
                System.out.println("Enter W to move up, A to move left, S to move down, D to move right");
                key = input.next();
            }
        }
        System.out.println("Congratulations, you solved the puzzle!");
    }

     */
}
package me.superischroma.playground.school;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class TableOfRandomDigits
{
    // i use ThreadLocalRandom over Random because not only is it more random, but it's also much simpler to use.
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    public static final int ROWS = 20;
    public static final int COLS = 50;
    private int[][] table = new int[ROWS][COLS];
    private int currRow;
    private int currCol;

    public TableOfRandomDigits()
    {
        generate();
    }

    /*
     * Sets all values in table to random digits between 0 and 9 inclusive
     */
    public void generate()
    {
        for (int i = 0; i < table.length; i++)
        {
            for (int j = 0; j < table[i].length; j++)
                table[i][j] = RANDOM.nextInt(0, 10);
        }
    }

    /**
     * Creates and returns a string of all of the digits in the table, with a tab between every 5 digits
     * and an additional line break between every 5 lines.
     * Sample formatted table printed at the bottom of this program
     */
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < table.length; i++)
        {
            if (i % 5 == 0)
                builder.append("\n");
            for (int j = 0; j < table[i].length; j++)
                builder.append(table[i][j]).append("\t");
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Generates a random row and column number as a starting point within the table
     */
    private void generateLocation(){
        currRow = RANDOM.nextInt(0, ROWS); //**TEMPORARY - FIX**
        currCol = RANDOM.nextInt(0, COLS); //**TEMPORARY - FIX**
        System.out.println("Starting location: (" + currRow + ", " + currCol + ")");
    }

    /**
     * Advances to the next location by moving one column to the right.
     * If column is out of bounds, move one row down and start at column 0.
     * If row is out of bounds, start at row 0
     * EXAMPLES:
     * If currRow is 15 and currCol is 8, advance will make currRow 15 and currCol 9
     * If currRow is 21 and currCol is COLS - 1, advance will make currRow 22 and currCol 0
     * If currRow is ROWS - 1 and currCol is COLS - 1, advance will make currRow 0 and currCol 0
     */
    public void advance()
    {
        if (currCol == COLS - 1)
        {
            currCol = 0;
            if (currRow == ROWS - 1)
                currRow = 0;
            else
                currRow++;
            return;
        }
        currCol++;
    }

    /**
     * Calculates and returns the number of digits in the parameter num
     * Example: if num is 3456, then 4 is returned
     */
    private int countNumDigits(int num)
    {
        return String.valueOf(num).length();
    }

    /**
     * Returns true if nums contains value, false otherwise
     * Examples:
     * arrayHas([42, 4, 54, 10, 0, 0], 4) returns true
     * arrayHas([42, 4, 54, 10, 0, 0], 50) returns false
     */
    public boolean arrayHas(int[] nums, int value)
    {
        return Arrays.stream(nums).filter(i -> i == value).toArray().length > 0;
    }

    /**
     * Builds and returns an array of "numValues" ints built from the table, each value falling between min and max inclusive
     * See testing video on Blackboard instructions for information on how to verify that the method works
     */
    private int[] getRandomNumbers(int numValues, int min, int max, boolean replacement){
        int numDigits = countNumDigits(max); //the number of digits in max (tells us how many digits to pull per random number)
        int[] nums = new int[numValues];  //the final array of random numbers

        int[] digits = new int[numDigits];
        generateLocation();

        for (int i = 0; i < nums.length;) //NOTE: i++ is INTENTIONALLY left off here. Don't "correct" it.
        {
            for (int j = 0; j < digits.length; j++)
            {
                digits[j] = table[currRow][currCol];
                advance();
            }
            StringBuilder sVal = new StringBuilder();
            for (int digit : digits)
                sVal.append(digit);
            int value = Integer.parseInt(sVal.toString());
            if ((value >= min && value <= max) && (replacement || !arrayHas(nums, value)))
            {
                nums[i] = value;
                i++;
            }
        }
        return nums;
    }

    /**
     * Generates and returns a simple random sample of numbers between min and max with replacement
     */
    public int[] generateWithReplacement(int numValues, int min, int max){
        return getRandomNumbers(numValues, min, max, true);
    }

    /**
     * Generates and returns a simple random sample of numbers between min and max without replacement
     */
    public int[] generateWithoutReplacement(int numValues, int min, int max){
        return getRandomNumbers(numValues, min, max, false);
    }
}


/**
 SAMPLE FORMATTED TABLE

 0:    92781	68026	17521	00883	09923	33261	19758	54689	46318	19951
 1:    27630	55881	44699	96817	47311	03817	17357	37548	11249	57817
 2:    64169	02048	95179	31139	53945	64515	34350	91977	64533	09632
 3:    45603	90334	30721	20788	45609	49499	31902	36468	48508	83321
 4:    39289	40578	97504	14358	00242	58386	32313	56922	92366	04798

 5:    62408	95177	08915	31195	74246	49518	58254	94591	89525	61243
 6:    46628	93397	20540	52825	25611	17326	64391	75058	43314	88317
 7:    91226	82323	19602	17697	59614	90843	26118	12968	46178	63206
 8:    79412	62462	93102	31405	25719	41544	33860	43558	64378	21768
 9:    63890	44144	76292	51866	28829	91973	34950	12173	45045	12885

 10:   55693	85737	42966	35015	46174	61212	24794	69875	76827	23176
 11:   57378	39498	85324	94085	32527	14723	51158	87556	94970	95190
 12:   99709	03315	34963	76415	22200	11287	86115	35698	23489	08555
 13:   19806	40605	07997	84306	57266	78561	65005	13255	77851	15348
 14:   77865	02530	12323	98595	82087	93665	08207	81930	71261	91203

 15:   40437	55027	26172	04684	02180	14132	39023	56898	63549	97860
 16:   39855	35021	29158	88791	06913	26111	97977	35213	91833	00280
 17:   17775	42947	22905	54221	29728	43043	71759	02418	92840	84031
 18:   34577	46070	16164	49602	66749	66539	55283	13906	80925	25732
 19:   61788	30667	09769	39023	34131	87462	34642	44803	37878	69962


 */
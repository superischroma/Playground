package me.superischroma.playground.school;

import me.superischroma.playground.command.Messenger;

import java.util.Arrays;

public class Primes
{
    /**
     * @param size the length of the array to be built
     * @param min the smallest value for any element of the array
     * @param max the largest value for any element of the array
     * @return an array containing "size" elements, with each element randomly generated between min and max inclusive
     */
    public static int[] buildArray(int size, int min, int max)
    {
        int[] arr = new int[size]; //TEMPORARY
        arr[0] = min;
        for (int i = 1; i < arr.length - 1; i++)
            arr[i] = (int) (Math.random() * ((max - min) + 1) + min);
        arr[arr.length - 1] = max;
        return arr;
    }


    /**
     * @param n the value to be tested for being prime
     * @return true if num is prime, false otherwise
     */
    private static boolean isPrime(int n)
    {
        if (n <= 1)
            return false;
        else if (n == 2)
            return true;
        else if (n % 2 == 0)
            return false;
        
        for (int i = 3; i <= Math.sqrt(n); i += 2)
        {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    /**
     * !! UNUSED METHOD !!
     * @param arr the array from which the prime numbers are to be counted
     * @return the number of prime values in arr. For example, if arr contains
     *    [10, 2, 80, 76, 19, 67, 4, 82, 81, 61], then countPrimes returns 4
     */
    public static int countPrimes(int[] arr)
    {
        return Arrays.stream(arr).filter(Primes::isPrime).toArray().length;
    }

    /**
     * @param arr the array from which the maximum is to be extracted
     * @return the maximum value of arr. For example if arr contains
     *    [2, 19, 67, 61], then getMax returns 67
     */
    public static int getMax(int[] arr)
    {
        return Arrays.stream(arr).max().getAsInt();
    }


    /**
     * Pre: 0 <= numPrimes <= arr.length
     * Post: arr is unchanged
     * @param arr
     * UNUSED PARAMETER: @param numPrimes the number of prime values in arr
     * @return an array consisting of the prime values of arr. For example if arr contains [10, 2, 80, 76, 19, 67, 4, 82, 81, 61],
     *    the returned array contains [2, 19, 67, 61]
     */
    public static int[] buildPrimesArray(int[] arr)
    {
        return Arrays.stream(arr).filter(Primes::isPrime).toArray();
    }


    /**
     * Outputs the contents of arr on a single line.
     * Example: [10, 54, 80, 76, 54, 67, 4, 82, 81, 61]
     * @param arr the array to be printed
     */
    public static void printArray(int[] arr)
    {
        Messenger.send(Arrays.toString(arr));
    }

    /*
    /**
     * Randomly creates an array size, and minimum and maximum values for the elements of the array.
     * Prints the array of randomly generated values.
     * Prints an array containing only the prime numbers, and the largest prime number
     *
    public static void main(String[] args)
    {
        int size = (int)(Math.random() * 20) + 1;
        int min = (int)(Math.random() * 51);
        int max = (int)(Math.random() * 101 - min) + min;

        //build and output starting array
        int[] mainArr = buildArray(size, min, max);
        System.out.println("Your array is:");
        printArray(mainArr);

        //build and output array for prime numbers
        // UNUSED VARIABLE: int primeCount = countPrimes(mainArr);
        int[] primes = buildPrimesArray(mainArr);
        if (primes.length != 0)
        {
            System.out.println("Your prime numbers are:");
            printArray(primes);

            //get and output largest prime number
            int primeMax = getMax(primes);
            System.out.println("The largest prime number is " + primeMax);
        }
        else
        {
            System.out.println("No primes in your array!");
        }
    }
    */
}
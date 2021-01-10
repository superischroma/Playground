package me.superischroma.playground.math;

public final class MathUtils
{
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

    public static double cqrt(double d)
    {
        return Math.pow(d, 1.0 / 3.0);
    }
}
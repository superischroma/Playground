package me.superischroma.playground.math;

public enum MathOperator
{
    ADD('+'),
    SUBTRACT('-'),
    MULTIPLY('*', 'x'),
    DIVIDE('/', '÷'),
    MODULO('%'),
    AND('&'),
    OR('|');

    private final char[] operators;

    MathOperator(char... operators)
    {
        this.operators = operators;
    }

    public static MathOperator valueOf(char test)
    {
        for (MathOperator mo : values())
        {
            for (char operator : mo.operators)
            {
                if (operator == test)
                    return mo;
            }
        }
        return null;
    }
}
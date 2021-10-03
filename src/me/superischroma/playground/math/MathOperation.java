package me.superischroma.playground.math;

public class MathOperation implements Comparable<MathOperation>
{
    private MathOperator operator;
    private double operand1;
    private double operand2;
    private int parenthesis;

    MathOperation(MathOperator operator, double operand1, double operand2, int parenthesis)
    {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.parenthesis = parenthesis;
    }

    public int priority()
    {
        return parenthesis + operator.ordinal();
    }

    @Override
    public int compareTo(MathOperation other)
    {
        return Integer.compare(priority(), other.priority());
    }
}
package me.superischroma.playground.math;

public class MathOperation implements Comparable<MathOperation>
{
    MathOperator operator;
    Double operand1;
    Double operand2;
    Integer parenthesis;

    MathOperation(MathOperator operator, double operand1, double operand2, int parenthesis)
    {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.parenthesis = parenthesis;
    }

    MathOperation()
    {
        this.operator = null;
        this.operand1 = null;
        this.operand2 = null;
        this.parenthesis = null;
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
package me.superischroma.playground.math;

import me.superischroma.playground.recreation.HashMap;
import me.superischroma.playground.recreation.PriorityQueue;

public class AlgebraExpression
{
    private final String equation;
    private final HashMap<Character, Double> variables;
    private final PriorityQueue<MathOperation> operations;

    public AlgebraExpression(String equation)
    {
        this.equation = equation.trim();
        this.variables = new HashMap<>();
        this.operations = new PriorityQueue<>(PriorityQueue.GREATEST);
    }

    public AlgebraExpression let(char var)
    {
        variables.put(var, null);
        return this;
    }

    public AlgebraExpression let(char var, double value)
    {
        variables.put(var, value);
        return this;
    }

    public double solve(char x)
    {
        MathOperation operation = new MathOperation();
        StringBuilder num = new StringBuilder();
        int plevel = 0;
        for (int i = 0; i < equation.length(); i++)
        {
            char current = equation.charAt(i);
            Double var = variables.get(x);
            if (var != null)
            {
                if (num.length() != 0)
                    operation.operator = MathOperator.MULTIPLY;
            }
            if (current == '(')
            {
                plevel++;
                continue;
            }
            if (current == ')')
            {
                plevel--;
                continue;
            }
            MathOperator operator = MathOperator.valueOf(current);
            if (operator == null)
                num.append(current);
        }
    }
}
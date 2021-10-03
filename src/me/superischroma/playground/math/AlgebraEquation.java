package me.superischroma.playground.math;

import me.superischroma.playground.recreation.HashMap;
import me.superischroma.playground.recreation.PriorityQueue;

public class AlgebraEquation
{
    private HashMap<Character, Double> variables;
    private PriorityQueue<MathOperation> operations;

    public AlgebraEquation(String equation)
    {
        this.variables = new HashMap<>();
        this.operations = new PriorityQueue<>(PriorityQueue.GREATEST);
    }

    public AlgebraEquation let(char var)
    {
        variables.put(var, null);
        return this;
    }

    public AlgebraEquation let(char var, double value)
    {
        var g = new Object() {

        };
        variables.put(var, value);
        return this;
    }

    public double solve(char x)
    {

    }
}
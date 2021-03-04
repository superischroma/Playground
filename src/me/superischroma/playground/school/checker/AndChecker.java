package me.superischroma.playground.school.checker;

import me.superischroma.playground.school.checker.Checker;

public class AndChecker implements Checker
{
    private final Checker c1;
    private final Checker c2;

    public AndChecker(Checker c1, Checker c2)
    {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public boolean accept(String string)
    {
        return c1.accept(string) && c2.accept(string);
    }
}
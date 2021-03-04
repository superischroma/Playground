package me.superischroma.playground.school.checker;

import me.superischroma.playground.school.checker.Checker;

public class NotChecker implements Checker
{
    private final Checker checker;

    public NotChecker(Checker checker)
    {
        this.checker = checker;
    }

    @Override
    public boolean accept(String string)
    {
        return !checker.accept(string);
    }
}
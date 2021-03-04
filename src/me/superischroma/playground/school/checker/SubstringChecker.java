package me.superischroma.playground.school.checker;

import me.superischroma.playground.school.checker.Checker;

public class SubstringChecker implements Checker
{
    private final String sub;

    public SubstringChecker(String sub)
    {
        this.sub = sub;
    }

    @Override
    public boolean accept(String string)
    {
        return string.contains(sub);
    }
}
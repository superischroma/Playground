package me.superischroma.playground.school.checker;

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
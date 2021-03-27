package me.superischroma.playground.school.checker;

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
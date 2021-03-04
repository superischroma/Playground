package me.superischroma.playground.command;

import me.superischroma.playground.school.checker.AndChecker;
import me.superischroma.playground.school.checker.Checker;
import me.superischroma.playground.school.checker.NotChecker;
import me.superischroma.playground.school.checker.SubstringChecker;

@Command(description = "parse")
public class TestCommand extends AbstractCommand
{
    @Override
    public void run(String[] args)
    {
        Checker sub = new SubstringChecker("broccoli");
        send(String.valueOf(sub.accept("i love broccoli"))); // true
        send(String.valueOf(sub.accept("i love beans"))); // false
        Checker sub2 = new SubstringChecker("beans");
        Checker and = new AndChecker(sub, sub2);
        send(String.valueOf(and.accept("i love broccoli and beans"))); // true
        send(String.valueOf(and.accept("i love broccoli"))); // false
        Checker not = new NotChecker(sub);
        send(String.valueOf(not.accept("i love broccoli"))); // false
        send(String.valueOf(not.accept("i love beans"))); // true
    }
}
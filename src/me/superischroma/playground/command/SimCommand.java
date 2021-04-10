package me.superischroma.playground.command;

import me.superischroma.playground.Sim;
import me.superischroma.playground.util.ArraysX;

import java.io.File;
import java.io.FileNotFoundException;

@Command(description = "Execute a Sim file.", usage = "<command> <file>")
public class SimCommand extends AbstractCommand
{
    @Override
    public void run(String[] args) throws FileNotFoundException
    {
        if (args.length == 0)
            throw new CommandFailException("Enter a file to be executed!");
        File file = new File(ArraysX.join(args, " "));
        Sim.execute(file);
        System.out.println();
        System.out.println("Finished execution.");
    }
}
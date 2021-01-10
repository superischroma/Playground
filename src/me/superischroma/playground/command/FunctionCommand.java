package me.superischroma.playground.command;

import me.superischroma.playground.Playground;
import me.superischroma.playground.storage.MappedTextFile;
import me.superischroma.playground.util.ArraysX;

import java.io.File;
import java.io.FileNotFoundException;

@Command(description = "run a function", usage = "<command> <name | file>", aliases = "func")
public class FunctionCommand extends AbstractCommand
{
    @Override
    public void run(String[] args) throws FileNotFoundException
    {
        MappedTextFile functionCache = Playground.getFunctionCache();
        if (args.length == 0)
        {
            if (!functionCache.iterator().hasNext())
                throw new CommandFailException("there are no known functions");
            StringBuilder builder = new StringBuilder()
                    .append("known functions:");
            for (String entry : functionCache)
                builder.append("\n - ").append(entry.split("=")[0]);
            send(builder.toString());
            return;
        }
        String s = ArraysX.join(args, " ");
        File file = new File(s);
        if (!file.exists())
        {
            file = null;
            for (String entry : functionCache)
            {
                String[] ps = entry.split("=");
                if (s.toLowerCase().trim().equals(ps[0]))
                    file = new File(ps[1]);
            }
            if (file == null)
                throw new FileNotFoundException("no file for this directory: " + s);
            if (!file.exists())
                throw new FileNotFoundException("no file for this directory: " + s);
        }
        CommandController.runFunction(file);
        String name = file.getName().toLowerCase().split("\\.")[0];
        if (!functionCache.contains(name))
        {
            functionCache.set(name, file.getAbsolutePath());
            functionCache.save();
        }
    }
}
package me.superischroma.playground.command;

import me.superischroma.playground.ssd.SSD;
import me.superischroma.playground.ssd.SSDCollection;
import me.superischroma.playground.ssd.SSDIO;
import me.superischroma.playground.swing.ApplicationType;
import me.superischroma.playground.util.ArraysX;

import java.io.File;
import java.io.IOException;

@Command(description = "read ssd files", usage = "<command> <file>")
public class SSDCommand extends AbstractCommand
{
    @Override
    public void run(String[] args) throws IOException
    {
        if (args.length == 0)
        {
            ApplicationType.SSD_EDITOR.open();
            return;
        }
        File file = new File(ArraysX.join(args, " "));
        if (!file.exists())
            throw new CommandFailException("no such file exists");
        if (!SSD.isSSDFile(file))
            throw new CommandFailException("that file does not contain ssd!");
        SSDCollection collection = SSDIO.read(file);
        send("~~~ " + file.getName() + " ~~~");
        send(collection.toString());
    }
}
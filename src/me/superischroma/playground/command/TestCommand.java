package me.superischroma.playground.command;

import me.superischroma.playground.ssd.SSDCollection;
import me.superischroma.playground.ssd.SSDIO;

import java.io.File;
import java.io.IOException;

@Command(description = "parse")
public class TestCommand extends AbstractCommand
{
    @Override
    public void run(String[] args) throws IOException
    {
        SSDCollection collection = new SSDCollection("gang");
        collection.setBoolean("bruh", false);
        collection.setString("gaming", "yes");
        File f = new File("./resources/test.txt");
        SSDIO.write(f, collection);
        send(SSDIO.read(f).toString());
    }
}
package me.superischroma.playground.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * A simple and modifiable text file.
 * This class can be iterated over.
 * The iterator for this class contains the contents of its file.
 */
public class SimpleTextFile implements Iterable<String>, Closeable
{
    private final File file;
    private PrintStream out;
    private Scanner in;

    public SimpleTextFile(String path)
    {
        this.file = new File(path);
        try
        {
            if (!file.exists())
            {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            }
            this.out = new PrintStream(new FileOutputStream(file, true));
            this.in = new Scanner(file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return;
        }
    }

    public void print(String s)
    {
        this.out.print(s);
        refreshStreams();
    }

    public void println(String s)
    {
        this.out.println(s);
        refreshStreams();
    }

    public void wipe()
    {
        try
        {
            this.out.flush();
            this.out.close();
            this.in.close();
            this.out = new PrintStream(new FileOutputStream(file));
            this.in = new Scanner(file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public List<String> getLines()
    {
        List<String> strings = new ArrayList<>();
        while (in.hasNextLine())
            strings.add(in.nextLine());
        refreshStreams();
        return strings;
    }

    public String getLine(int index)
    {
        return getLines().get(index);
    }

    public String getLine(CharSequence cs)
    {
        List<String> results = getLines().stream()
                .filter(line -> line.contains(cs)).collect(Collectors.toList());
        return results.size() != 0 ? results.get(0) : null;
    }

    public boolean contains(CharSequence cs)
    {
        for (String line : getLines())
        {
            if (line.contains(cs))
                return true;
        }
        return false;
    }

    public void refreshStreams()
    {
        this.out.flush();
        this.out.close();
        this.in.close();
        try
        {
            this.out = new PrintStream(new FileOutputStream(file, true));
            this.in = new Scanner(file);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public Iterator<String> iterator()
    {
        return getLines().iterator();
    }

    @Override
    public void close()
    {
        this.out.flush();
        this.out.close();
        this.in.close();
    }
}
package me.superischroma.playground.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Command(description = "parse")
public class ParserCommand extends AbstractCommand
{
    @Override
    public void run(String[] args) throws FileNotFoundException
    {
        Scanner in = new Scanner(new File("./resources/in.txt"));
        StringBuilder common = new StringBuilder("Arrays.asList(");
        StringBuilder uncommon = new StringBuilder("Arrays.asList(");
        StringBuilder rare = new StringBuilder("Arrays.asList(");
        StringBuilder epic = new StringBuilder("Arrays.asList(");
        StringBuilder legendary = new StringBuilder("Arrays.asList(");
        boolean n = true;
        while (in.hasNextLine())
        {
            String line = in.nextLine();
            String[] split = line.split("\t");
            if (!n)
            {
                common.append(", ");
                uncommon.append(", ");
                rare.append(", ");
                epic.append(", ");
                legendary.append(", ");
            }
            else
                n = false;
            common.append(split[1].replaceAll(",", ""));
            uncommon.append(split[2].replaceAll(",", ""));
            rare.append(split[3].replaceAll(",", ""));
            epic.append(split[4].replaceAll(",", ""));
            legendary.append(split[5].replaceAll(",", ""));
        }
        in.close();
        common.append(")");
        uncommon.append(")");
        rare.append(")");
        epic.append(")");
        legendary.append(")");
        send("Common: " + common.toString());
        send("Uncommon: " + uncommon.toString());
        send("Rare: " + rare.toString());
        send("Epic: " + epic.toString());
        send("Legendary: " + legendary.toString());
    }
}
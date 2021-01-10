package me.superischroma.playground.command;

import me.superischroma.playground.chemistry.Element;

import java.util.ArrayList;
import java.util.List;

@Command(description = "calculate molar stuff", aliases = "mc, molar, molecalculator, molarcalc, molecalc")
public class MolarCalculatorCommand extends AbstractCommand
{
    @Override
    public void run(String[] args)
    {
        sendnl("conversion type: ");
        ConversionType type = ConversionType.valueOf(scanner.nextLine().toUpperCase());
        sendnl((type == ConversionType.MOLES ? "grams" : "moles") + ": ");
        double a = Double.parseDouble(scanner.nextLine());
        List<MolarMassEntry> entries = new ArrayList<>();
        String line;
        send("enter chemical formula entries below");
        send("format: <symbol | mass>[, <quantity>]");
        send("type \"end\" when you're finished");
        sendnl("> ");
        while (!(line = scanner.nextLine()).toLowerCase().equals("end"))
        {
            entries.add(getEntry(line));
            sendnl("> ");
        }
        double massTotal = 0.0;
        for (MolarMassEntry entry : entries)
            massTotal += entry.getMass() * entry.getAmount();
        send("molar mass: " + massTotal);
        if (type == ConversionType.MOLES)
            send("amount of moles: " + a / massTotal);
        else
            send("amount of grams: " + massTotal * a + "g");
    }

    private static MolarMassEntry getEntry(String input)
    {
        String[] strings = input.split(", ");
        if (strings.length == 0)
            return null;
        return new MolarMassEntry()
        {
            @Override
            public double getMass()
            {
                try
                {
                    return Double.parseDouble(strings[0]);
                }
                catch (NumberFormatException ex)
                {
                    Element element = Element.getBySymbol(strings[0]);
                    if (element == null)
                        throw new CommandFailException("no element found");
                    return element.getAtomicMass();
                }
            }

            @Override
            public int getAmount()
            {
                if (strings.length == 1)
                    return 1;
                return Integer.parseInt(strings[1]);
            }
        };
    }

    private interface MolarMassEntry
    {
        double getMass();
        int getAmount();
    }

    private enum ConversionType
    {
        MOLES,
        GRAMS
    }
}
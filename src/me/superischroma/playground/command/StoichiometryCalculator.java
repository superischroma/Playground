package me.superischroma.playground.command;

import me.superischroma.playground.chemistry.ChemUtils;
import me.superischroma.playground.chemistry.Compound;
import me.superischroma.playground.chemistry.Element;
import me.superischroma.playground.chemistry.ElementQuantifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Command(description = "easy stoich lol", aliases = "sc")
public class StoichiometryCalculator extends AbstractCommand
{
    @Override
    public void run(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Compound[] compounds = new Compound[3];
        String[] types = new String[]{"insertion", "current result", "current insertion"};
        for (int i = 0; i < compounds.length; i++)
        {
            String type = types[i];
            sendnl(type + " // quantity: ");
            double quantity = Double.parseDouble(scanner.nextLine());
            sendnl(type + " // moles or grams? ");
            String t = scanner.nextLine();
            int mode = Compound.GRAMS;
            if (t.equalsIgnoreCase("moles"))
                mode = Compound.MOLES;
            send("enter chemical formula entries below");
            send("format: <symbol | mass>[, <quantity>]");
            send("type \"end\" when you're finished");
            sendnl("> ");
            List<ElementQuantifiable> elements = new ArrayList<>();
            String line;
            while (!(line = scanner.nextLine()).toLowerCase().equals("end"))
            {
                String[] div = line.split(", ");
                if (div.length == 0)
                    continue;
                Element element = Element.resolve(div[0]);
                if (element == null)
                {
                    send("invalid element");
                    continue;
                }
                int amount = 1;
                if (div.length > 1)
                    amount = Integer.parseInt(div[1]);
                elements.add(new ElementQuantifiable(element, amount));
                sendnl("> ");
            }
            compounds[i] = new Compound(quantity, mode, elements);
        }
        Compound result = ChemUtils.stoi(compounds[0], compounds[1], compounds[2]);
        send("stoich result: " + result.toString());
    }
}

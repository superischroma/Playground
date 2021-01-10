package me.superischroma.playground.command;

import me.superischroma.playground.storage.SimpleTextFile;
import me.superischroma.playground.util.ArraysX;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Command(description = "generate an enumeration for chemistry.Element based on the format provided", usage = "<command> <format>", aliases = "genel, genelements")
public class GenerateElementEnumCommand extends AbstractCommand
{
    @Override
    public void run(String[] args) throws ParseException, FileNotFoundException
    {
        if (args.length == 0)
            throw new CommandFailException("please enter a format");
        String format = ArraysX.join(args, " ");
        SimpleTextFile textFile = new SimpleTextFile("./output/element_enum.txt");
        textFile.wipe();
        Scanner in = new Scanner(new File("./resources/periodic_table.json"));
        StringBuilder builder = new StringBuilder();
        while (in.hasNextLine())
            builder.append(in.nextLine()).append("\n");
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(builder.toString());
        for (Object o : (JSONArray) object.get("elements"))
        {
            JSONObject element = (JSONObject) o;
            textFile.println(format
                    .replaceAll("%uppercase_name%", String.valueOf(element.get("name")).toUpperCase())
                    .replaceAll("%name%", String.valueOf(element.get("name")))
                    .replaceAll("%appearance%", String.valueOf(element.get("appearance")))
                    .replaceAll("%atomic_mass%", String.valueOf(element.get("atomic_mass")))
                    .replaceAll("%boil%", String.valueOf(element.get("boil")))
                    .replaceAll("%category%", String.valueOf(element.get("category")))
                    .replaceAll("%color%", String.valueOf(element.get("color")))
                    .replaceAll("%density%", String.valueOf(element.get("density")))
                    .replaceAll("%discovered_by%", String.valueOf(element.get("discovered_by")))
                    .replaceAll("%melt%", String.valueOf(element.get("melt")))
                    .replaceAll("%molar_heat%", String.valueOf(element.get("molar_heat")))
                    .replaceAll("%named_by%", String.valueOf(element.get("named_by")))
                    .replaceAll("%number%", String.valueOf(element.get("number")))
                    .replaceAll("%period%", String.valueOf(element.get("period")))
                    .replaceAll("%phase%", String.valueOf(element.get("phase")))
                    .replaceAll("%symbol%", String.valueOf(element.get("symbol")))
            );
        }
        send("an element enumeration with the format" +
                " provided has been exported to \"output/element_enum.txt\"");
    }
}
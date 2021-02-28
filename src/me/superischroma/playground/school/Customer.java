package me.superischroma.playground.school;

import me.superischroma.playground.ssd.SSD;
import me.superischroma.playground.ssd.SSDCollection;
import me.superischroma.playground.ssd.SSDIO;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Customer
{
    private static final String[] NAMES = {"John", "Joe", "Rob", "Darrell", "Bob", "Steve", "Jack", "Jared"};
    private static final String[] ENTREES = {"Filet Mignon", "New York Strip", "Pulled Pork", "Sirloin",
        "Hamburger", "Cheeseburger", "Chicken Wings", "Noodles"};
    private static final String[] DRINKS = {"Coca-Cola", "Sprite", "Water", "Dr. Pepper", "Pepsi", "Fanta",
        "Sunkist"};

    private String name;
    private String entree;
    private String drink;

    public Customer()
    {
        this.name = NAMES[ThreadLocalRandom.current().nextInt(0, NAMES.length)];
        this.entree = ENTREES[ThreadLocalRandom.current().nextInt(0, ENTREES.length)];
        this.drink = DRINKS[ThreadLocalRandom.current().nextInt(0, DRINKS.length)];
    }

    private Customer(String name, String entree, String drink)
    {
        this.name = name;
        this.entree = entree;
        this.drink = drink;
    }

    public void save()
    {
        File file = new File("./customers/Customer_" + name + ".ssf");
        try
        {

            file.createNewFile();
            SSDCollection collection = new SSDCollection("Data");
            collection.setString("Name", name);
            collection.setString("Entree", entree);
            collection.setString("Drink", drink);
            SSDIO.write(file, collection);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public String getName()
    {
        return name;
    }

    public String getEntree()
    {
        return entree;
    }

    public String getDrink()
    {
        return drink;
    }

    @Override
    public String toString()
    {
        return name + " (" + entree + ", " + drink + ")";
    }

    public static Customer of(File file)
    {
        if (!SSD.isSSDFile(file))
            return null;
        try
        {
            SSDCollection collection = SSDIO.read(file);
            return new Customer(collection.getString("Name"), collection.getString("Entree"), collection.getString("Drink"));
        }
        catch (IOException ex)
        {
            return null;
        }
    }
}
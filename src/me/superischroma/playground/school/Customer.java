package me.superischroma.playground.school;

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
}
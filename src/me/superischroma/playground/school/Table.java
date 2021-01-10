package me.superischroma.playground.school;

import me.superischroma.playground.command.Messenger;

import java.util.concurrent.ThreadLocalRandom;

public class Table
{
    private Customer[] customers;
    private int max;

    public Table()
    {
        this.customers = new Customer[0];
        this.max = ThreadLocalRandom.current().nextInt(2, 11);
    }

    public void clear()
    {
        this.customers = new Customer[0];
    }

    public void seat(int partySize)
    {
        if (customers.length != 0)
            throw new TableSeatingException("Table is already occupied!");
        if (partySize > max)
            throw new TableSeatingException("This party is too large for this table!");
        Customer[] customers = new Customer[partySize];
        for (int i = 0; i < customers.length; i++)
            customers[i] = new Customer();
        this.customers = customers;
        Messenger.send("Seated!");
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder()
            .append("capacity: ").append(max);
        if (customers.length != 0)
            builder.append("\nparty size: ").append(customers.length);
        for (Customer customer : customers)
        {
            builder.append("\n").append(customer.getName()).append(" is eating ")
                    .append(customer.getEntree()).append(" and drinking a(n) ")
                    .append(customer.getDrink());
        }
        return builder.toString();
    }

    public static class TableSeatingException extends RuntimeException
    {
        public TableSeatingException(String message)
        {
            super(message);
        }
    }
}
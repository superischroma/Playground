package me.superischroma.playground.school;

public class Vehicle
{
    protected String name;
    protected int passengers;

    public Vehicle(String name, int passengers)
    {
        this.name = name;
        this.passengers = passengers;
    }

    @Override
    public String toString()
    {
        return "Vehicle{name=" + name + ", passengers=" + passengers + "}";
    }
}
package me.superischroma.playground.school;

public class Car extends Vehicle
{
    private double odometer;

    public Car(String name, int passengers, double odometer)
    {
        super(name, passengers);
        this.odometer = odometer;
    }

    public Car(double odometer)
    {
        this("Sedan", 5, odometer);
    }

    @Override
    public String toString()
    {
        return "Car{name=" + name + ", passengers=" + passengers + ", odometer=" + odometer + "}";
    }
}
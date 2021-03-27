package me.superischroma.playground.school.zoo;

public class Animal
{
    private final String name;
    private final String sound;

    public Animal(String name, String sound)
    {
        this.name = name;
        this.sound = sound;
    }

    @Override
    public String toString()
    {
        return "The " + name + " says " + sound;
    }
}
package me.superischroma.playground.school.zoo;

public class Bird extends Animal
{
    private boolean canFly;

    public Bird()
    {
        super("bird", "chirpchirp");
    }

    public Bird(String name, String sound, boolean canFly)
    {
        super(name, sound);
        this.canFly = canFly;
    }

    @Override
    public String toString()
    {
        return super.toString() + " and " + (canFly ? "can" : "cannot") + " fly";
    }
}
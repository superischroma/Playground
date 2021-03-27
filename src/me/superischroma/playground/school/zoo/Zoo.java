package me.superischroma.playground.school.zoo;

import java.util.concurrent.ThreadLocalRandom;

public class Zoo
{
    public static void main(String[] args)
    {
        for (int i = 0; i < 10; i++)
            System.out.println(randomAnimal().toString());
    }

    private static Animal randomAnimal()
    {
        int random = ThreadLocalRandom.current().nextInt(0, 5);
        switch (random)
        {
            case 0: return new Bird();
            case 1: return new Dog();
            case 2: return new Parrot();
            case 3: return new Snake();
            case 4: return new Turkey();
        }
        return null;
    }
}
package me.superischroma.playground.school.bts;

public class SchoolPerson
{
    private final String name;
    private final int age;
    private final String gender;

    public SchoolPerson(String name, int age, String gender)
    {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString()
    {
        return name + ", age: " + age + ", gender: " + gender;
    }
}
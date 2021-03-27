package me.superischroma.playground.school.bts;

public class Student extends SchoolPerson
{
    private final String id;
    private final double gpa;

    public Student(String name, int age, String gender, String id, double gpa)
    {
        super(name, age, gender);
        this.id = id;
        this.gpa = gpa;
    }

    @Override
    public String toString()
    {
        return super.toString() + ", ID: " + id + ", GPA: " + gpa;
    }
}
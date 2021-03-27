package me.superischroma.playground.school.bts;

public class Teacher extends SchoolPerson
{
    private final double salary;
    private final String subject;

    public Teacher(String name, int age, String gender, double salary, String subject)
    {
        super(name, age, gender);
        this.salary = salary;
        this.subject = subject;
    }

    @Override
    public String toString()
    {
        return super.toString() + ", salary: $" + salary + ", subject(s) taught: " + subject;
    }
}
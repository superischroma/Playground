package me.superischroma.playground.school.bts;

public class CollegeStudent extends Student
{
    private final int year;
    private final String major;

    public CollegeStudent(String name, int age, String gender, String id, double gpa, int year, String major)
    {
        super(name, age, gender, id, gpa);
        this.year = year;
        this.major = major;
    }

    @Override
    public String toString()
    {
        String y;
        switch (year)
        {
            case 1: y = "FR"; break;
            case 2: y = "SO"; break;
            case 3: y = "JU"; break;
            case 4: y = "SE"; break;
            default: y = "UN"; break;
        }
        return super.toString() + ", Year: " + y + ", Major: " + major;
    }
}
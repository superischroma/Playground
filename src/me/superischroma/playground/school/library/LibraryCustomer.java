package me.superischroma.playground.school.library;

import java.util.ArrayList;
import java.util.List;

public class LibraryCustomer{
    private String name;
    private List<Book> books;

    public LibraryCustomer(String name){
        this.name = name;
        this.books = new ArrayList<>();
    }

    public LibraryCustomer(){
        this("Jane Doe");
    }

    public int getBookCount(){
        return books.size();
    }

    public String getName(){
        return name;
    }

    public boolean borrow(Book request){
        if (books.size() >= 50)
            return false;
        if (books.contains(request))
            return false;
        books.add(request);
        return true;
    }

    public boolean dropOff(Book dropped){
        for (int i = books.size() - 1; i >= 0; i--)
        {
            Book book = books.get(i);
            if (book.equals(dropped))
            {
                books.remove(i);
                return true;
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder("Name: " + name + "\n");
        if (!books.isEmpty())
            builder.append("Books: ");
        else
            builder.append("No books!");
        for (int i = 0; i < books.size(); i++)
            builder.append(i != 0 ? ", " : "").append(books.get(i).toString());
        return builder.toString();
    }
}
package me.superischroma.playground.school.library;

import java.util.Scanner;

public class MyLibrary{
    private static Scanner input = new Scanner(System.in);
    private static LibraryCustomer customer;

    /**
     * Displays main menu choices and returns the menu choice
     */
    private static int getMenuChoice(){
        boolean valid = false;
        int choice = 0;
        while (!valid){
            System.out.println("Welcome, " + customer.getName());
            System.out.println("Enter 1 to view your current books");
            System.out.println("Enter 2 to borrow a new book");
            System.out.println("Enter 3 to return a book");
            System.out.println("Enter 4 to leave the library");
            try{     //error check for integer input
                choice = input.nextInt();
                valid = true;
            }
            catch(Exception e){
                System.out.println("Error - invalid input");
                System.out.println();
                input.nextLine();
            }
        }
        input.nextLine();
        return choice;
    }

    /**
     * Shows the customer's book checkout status.
     */
    private static void viewBooks(){
        System.out.println(customer);
    }

    /**
     * Prompts user for the title and author of a book.
     * Returns a new Book object based on entered title and author.
     */
    private static Book promptForBook(){
        System.out.println("Enter the title of your book:");
        String title = input.nextLine();
        System.out.println("Enter the author of your book:");
        String author = input.nextLine();
        return new Book(title, author);
    }

    /**
     * If customer has fewer than 3 items checked out,
     * prompts user for title and author of book to be borrowed. If book was previously
     * checked out, a second copy may not be checked out.
     */
    private static void borrowBook(){
        if(customer.getBookCount() == 3){
            System.out.println("Error: too many books checked out.");
        }
        else{
            Book book = promptForBook();
            boolean success = customer.borrow(book);
            if(success){
                System.out.println(book + " successfully checked out.");
            }
            else{
                System.out.println("Error: you already have a copy of " + book + " checked out.");
            }
        }
    }

    /**
     * Prompts user for title and author of book to be returned. Only works if
     * user currently has a copy of the enterd book, and if at least one book
     * is currently checked out.
     */
    private static void returnBook(){
        if (customer.getBookCount() == 0){
            System.out.println("Error: No books currently checked out");
        }
        else{
            Book book = promptForBook();
            boolean success = customer.dropOff(book);
            if(success){
                System.out.println(book + " successfully returned.");
            }
            else{
                System.out.println("Error: you don't have a copy of " + book);
            }
        }
    }

    public static void main(String[] args){
        int choice = 0;
        System.out.println("What is your name?");
        String name = input.nextLine();
        customer = new LibraryCustomer(name);

        while (choice != 4){  //menu runs continuously until user chooses to exit
            choice = getMenuChoice();
            switch(choice){
                case 1: viewBooks();
                    break;
                case 2: borrowBook();
                    break;
                case 3: returnBook();
                    break;
                case 4:
                    break;
                default: System.out.println("Not a valid menu option");
            }
            System.out.println();
        }

        System.out.println("Thank you for using the library.");
        System.out.println("Have a great day!");
    }
}
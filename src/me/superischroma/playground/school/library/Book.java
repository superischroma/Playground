package me.superischroma.playground.school.library;

public class Book{
    private String title;
    private String author;

    public Book(){
        title = "The Hungry, Hungry, Caterpillar";
        author = "Eric Carle";
    }

    public Book(String title, String author) {
        this.author = author;
        this.title = title;
    }

  /*
  	public Book(String t, String a){
    	title = t;
      author = a;
    }
    */

    public boolean equals(Object someBook){
        //downcast someBook to a Book data type
        Book secondBook = (Book) (someBook);
        //example call: if (firstBook.equals(secondBook))
        //this > firstBook
        if (this.title.equals(secondBook.title) && this.author.equals(secondBook.author)){
            return true;
        }
        return false;
    }

    public String toString(){
        return "\"" + title + "\" by " + author;
    }

}
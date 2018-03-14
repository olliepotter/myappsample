package library;
import java.time.Year;
import java.util.ArrayList;

public class Book {
    private int idNumber;
    private String bookTitle;
    private ArrayList<String> bookAuthors;
    private Year publishYear;
    private int numberCopies;

    public Book(){
        this.idNumber = 0;
        this.bookTitle = "";
        this.bookAuthors = new ArrayList<>();
        this.publishYear = Year.parse("0000");
        this.numberCopies = 0;
    }

    public Book(int idNumber, String bookTitle, ArrayList<String> bookAuthors, Year publishYear, int numberCopies){
        this.idNumber = idNumber;
        this.bookTitle = bookTitle;
        this.bookAuthors = bookAuthors;
        this.publishYear = publishYear;
        this.numberCopies = numberCopies;
    }

    public void printInfo(){
        System.out.println("Book ID: " + this.getIdNumber());
        System.out.println("Book Title: " + this.getBookTitle());
        for (int i = 0; i < this.getBookAuthors().size(); i++){
            System.out.println("Book Author(s): " + this.getBookAuthors().get(i));
        }
        System.out.println("Year Published: " + this.getPublishYear());
        System.out.println("Total Copies: " + this.getNumberCopies());
    }

    // Getters
    public int getIdNumber(){ return idNumber; }

    public String getBookTitle() { return bookTitle; }

    public ArrayList<String> getBookAuthors() { return bookAuthors; }

    public Year getPublishYear() { return publishYear; }

    public int getNumberCopies() { return numberCopies; }


    // Setters
    public void setIdNumber(int idNumber) { this.idNumber = idNumber; }

    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public void setBookAuthors(ArrayList<String> bookAuthors) { this.bookAuthors = bookAuthors; }

    public void setPublishYear(Year publishYear) { this.publishYear = publishYear; }

    public void setNumberCopies(int numberCopies) { this.numberCopies = numberCopies; }
}
package library;
import java.time.Year;
import java.util.ArrayList;
/**
 * Defines Book objects and provides methods
 * to carry out operations on them
 *
 * @author 091388, 094954
 * @version 1.0
 */
public class Book {
    // Attributes of a Book object
    private int idNumber;
    private String bookTitle;
    private ArrayList<String> bookAuthors;
    private Year publishYear;
    private int numberCopies;

    /**
     * Constructs a Book object with default values when no parameters are given
     */
    public Book(){
        this.idNumber = 0;
        this.bookTitle = "";
        this.bookAuthors = new ArrayList<>();
        this.publishYear = Year.parse("0000");
        this.numberCopies = 0;
    }

    /**
     * Constructs a Book object based on the values given via parameters
     *
     * @param idNumber ID number of the book to be created
     * @param bookTitle Title of the book to be created
     * @param bookAuthors Author/s of the book to be created, given as an ArrayList
     * @param publishYear Year the book to be created was published
     * @param numberCopies Number of copies there are of the book that is to be created
     */
    public Book(int idNumber, String bookTitle, ArrayList<String> bookAuthors, Year publishYear, int numberCopies){
        // Set the new Book object's attributes to those that are given via parameters
        this.idNumber = idNumber;
        this.bookTitle = bookTitle;
        this.bookAuthors = bookAuthors;
        this.publishYear = publishYear;
        this.numberCopies = numberCopies;
    }

    /**
     * Prints out a Book objects information via its attributes
     */
    public void printInfo(){
        System.out.println("Book ID: " + this.getIdNumber());
        System.out.println("Book Title: " + this.getBookTitle());

        // Print all authors should there be more than one in a user friendly format
        for (int i = 0; i < this.getBookAuthors().size(); i++){
            System.out.println("Book Author(s): " + this.getBookAuthors().get(i));
        }

        System.out.println("Year Published: " + this.getPublishYear());
        System.out.println("Total Copies: " + this.getNumberCopies() + "\n");
    }

    // Getters

    /**
     * @return ID number of a Book object
     */
    public int getIdNumber(){ return idNumber; }

    /**
     * @return Title of a Book object
     */
    public String getBookTitle() { return bookTitle; }

    /**
     * @return Author/s of a Book object as an ArrayList
     */
    public ArrayList<String> getBookAuthors() { return bookAuthors; }

    /**
     * @return Publish year of a Book object
     */
    public Year getPublishYear() { return publishYear; }

    /**
     * @return Number of copies of a Book object
     */
    public int getNumberCopies() { return numberCopies; }


    // Setters

    /**
     * @param idNumber ID number to set for a Book object
     */
    public void setIdNumber(int idNumber) { this.idNumber = idNumber; }

    /**
     * @param bookTitle Title to set for a Book object
     */
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    /**
     * @param bookAuthors Author/s as an ArrayList to set for a Book object
     */
    public void setBookAuthors(ArrayList<String> bookAuthors) { this.bookAuthors = bookAuthors; }

    /**
     * @param publishYear Year to set the Publish Year to for a Book object
     */
    public void setPublishYear(Year publishYear) { this.publishYear = publishYear; }

    /**
     * @param numberCopies Number of copies to set for a Book object
     */
    public void setNumberCopies(int numberCopies) { this.numberCopies = numberCopies; }
}
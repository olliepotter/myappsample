package library;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Year;
/**
 * Provides a platform for a library to read from text files
 *
 * @author 091388, 094954
 * @version 1.0
 */
public class LibFileReader{

    /**
     * Reads all lines in a file and appends each line to an ArrayList
     *
     * @param path Path of file to be read
     * @return ArrayList containing all lines from file
     */
    public ArrayList<String> ReadFile(String path){

        ArrayList<String> allLinesInFile = new ArrayList<>();

        try {

            Scanner scanner = new Scanner(new File(path));

            // Add all lines
            while (scanner.hasNextLine()) {
                allLinesInFile.add(scanner.nextLine());
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("* File could not be found");
            e.printStackTrace();
        }

        return allLinesInFile;
    }

    /**
     * Creates Book objects from a comma separated .txt file containing book data
     *
     * @param path Path of file to be read
     * @return ArrayList of books created
     */
    public ArrayList<Book> ReadBooks(String path){

        // Get book data from file
        ArrayList<String> bookData = ReadFile(path);

        ArrayList<Book> allBooks = new ArrayList<>();

        for (String item : bookData){

            String[] stringArray = item.split(",");

            // Build book
            Book newBook = new Book();
            newBook.setIdNumber(Integer.parseInt(stringArray[0]));
            newBook.setBookTitle(stringArray[1]);
            newBook.setPublishYear(Year.parse(stringArray[3]));
            newBook.setNumberCopies(Integer.parseInt(stringArray[4]));

            // If more than one author
            if (stringArray[2].contains(":")){

                String[] authorArray = stringArray[2].split(":");
                ArrayList<String> splitAuthors= new ArrayList<>();

                splitAuthors.addAll(Arrays.asList(authorArray));

                newBook.setBookAuthors(splitAuthors);
            }

            // If one author
            else {
                ArrayList<String> author =  new ArrayList<>();
                author.add(stringArray[2]);
                newBook.setBookAuthors(author);
            }

            allBooks.add(newBook);
        }

        return allBooks;
    }

    /**
     * Creates Member objects from a comma separated .txt file containing member data
     *
     * @param path Path of file to be read
     * @return ArrayList of members created
     */
    public ArrayList<Member> ReadMembers(String path){

        // Get member data from file
        ArrayList<String> memberData = ReadFile(path);

        ArrayList<Member> allMembers = new ArrayList<>();

        for (String item : memberData) {

            String[] stringArray = item.split(",");

            // Build Member
            Member newMember = new Member();
            newMember.setIdNumber(Integer.parseInt(stringArray[0]));
            newMember.setFirstName(stringArray[1]);
            newMember.setLastName(stringArray[2]);
            newMember.setDateJoined(LocalDate.parse(stringArray[3]));

            allMembers.add(newMember);
        }

        return allMembers;
    }

    /**
     * Creates BookLoan objects from a comma separated .txt file containing book loan data
     *
     * @param path Path of file to be read
     * @return ArrayList of book loans created
     */
    public ArrayList<BookLoan> ReadBookLoans(String path){

        // Get book loan data from file
        ArrayList<String> bookLoanData = ReadFile(path);

        ArrayList<BookLoan> allBookLoans = new ArrayList<>();

        for (String item: bookLoanData){
            String[] stringArray = item.split(",");

            // Build loan
            BookLoan loan = new BookLoan();
            loan.setLoanId(Integer.parseInt(stringArray[0]));
            loan.setBookId(Integer.parseInt(stringArray[1]));
            loan.setMemberId(Integer.parseInt(stringArray[2]));
            loan.setBorrowDate(LocalDate.parse(stringArray[3]));

            allBookLoans.add(loan);

        }

        return allBookLoans;
    }

}
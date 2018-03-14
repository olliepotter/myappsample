package library;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Year;

public class LibFileReader{

    public ArrayList<String> ReadFile(String path){

        ArrayList<String> allLinesInFile = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(path));

            while (scanner.hasNextLine()) {
                allLinesInFile.add(scanner.nextLine());
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return allLinesInFile;

    }


    public ArrayList<Member> ReadMembers(String path){

        ArrayList<String> memberData = ReadFile(path);
        ArrayList<Member> allMembers = new ArrayList<>();

        for (String item : memberData) {

            String[] stringArray = item.split(",");

            Member newMember = new Member();

            newMember.setIdNumber(Integer.parseInt(stringArray[0]));
            newMember.setFirstName(stringArray[1]);
            newMember.setLastName(stringArray[2]);
            newMember.setDateJoined(LocalDate.parse(stringArray[3]));

            allMembers.add(newMember);
        }

        return allMembers;
    }


    public ArrayList<Book> ReadBooks(String path){

        ArrayList<String> bookData = ReadFile(path);
        ArrayList<Book> allBooks = new ArrayList<>();

        for (String item : bookData){

            String[] stringArray = item.split(",");

            Book newBook = new Book();

            newBook.setIdNumber(Integer.parseInt(stringArray[0]));
            newBook.setBookTitle(stringArray[1]);
            newBook.setPublishYear(Year.parse(stringArray[3]));
            newBook.setNumberCopies(Integer.parseInt(stringArray[4]));



            if (stringArray[2].contains(":")){

                String[] authorArray = stringArray[2].split(":");
                ArrayList<String> splitAuthors= new ArrayList<>();

//                for (String author : authorArray) {
//                    splitAuthors.add(author);
//                }

                splitAuthors.addAll(Arrays.asList(authorArray));

                newBook.setBookAuthors(splitAuthors);

            } else {
                ArrayList<String> author =  new ArrayList<>();
                author.add(stringArray[2]);
                newBook.setBookAuthors(author);
            }

            allBooks.add(newBook);
        }

        return allBooks;
    }


    public ArrayList<BookLoan> ReadBookLoans(String path){

        ArrayList<String> bookLoanData = ReadFile(path);
        ArrayList<BookLoan> allBookLoans = new ArrayList<>();

        for (String item: bookLoanData){
            String[] stringArray = item.split(",");

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
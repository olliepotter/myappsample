package library;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;
/**
 * Library provides an implementation of a library management system.
 *
 * @author 091388, 094954
 * @version 1.0
 */
public class Library {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private ArrayList<BookLoan> bookLoans;

    /**
     * Constructs object Library
     *
     * @param bookData Data file, type .txt, containing all books to be added to Library
     * @param memberData Data file, type .txt, containing all members to be added to Library
     * @param loanData Data file, type .txt, containing all loans to be added to Library
     */
    public Library(String bookData, String memberData, String loanData){

        // Create Reader
        LibFileReader reader = new LibFileReader();

        // Read and create Book objects
        this.books = reader.ReadBooks(bookData);

        // Read and create Member objects
        this.members = reader.ReadMembers(memberData);

        // Read and create bookLoan objects
        this.bookLoans = reader.ReadBookLoans(loanData);

    }

    /**
     * Prints all information about all books in the library
     */
    public void showAllBooks(){
        for (Book book : this.books){
            book.printInfo();
        }
    }

    /**
     * Prints all information about a member in the library
     */
    public void showAllMembers(){
        for (Member member : this.members){
            member.printInfo();
        }
    }

    /**
     * Prints all information for all book loans in the library
     */
    public void showAllBookLoans(){
        for (BookLoan loan : this.bookLoans){
            System.out.println("Book Loan ID: " + loan.getLoanId());
            System.out.println("Book ID: " + loan.getBookId());
            System.out.println("Member ID: " + loan.getMemberId());
            System.out.println("Borrow Date: " + loan.getBorrowDate() + "\n");
        }
    }

    /**
     * Takes keyboard input to search for a book in the library,
     * uses overloading, passing input as parameters
     */
    public void searchBook(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("* Title of book you'd like to search for: ");
        String searchString = scanner.nextLine();

        searchBook(searchString);
    }

    /**
     * Searches the library for a book with title searchString
     *
     * @param searchString Title of book being searched for
     */
    public void searchBook(String searchString){

        boolean found = false;

        for (Book book : this.books){
            if(book.getBookTitle().toLowerCase().contains(searchString.toLowerCase())){
                book.printInfo();
                System.out.println("Remaining Copies: " + getRemainingCopies(book) + "\n");
                found = true;
            }
        }

        if(!found){
            System.out.println("*No books found matching the title: " + searchString);
        }
    }

    /**
     * Takes keyboard input to search for a member in the library,
     * uses overloading, passing input as parameters
     */
    public void searchMember(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("* Please enter member name: ");
        String searchString = scanner.nextLine();

        if (searchString.contains(" ")){
            String names[] = searchString.split(" ");
            this.searchMember(names[0], names[1]);
        } else {
            System.out.println("* Please enter a first and last name separated by a ' '");
        }
    }

    /**
     * Searches the library members for a specified member, printing
     * all information about them and any loan details they currently have.
     *
     * @param firstName First name of the member
     * @param lastName Last name of the member
     */
    public void searchMember(String firstName, String lastName){

        boolean found = false;

        String fullName = firstName + " " + lastName;

        for (Member member : this.members){

            // If a member matches search
            if(member.getFullName().toLowerCase().equals(fullName.toLowerCase())){

                // Print basic member information
                member.printInfo();

                // Retrieve active loans
                ArrayList<BookLoan> currentLoans = this.getCurrentLoans(member);

                double totalFine = 0;

                //Print any loan information
                System.out.println("Number of current rentals: " + currentLoans.size());
                for (BookLoan loan: currentLoans){

                    // Book information
                    System.out.printf("%s was rented on %s \n", findBook(loan.getBookId()).getBookTitle(), loan.getBorrowDate());
                    System.out.printf("Due to be returned on %s \n", loan.getDueDate());

                    // Difference between today and loan date
                    long difference = ChronoUnit.DAYS.between(loan.getBorrowDate(), LocalDate.now());

                    // Any fines
                    if(difference <= 30){
                        System.out.printf("Days since borrow date: %d days \n", difference);
                        System.out.printf("Due to be returned in %d days \n \n", (30 - difference));
                    } else {
                        System.out.printf("This book is overdue by %d days \n", (difference - 30));
                        System.out.printf("There is a fine of %s%.2f \n \n", "£",((difference - 30) * 0.1));
                        totalFine += (difference - 30) * 0.1;
                    }
                }

                // Print fines if exist
                if (totalFine == 0){
                    System.out.println("You have no outstanding fines \n");
                } else {
                    System.out.printf("Total fine for %s is %s%.2f \n \n", member.getFullName(), "£", totalFine);
                }

                System.out.println("**********");

                found = true;
            }
        }

        if(!found){
            System.out.println("*No members found with that name");
        }
    }

    /**
     * Takes keyboard input to create a book loan,
     * uses overloading, passing input as parameters
     */
    public void borrowBook(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("* Please enter the title of the book you would like to rent: ");
        String bookTitle = scanner.nextLine();

        System.out.println("* First name of member to rent book: ");
        String firstName = scanner.nextLine();

        System.out.println("* Last name of member to rent book: ");
        String lastName = scanner.nextLine();

        borrowBook(bookTitle, firstName, lastName);

    }

    /**
     * Allows a member to rent a book by creating a bookLoan object
     *
     * @param bookTitle Title of book in the library to rent
     * @param firstName First name of member renting the book
     * @param lastName Last name of member renting the book
     */
    public void borrowBook(String bookTitle, String firstName, String lastName){

        boolean bookExists = false;

        // Find books from search string
        ArrayList<Book> books = this.getBookMatches(bookTitle);
        Book bookToRent = new Book();

        // No books found
        if (books.size() == 0) { System.out.printf("* No books with title: %s \n", bookTitle); }

        // One book found
        else if (books.size() == 1){ bookToRent = books.get(0); bookExists = true; }

        // Multiple possible books found
        else {

            // Display books found
            for (Book book : books){ System.out.println("*" + book.getBookTitle()); }

            // Ask which book
            Scanner scanner = new Scanner(System.in);
            System.out.println("* Please enter the exact title of the book you would like to rent from the above options:");
            String searchTitle = scanner.nextLine();

            // Retrieve exact book
            for (Book book : books){
                if(book.getBookTitle().toLowerCase().equals(searchTitle.toLowerCase())){
                    bookToRent = book;
                    bookExists = true;
                    break;
                }
            }

            // No book found
            if(bookToRent.getIdNumber() == 0){ System.out.println("* Book still not found"); }
        }

        boolean memberExists = false;
        boolean allowToRent = false;
        boolean remainingCopies = false;

        // Find Member
        String fullName = firstName + " " + lastName;
        Member memberRenting = getMember(fullName);

        // Check member exists
        if(memberRenting != null){

            memberExists = true;

            // Check active rentals for member
            if (getRentals(memberRenting.getIdNumber()).size() < 5){ allowToRent = true; }
            else { System.out.println("* Maximum number of active rentals reached"); }

        } else { System.out.println("* No such member exists"); }

        // Check book quantity
        if (getRemainingCopies(bookToRent) > 0){ remainingCopies = true; }
        else { System.out.println("* No remaining copies of this book to rent"); }

        // Create book loan
        if (memberExists && bookExists && allowToRent && remainingCopies){
            BookLoan newRental = new BookLoan(
                    generateLoanId(),
                    bookToRent.getIdNumber(),
                    memberRenting.getIdNumber(),
                    LocalDate.now()
            );

            System.out.println("* Successfully Rented");
            this.bookLoans.add(newRental);
        }
    }

    /**
     * Takes keyboard input to return a book by removing a bookLoan from the library,
     * uses overloading, passing input as parameters
     */
    public void returnBook(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("* Please enter the Book Loan Id to return book:");

        try {
            int input = scanner.nextInt();
            returnBook(input);
        } catch (InputMismatchException e){
            System.out.println("* An ID must be an integer");
        }
    }

    /**
     * Returns a book by removing a bookLoan from the library
     * @param bookLoanId ID of a bookLoan to be returned
     */
    public void returnBook(int bookLoanId){

        BookLoan loan = new BookLoan();
        boolean paid = true;
        boolean loanFound = false;

        // Check if loan exists
        for(BookLoan bookLoan : bookLoans){
            if (bookLoan.getLoanId() == bookLoanId){
                loan = bookLoan;
                loanFound = true;
            }
        }

        // If loan not found
        if(!loanFound){ System.out.println("* Loan with ID: " + bookLoanId + " does not exist"); }

        // If loan found
        else {

            // Manage fine
            if (calculateFine(loan) > 0){

                boolean isYesOrNoInput = false;

                Scanner scanner = new Scanner(System.in);

                // Print fine, ask to pay
                System.out.printf("* You have a fine of : £%.2f \n", calculateFine(loan));
                System.out.println("* Would you like to pay this fine? [y/n]");
                char inCh = scanner.next().charAt(0);

                // Handle payment
                while (!isYesOrNoInput) {
                    if ((inCh == 'y') || (inCh == 'Y')) {
                        paid = true;
                        isYesOrNoInput = true;

                    } else if ((inCh == 'n') || (inCh == 'N')) {
                        paid = false;
                        isYesOrNoInput = true;
                        System.out.println("* Book was not returned");
                    } else {
                        System.out.println("******** Wrong input. Try again.");
                        isYesOrNoInput = false;
                    }
                }
            }

            // If fines settled, remove book loan
            if (paid) {
                this.bookLoans.remove((this.bookLoans.indexOf(loan)));
                System.out.println("* Book successfully returned");
            }
        }
    }

    /**
     * Takes keyboard input to add a new book to the library,
     * uses overloading, passing input as parameters
     */
    public void addNewBook(){

        Scanner scanner = new Scanner(System.in);

        String title;
        String authorString;
        String[] authors;
        int publishYear;
        int copies;

        System.out.println("* Please enter the title of the book: ");
        title = scanner.nextLine();

        System.out.println("* Please enter all authors separated by a ','");
        authorString = scanner.nextLine();
        authors = authorString.split(",");

        for(int i = 0; i < authors.length; i++){
            authors[i] = authors[i].trim();
        }

        try {

            System.out.println("* Please enter the year this book was published: ");
            publishYear = scanner.nextInt();

            System.out.println("* Please enter the number of copies of this book to add to the library: ");
            copies = scanner.nextInt();

            addNewBook(title, authors, publishYear, copies);

        } catch (InputMismatchException e){
            System.out.println("* Year and number of copies must be integers");
            System.out.println("* Book not added to the library");
        }
    }

    /**
     * Adds a new book to the library
     *
     * @param title Title of book to add to library
     * @param authors Authors of book being added
     * @param publishYear Publish year of book being added
     * @param copies Number of copies of the book
     */
    public void addNewBook(String title, String[] authors, int publishYear, int copies){

        // Check valid input
        boolean validInput = true;

        // Check title
        if (title.trim().equals("")){
            System.out.println("* A book must have a title");
            validInput = false;
        }

        // Check authors
        if(authors.length == 0){
            System.out.println("* A book must have an author");
            validInput = false;
        } else {
            for (String author : authors){
                if (author.equals("")){
                    System.out.println("* An invalid author has been entered");
                    validInput = false;
                }
            }
        }

        // Check publish year
        if(LocalDate.now().getYear() < publishYear){
            System.out.println("* A book cannot have been published in the future");
            validInput = false;
        }

        // Check at least one copy
        if (copies < 1){
            System.out.println("* Must have at least one copy of the book");
            validInput = false;
        }

        if (validInput) {

            boolean titleClash = false;
            boolean createBook = true;
            ArrayList<String> authorList = new ArrayList<>(Arrays.asList(authors));

            // Check for title clashes
            for (Book book : this.books) {
                if (book.getBookTitle().toLowerCase().equals(title.toLowerCase())) {
                    titleClash = true;
                }
            }

            if (titleClash) {

                boolean isYesOrNoInput = false;
                Scanner scanner = new Scanner(System.in);

                // Give option
                System.out.println("* There is already a book with this title, would you like to continue anyway? [y/n]");
                char inCh = scanner.next().charAt(0);

                // Choice to add book despite title clash
                while (!isYesOrNoInput) {
                    if ((inCh == 'y') || (inCh == 'Y')) {
                        isYesOrNoInput = true;
                    } else if ((inCh == 'n') || (inCh == 'N')) {
                        createBook = false;
                        isYesOrNoInput = true;
                        System.out.println("* Book was not created");
                    } else {
                        System.out.println("******** Wrong input. Try again.");
                        isYesOrNoInput = false;
                    }
                }

            }

            // Add new book to library
            if (createBook) {
                Book newBook = new Book(generateBookId(), title, authorList, Year.parse(Integer.toString(publishYear)), copies);
                this.books.add(newBook);
                System.out.println("Book created.");
                newBook.printInfo();
            }
        }
    }

    /**
     * Takes keyboard input to add a new member to the library,
     * uses overloading, passing input as parameters
     */
    public void addNewMember(){

        Scanner scanner = new Scanner(System.in);

        // Input first name
        System.out.println("* Please enter the first name of the member to add: ");
        String firstName = scanner.nextLine();

        // Input last name
        System.out.println("* Last name of the member to add: ");
        String lastName = scanner.nextLine();

        // Pass input
        addNewMember(firstName, lastName, LocalDate.now());

    }

    /**
     * Adds a new member to the library
     *
     * @param firstName First name of the member
     * @param lastName Last name of the member
     * @param dateJoined Date the member was created
     */
    public void addNewMember(String firstName, String lastName, LocalDate dateJoined){

        boolean validInput = false;

        // Checks name only contains letters and not empty
        if(firstName.matches("[a-zA-Z]+") && lastName.matches("[a-zA-Z]+")){
            validInput = true;
        } else {
            System.out.println("* Invalid first or last name, name can only contain letters");
        }

        if (validInput){

            // Create member
            Member newMember = new Member(generateMemberId(), firstName, lastName, dateJoined);

            // Add member to library
            this.members.add(newMember);
            System.out.println("Member Added");

        }

    }

    /**
     * Takes keyboard input to change the quantity of a book in the library,
     * uses overloading, passing input as parameters
     */
    public void changeQuantity(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the title of the book you would like to change the quantity of: ");
        String title = scanner.nextLine();

        System.out.println("Please enter the amount you would like to increase the quantity by (use negative numbers to decrease it): ");
        int amount = scanner.nextInt();

        changeQuantity(title, amount);
    }

    /**
     * Changes the number of copies of a book in the library
     *
     * @param title Title of the book to have quantity changed
     * @param amount Quantity to change the number of copies to
     */
    public void changeQuantity(String title, int amount) {

        // Find possible matches
        ArrayList<Book> matches = this.getBookMatches(title);

        Book bookToChange = new Book();

        // No matches
        if (matches.size() == 0) { System.out.println("* No books match that title."); }

        // One match
        else if (matches.size() == 1) { bookToChange = this.books.get(0); }

        // Multiple matches
        else {

            // Display books found
            for (Book book : this.books) {
                System.out.println("*" + book.getBookTitle());
            }

            // Ask which book
            Scanner scanner = new Scanner(System.in);
            System.out.println("* Please enter the exact title of the book you would like to rent from the above options:");
            String searchTitle = scanner.nextLine();

            // Retrieve exact book
            for (Book book : this.books) {
                if (book.getBookTitle().toLowerCase().equals(searchTitle.toLowerCase())) {
                    bookToChange = book;
                    break;
                }
            }
        }

        if (getRemainingCopies(bookToChange) < amount){
            System.out.println("* The amount to decrease by is more than are available");
        } else {
            bookToChange.setNumberCopies(bookToChange.getNumberCopies() + amount);
            System.out.println("* Quantity Changed");
        }
    }

    /**
     * Save any changes made during execution to the library data files
     *
     * @param bookFile Data file, type .txt, containing all books in Library
     * @param memberFile Data file, type .txt, containing all members in Library
     * @param loanFile Data file, type .txt, containing all loans in Library
     */
    public void saveChanges(String bookFile, String memberFile, String loanFile) {

        // Create array of files
        String[] files = new String[]{bookFile, memberFile, loanFile};

        for (String file : files){
            try {

                FileWriter writer = new FileWriter(file);

                switch (file){
                    case "data/books.txt":
                        writeBooks(writer);
                        break;
                    case "data/members.txt":
                        writeMembers(writer);
                        break;
                    case "data/bookloans.txt":
                        writeLoans(writer);
                        break;
                }

                writer.close();

            } catch (IOException e){
                System.out.println("IOException");
            }
        }
    }

    /**
     * Retrieves BookLoan objects that are related to a passed Member object
     *
     * @param member Member object to find related BookLoans objects of
     * @return An ArrayList of the related BookLoans
     */
    private ArrayList<BookLoan> getCurrentLoans(Member member){

        ArrayList<BookLoan> memberLoans = new ArrayList<>();

        for(BookLoan bookLoan : this.bookLoans){
            if (member.getIdNumber() == bookLoan.getMemberId()){
                memberLoans.add(bookLoan);
            }
        }

        return memberLoans;
    }

    /**
     * Finds a Book object in the library based on its ID
     *
     * @param searchId ID of the Book object
     * @return Book object if found, otherwise null
     */
    private Book findBook(int searchId){

        for(Book book : this.books){
            if(book.getIdNumber() == searchId){
                return book;
            }
        }

        return null;
    }

    /**
     * Find any books that partially match a given string
     *
     * @param searchString String to check for in the titles of all books
     * @return ArrayList of all Book objects that partially match the given string
     */
    private ArrayList<Book> getBookMatches(String searchString) {

        ArrayList<Book> matches = new ArrayList<>();

        // Loop through all Book objects and add them to the ArrayList if their title contains the searchString
        for (Book book: this.books){
            if(book.getBookTitle().toLowerCase().contains(searchString.toLowerCase())){
                matches.add(book);
            }
        }

        return matches;
    }

    /**
     * Find a Member object given a full name
     *
     * @param fullName Full name of member to find
     * @return Member object that is desired if found, otherwise null
     */
    private Member getMember(String fullName){

        // Loop through all Member objects and return it if they are found
        for(Member member : this.members){
            if (member.getFullName().toLowerCase().equals(fullName.toLowerCase())){
                return member;
            }
        }

        // Return null if the Member object can't be found
        return null;
    }

    /**
     * Retrieve all BookLoan objects that a member currently has
     *
     * @param memberID ID of the Member object to get the loans of
     * @return ArrayList of all BookLoan objects a given Member object has
     */
    private ArrayList<BookLoan> getRentals(int memberID){

        ArrayList<BookLoan> currentLoans = new ArrayList<>();

        // Loop through all BookLoan objects and add any loans of the given member to the ArrayList
        for (BookLoan bookLoan : this.bookLoans){
            if (bookLoan.getMemberId() == memberID){
                currentLoans.add(bookLoan);
            }
        }

        return currentLoans;
    }

    /**
     * Calculate how many copies of a book there are remaining considering active loans
     *
     * @param book Book object to find amount of remaining copies of
     * @return Number of remaining copies of the Book object
     */
    private int getRemainingCopies(Book book){

        int totalCopies = book.getNumberCopies();
        int numberRented = 0;

        // Loop through all BookLoan objects increment numberRented when the loan is of the Book object in question
        for (BookLoan bookLoan : this.bookLoans){
            if (bookLoan.getBookId() == book.getIdNumber()){
                numberRented ++;
            }
        }

        // Calculate remaining copies and return
        return totalCopies - numberRented;
    }

    /**
     * Generates a book Id by finding current highest Id and incrementing by 1
     *
     * @return Book Id
     */
    private int generateBookId(){

        int highest = 100000;

        for (Book book : this.books){
            if (book.getIdNumber() > highest){
                highest = book.getIdNumber();
            }
        }

        return highest + 1;
    }

    /**
     * Generates a member Id by finding current highest Id and incrementing by 1
     *
     * @return member Id
     */
    private int generateMemberId(){

        int highest = 200000;

        for (Member member : this.members){
            if (member.getIdNumber() > highest){
                highest = member.getIdNumber();
            }
        }

        return highest + 1;
    }

    /**
     * Generates a loan Id by finding current highest Id and incrementing by 1
     *
     * @return Loan Id
     */
    private int generateLoanId(){

        int highest = 300000;

        for (BookLoan bookLoan : this.bookLoans){
            if (bookLoan.getLoanId() > highest){
                highest = bookLoan.getLoanId();
            }
        }

        return highest + 1;
    }

    /**
     * Calculates late fine for a book loan
     *
     * @param loan BookLoan object the fine is being calculated for
     * @return fine value
     */
    private double calculateFine(BookLoan loan){

        double fine = 0;

        // Find days since loan
        long days = ChronoUnit.DAYS.between(loan.getBorrowDate(), LocalDate.now());

        if(days > 30){
            fine += (days - 30) * 0.1;
        }

        return fine;
    }

    /**
     * Writes all information about all books in the library to a file
     *
     * @param writer FileWriter object
     * @throws IOException
     */
    private void writeBooks(FileWriter writer) throws IOException{

        // Loop through all books in library
        for (Book book : this.books){

            String writeStr;

            // More than one author
            if(book.getBookAuthors().size() > 1){

                StringBuilder builder = new StringBuilder();

                for (String author : book.getBookAuthors()){
                    builder.append(author);
                    builder.append(":");
                }

                builder.setLength(builder.length() - 1); // -1 to remove final ':'

                String authorString = builder.toString();

                writeStr = book.getIdNumber() + "," + book.getBookTitle() + "," + authorString + "," + book.getPublishYear() + "," + book.getNumberCopies() + "\n";

            // One author
            } else {

                writeStr = book.getIdNumber() + "," + book.getBookTitle() + "," + book.getBookAuthors().get(0) + "," + book.getPublishYear() + "," + book.getNumberCopies() + "\n";

            }

            writer.write(writeStr);
        }
    }

    /**
     * Writes all information about all members in the library to a file
     *
     * @param writer FileWriter object
     * @throws IOException
     */
    private void writeMembers(FileWriter writer) throws IOException{

        // Loop through all members in library
        for (Member member : this.members){

            String writeStr = member.getIdNumber() + "," + member.getFirstName() + "," + member.getLastName() + "," + member.getDateJoined() + "\n";

            writer.write(writeStr);
        }
    }

    /**
     * Writes all information about all loans in the library to a file
     *
     * @param writer FileWriter object
     * @throws IOException
     */
    private void writeLoans(FileWriter writer) throws IOException{

        for(BookLoan loan : this.bookLoans){

            String writeStr = loan.getLoanId() + "," + loan.getBookId() + "," + loan.getMemberId() + "," + loan.getBorrowDate() + "\n";

            writer.write(writeStr);
        }
    }

}
package library;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class Library {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private ArrayList<BookLoan> bookLoans;


    protected Library(String bookData, String memberData, String loanData){

        // Create Reader
        LibFileReader reader = new LibFileReader();

        // READ BOOKS
        this.books = reader.ReadBooks(bookData);

        // Read and create Member objects
        this.members = reader.ReadMembers(memberData);

        // Read and create Member objects
        this.bookLoans = reader.ReadBookLoans(loanData);

    }

    public void showAllBooks(){

        for (Book book : this.books){
            System.out.println(book.getIdNumber());
            System.out.println(book.getBookTitle());
            for (int i = 0; i < book.getBookAuthors().size(); i++){
                System.out.println(book.getBookAuthors().get(i));
            }
            System.out.println(book.getPublishYear());
            System.out.println(book.getNumberCopies() + "\n");
        }
    }

    public void showAllMembers(){
        for (Member member : this.members){
            member.printInfo();
        }
    }

    public void showAllBookLoans(){
        for (BookLoan loan : this.bookLoans){
            System.out.println("Book Loan ID: " + loan.getLoanId());
            System.out.println("Book ID: " + loan.getBookId());
            System.out.println("Member ID: " + loan.getMemberId());
            System.out.println("Borrow Date: " + loan.getBorrowDate() + "\n");
        }
    }

    public void searchBook(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("*Title of book you'd like to search for: ");
        String searchString = scanner.nextLine();

        // COME BACK TO THIS ## OUTPUT NUMBER OF BOOKS AVAILABLE
        this.searchBook(searchString);

    }

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

    public void searchMember(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("*Please enter member name: ");
        String searchString = scanner.nextLine();

        String names[] = searchString.split(" ");

        this.searchMember(names[0], names[1]);
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

            if(member.getFullName().toLowerCase().equals(fullName.toLowerCase())){

                // PRINT BASIC MEMBER INFORMATION
                member.printInfo();

                // RETRIEVE ACTIVE LOANS
                ArrayList<BookLoan> currentLoans = this.getCurrentLoans(member);

                double totalFine = 0;

                //PRINT LOAN INFORMATION
                System.out.println("Number of current rentals: " + currentLoans.size());
                for (BookLoan loan: currentLoans){
                    System.out.printf("%s was rented on %s \n", findBook(loan.getBookId()).getBookTitle(), loan.getBorrowDate());
                    System.out.printf("Due to be returned on %s \n", loan.getDueDate());
                    long difference = ChronoUnit.DAYS.between(loan.getBorrowDate(), LocalDate.now());
                    if(difference <= 30){
                        System.out.printf("Days since borrow date: %d days \n", difference);
                        System.out.printf("Due to be returned in %d days \n \n", (30 - difference));
                    } else {
                        System.out.printf("This book is overdue by %d days \n", (difference - 30));
                        System.out.printf("There is a fine of %s%.2f \n \n", "£",((difference - 30) * 0.1));
                        totalFine += (difference - 30) * 0.1;
                    }
                }

                // PRINT FINES IF EXIST
                if (totalFine == 0){
                    System.out.println("You have no outstanding fines");
                } else {
                    System.out.printf("Total fine for " + member.getFullName() + " is %s%.2f", "£", totalFine);
                }

                found = true;
            }

        }

        if(!found){
            System.out.println("*No members found with that name");
        }
    }

    public void borrowBook(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the title of the book you would like to rent: ");
        String bookTitle = scanner.nextLine();

        System.out.println("First name of member to rent book: ");
        String firstName = scanner.nextLine();

        System.out.println("Last name of member to rent book: ");
        String lastName = scanner.nextLine();

        borrowBook(bookTitle, firstName, lastName);
    }

    public void borrowBook(String bookTitle, String firstName, String lastName){

        boolean bookExists = false;

        // FIND BOOKS FROM SEARCH STRING
        ArrayList<Book> books = this.getBookMatches(bookTitle);
        Book bookToRent = new Book();

        // NO BOOKS FOUND
        if (books.size() == 0)
        {
            System.out.printf("No books with title: %s", bookTitle);
        }

        // ONE BOOK FOUND
        else if (books.size() == 1){
            bookToRent = books.get(0);
            bookExists = true;
        }

        // MULTIPLE POSSIBLE BOOKS
        else {

            // Display books found
            for (Book book : books){
                System.out.println("*" + book.getBookTitle());
            }

            // Ask which book
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the exact title of the book you would like to rent from the above options:");
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
            if(bookToRent.getIdNumber() == 0){
                System.out.println("Book still not found");
            }
        }

        boolean memberExists = false;
        boolean allowToRent = false;

        // FIND MEMBER
        String fullName = firstName + " " + lastName;
        Member memberRenting = getMember(fullName);

        // CHECK MEMBER EXISTS
        if(memberRenting != null){

            memberExists = true;

            // CHECK ACTIVE RENTALS
            if (getRentals(memberRenting.getIdNumber()).size() < 5){
                allowToRent = true;
            } else {
                System.out.println("Maximum number of active rentals reached");
            }

        } else {
            System.out.println("No such member exists");
        }





        boolean remaingCopies = false;

        // CHECK BOOK QUANTITY
        if (getRemainingCopies(bookToRent) > 0){
            remaingCopies = true;
        } else {
            System.out.println("No remaining copies of this book to rent");
        }

        if (memberExists && bookExists && allowToRent && remaingCopies){
            BookLoan newRental = new BookLoan(
                    findHighestLoanID(),
                    bookToRent.getIdNumber(),
                    memberRenting.getIdNumber(),
                    LocalDate.now()
            );

            System.out.println("Successfully Rented");
            this.bookLoans.add(newRental);
        }
    }

    public void returnBook(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the Book Loan Id to return book:");
        int input = scanner.nextInt();

        returnBook(input);
    }

    public void returnBook(int bookLoanId){

        BookLoan loan = new BookLoan();
        boolean paid = true;
        boolean bookFound = false;

        // CHECK IF LOAN EXISTS
        for(BookLoan bookLoan : bookLoans){
            if (bookLoan.getLoanId() == bookLoanId){
                loan = bookLoan;
                bookFound = true;
            }
        }

        // IF BOOK NOT FOUND
        if(!bookFound){
            System.out.println("Loan with ID: " + bookLoanId + " does not exist");


        // IF BOOK IS FOUND
        } else {

            // Manage fine
            if (calculateFine(loan) > 0){
                boolean isYesOrNoInput = false;

                Scanner scanner = new Scanner(System.in);

                System.out.printf("You have a fine of : £%.2f \n", calculateFine(loan));
                System.out.println("Would you like to pay this fine? [y/n]");
                char inCh = scanner.next().charAt(0);


                while (!isYesOrNoInput) {
                    if ((inCh == 'y') || (inCh == 'Y')) {
                        paid = true;
                        isYesOrNoInput = true;

                    } else if ((inCh == 'n') || (inCh == 'N')) {
                        paid = false;
                        isYesOrNoInput = true;
                        System.out.println("Book was not returned");
                    } else {
                        System.out.println("******** Wrong input. Try again.");
                        isYesOrNoInput = false;
                    }
                }
            }

            // IF FINES SETTLED, REMOVE BOOK LOAN
            if (paid) {
                this.bookLoans.remove((this.bookLoans.indexOf(loan)));
                System.out.println("Book successfully returned");
            }
        }
    }

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

        System.out.println("* Please enter the year this book was published: ");
        publishYear = scanner.nextInt();

        System.out.println("* Please enter the number of copies of this book to add to the library: ");
        copies = scanner.nextInt();

        addNewBook(title, authors, publishYear, copies);
    }

    public void addNewBook(String title, String[] authors, int publishYear, int copies){

        // CHECK VALID INPUT
        boolean validInput = true;

        // Check title
        if (title.trim().equals("")){
            System.out.println("A book must have a title");
            validInput = false;
        }

        // Check authors
        if(authors.length == 0){
            System.out.println("A book must have an author");
            validInput = false;
        } else {
            for (String author : authors){
                if (author.equals("")){
                    System.out.println("An invalid author has been entered");
                    validInput = false;
                }
            }
        }

        // Check publish year
        if(LocalDate.now().getYear() < publishYear){
            System.out.println("A book cannot have been published in the future");
            validInput = false;
        }

        // Check at least one copy
        if (copies < 1){
            System.out.println("Must have at least one copy of the book");
            validInput = false;
        }

        if (validInput) {

            int bookId = findHighestBookID();
            boolean titleClash = false;
            boolean createBook = true;
            ArrayList<String> authorList = new ArrayList<String>(Arrays.asList(authors));

            for (Book book : this.books) {
                if (book.getBookTitle().toLowerCase().equals(title.toLowerCase())) {
                    titleClash = true;
                }
            }

            if (titleClash) {

                boolean isYesOrNoInput = false;
                Scanner scanner = new Scanner(System.in);

                System.out.println("There is already a book with this title, would you like to continue anyway? [y/n]");
                char inCh = scanner.next().charAt(0);


                while (!isYesOrNoInput) {
                    if ((inCh == 'y') || (inCh == 'Y')) {
                        isYesOrNoInput = true;
                    } else if ((inCh == 'n') || (inCh == 'N')) {
                        createBook = false;
                        isYesOrNoInput = true;
                        System.out.println("Book was not created");
                    } else {
                        System.out.println("******** Wrong input. Try again.");
                        isYesOrNoInput = false;
                    }
                }
            }

            if (createBook) {
                Book newBook = new Book(bookId, title, authorList, Year.parse(Integer.toString(publishYear)), copies);
                this.books.add(newBook);
                System.out.println("Book created.");
                newBook.printInfo();
            }
        }
    }

    public void addNewMember(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the first name of the member to add: ");
        String firstName = scanner.nextLine();

        System.out.println("Last name of the member to add: ");
        String lastName = scanner.nextLine();

        addNewMember(firstName, lastName, LocalDate.now());
    }

    public void addNewMember(String firstName, String lastName, LocalDate dateJoined){

        boolean validInput = true;

        if (firstName.trim().equals("")){
            System.out.println("A member must have a first name");
            validInput = false;
        }

        if (lastName.trim().equals("")){
            System.out.println("A member must have a last name");
        }


        if (validInput){

            int memberId = findHighestMemberID();

            Member newMember = new Member(memberId, firstName, lastName, dateJoined);
            this.members.add(newMember);
            System.out.println("Member Added");
        }

    }

    public void changeQuantity(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the title of the book you would like to change the quantity of: ");
        String title = scanner.nextLine();

        System.out.println("Please enter the amount you would like to increase the quantity by (use negative numbers to decrease it): ");
        int amount = scanner.nextInt();

        changeQuantity(title, amount);
    }

    public void changeQuantity(String title, int amount) {
        ArrayList<Book> matches = this.getBookMatches(title);
        Book bookToChange = new Book();

        if (matches.size() == 0) {
            System.out.println("No books match that title.");
        } else if (matches.size() == 1) {
            bookToChange = this.books.get(0);
        }

        // MULTIPLE POSSIBLE BOOKS
        else {

            // Display books found
            for (Book book : this.books) {
                System.out.println("*" + book.getBookTitle());
            }

            // Ask which book
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the exact title of the book you would like to rent from the above options:");
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
            System.out.println("The amount to decrease by is more than are available");
        } else {
            bookToChange.setNumberCopies(bookToChange.getNumberCopies() + amount);
            System.out.println("Quantity Changed");
        }
    }

    public void saveChanges(String bookFile, String memberFile, String loanFile) {

        // CREATE ARRAY OF FILES
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
     * Function to find any books that partially match a given string
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
     * Function to find a Member object given a full name
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
     * Function to get all of the BookLoan objects that a member currently has
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
     * Function to calculate how many copies of a book there are remaining taking loans into account
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
     * Function to establish the highest current loanID to find the next ID to be used on a new BookLoan object
     *
     * @return  The current highest loan ID + 1 (next ID to be used)
     */
    private int findHighestLoanID(){

        int highest = 300000;

        // Loop through all BookLoan objects to establish highest current ID
        for (BookLoan bookLoan : this.bookLoans){
            if (bookLoan.getLoanId() > highest){
                highest = bookLoan.getLoanId();
            }
        }

        return highest + 1;
    }

    private int findHighestBookID(){
        int highest = 100000;

        for (Book book : this.books){
            if (book.getIdNumber() > highest){
                highest = book.getIdNumber();
            }
        }

        return highest + 1;
    }

    private int findHighestMemberID(){
        int highest = 200000;

        for (Member member : this.members){
            if (member.getIdNumber() > highest){
                highest = member.getIdNumber();
            }
        }

        return highest + 1;
    }

    private double calculateFine(BookLoan loan){

        double fine = 0;

        long difference = ChronoUnit.DAYS.between(loan.getBorrowDate(), LocalDate.now());

        if(difference > 30){
            fine += (difference - 30) * 0.1;
        }

        return fine;
    }

    private void writeBooks(FileWriter writer) throws IOException{

        // Loop through all books in library
        for (Book book : this.books){

            String writeStr;

            if(book.getBookAuthors().size() > 1){

                StringBuilder builder = new StringBuilder();

                for (String author : book.getBookAuthors()){
                    builder.append(author);
                    builder.append(":");
                }

                builder.setLength(builder.length() - 1);

                String authorString = builder.toString();

                writeStr = book.getIdNumber() + "," + book.getBookTitle() + "," + authorString + "," + book.getPublishYear() + "," + book.getNumberCopies() + "\n";
            } else {

                writeStr = book.getIdNumber() + "," + book.getBookTitle() + "," + book.getBookAuthors().get(0) + "," + book.getPublishYear() + "," + book.getNumberCopies() + "\n";

            }

            writer.write(writeStr);
        }
    }

    private void writeMembers(FileWriter writer) throws IOException{

        // Loop through all books in library
        for (Member member : this.members){

            String writeStr = member.getIdNumber() + "," + member.getFirstName() + "," + member.getLastName() + "," + member.getDateJoined() + "\n";

            writer.write(writeStr);

        }
    }

    private void writeLoans(FileWriter writer) throws IOException{

        for(BookLoan loan : this.bookLoans){

            String writeStr = loan.getLoanId() + "," + loan.getBookId() + "," + loan.getMemberId() + "," + loan.getBorrowDate() + "\n";

            writer.write(writeStr);

        }
    }

}
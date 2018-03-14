package library;
import java.time.LocalDate;
/**
 * Defines BookLoan objects and provides methods
 * to carry out operations on them
 *
 * @author 091388, 094954
 * @version 1.0
 */
public class BookLoan {
    // Attributes of a BookLoan object
    private int loanId;
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    /**
     * Constructs a BookLoan object with default values when no parameters are given
     */
    public BookLoan(){
        this.loanId = 0;
        this.bookId = 0;
        this.memberId = 0;
        this.borrowDate = LocalDate.parse("0000-01-01");
        this.dueDate = borrowDate.plusDays(30);
    }

    /**
     * Constructs a BookLoan object based on the values given via parameters
     *
     * @param loanId Loan ID Number of the book loan to be created
     * @param bookId Book ID Number of the book loan to be created (book being loaned)
     * @param memberId  Member ID Number of the book loan to be created (member taking out the loan)
     * @param borrowDate Date on which the book loan began
     */
    public BookLoan(int loanId, int bookId, int memberId, LocalDate borrowDate){
        // Set the new BookLoan object's attributes to those that are given via parameters
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(30);
    }


    // Getters

    /**
     * @return Loan ID Number of a BookLoan object
     */
    public int getLoanId() { return loanId; }

    /**
     * @return Book ID Number from a BookLoan object
     */
    public int getBookId() { return bookId; }

    /**
     * @return Member ID Number from a BookLoan object
     */
    public int getMemberId() { return memberId; }

    /**
     * @return Date on which a Book was loaned
     */
    public LocalDate getBorrowDate() { return borrowDate; }

    /**
     * @return The date on which the Book is due to be returned by
     */
    public LocalDate getDueDate() { return this.borrowDate.plusDays(30); }

    // Setters

    /**
     * @param loanId Loan ID Number to set for a BookLoan object
     */
    public void setLoanId(int loanId) { this.loanId = loanId; }

    /**
     * @param bookId Book ID Number to set for a BookLoan object
     */
    public void setBookId(int bookId) { this.bookId = bookId; }

    /**
     * @param memberId Member ID Number to set for a BookLoan object
     */
    public void setMemberId(int memberId) { this.memberId = memberId; }

    /**
     * @param borrowDate Date to set the date a BookLoan was created to
     */
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    /**
     * @param dueDate Date to set the date a Book is due to be returned to
     */
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
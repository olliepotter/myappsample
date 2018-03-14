package library;
import java.time.LocalDate;

public class BookLoan {
    private int loanId;
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    // Constructors
    public BookLoan(){
        this.loanId = 0;
        this.bookId = 0;
        this.memberId = 0;
        this.borrowDate = LocalDate.parse("0000-01-01");
        this.dueDate = borrowDate.plusDays(30);
    }

    public BookLoan(int loanId, int bookId, int memberId, LocalDate borrowDate){
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(30);
    }


    // Getters
    public int getLoanId() { return loanId; }

    public int getBookId() { return bookId; }

    public int getMemberId() { return memberId; }

    public LocalDate getBorrowDate() { return borrowDate; }

    public LocalDate getDueDate() {
        return this.borrowDate.plusDays(30);
    }

    // Setters
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public void setBookId(int bookId) { this.bookId = bookId; }

    public void setMemberId(int memberId) { this.memberId = memberId; }

    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
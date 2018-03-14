package library;
import java.time.LocalDate;
import java.util.ArrayList;

public class Member {
    private int idNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateJoined;

    public Member(){
        this.idNumber = 0;
        this.firstName = "";
        this.lastName = "";
        this.dateJoined = LocalDate.parse("0000-01-01");
    }

    public Member(int idNumber, String firstName, String lastName, LocalDate dateJoined){
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateJoined = dateJoined;
    }

    public void printInfo(){
        System.out.println(this.idNumber);
        System.out.println(this.firstName + " " + this.lastName);
        System.out.println(this.dateJoined + "\n");
    }

    // GETTERS

    public int getIdNumber() { return idNumber; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return  lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public LocalDate getDateJoined() { return dateJoined; }


    // SETTERS

    public void setIdNumber(int idNumber) { this.idNumber = idNumber; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }

}

package library;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Defines Member objects and provides methods
 * to carry out operations on them
 *
 * @author 091388, 094954
 * @version 1.0
 */
public class Member {
    // Attributes of a Member object
    private int idNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateJoined;

    /**
     * Constructs a Member object with default values when no parameters are given
     */
    public Member(){
        this.idNumber = 0;
        this.firstName = "";
        this.lastName = "";
        this.dateJoined = LocalDate.parse("0000-01-01");
    }

    /**
     * Constructs a Member object based on the values given via parameters
     *
     * @param idNumber ID Number of the member to be created
     * @param firstName First name of the member to be created
     * @param lastName Last name of the member to be created
     * @param dateJoined Date that the new member joined
     */
    public Member(int idNumber, String firstName, String lastName, LocalDate dateJoined){
        // Set the new Member object's attributes to those that are given via parameters
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateJoined = dateJoined;
    }

    /**
     * Prints out a Member objects information via its attributes
     */
    public void printInfo(){
        System.out.println("Member ID: " + this.idNumber);

        // Prints the members full name at once
        System.out.println("Name: " + this.firstName + " " + this.lastName);

        System.out.println("Date Joined: " + this.dateJoined + "\n");
    }

    // Getters

    /**
     * @return ID Number of a Member object
     */
    public int getIdNumber() { return idNumber; }

    /**
     * @return The first name of a Member object
     */
    public String getFirstName() { return firstName; }

    /**
     * @return The last name of a Member object
     */
    public String getLastName() { return  lastName; }

    /**
     * @return The full name of a Member object
     */
    public String getFullName() { return firstName + " " + lastName; }

    /**
     * @return The date that a Member joined
     */
    public LocalDate getDateJoined() { return dateJoined; }


    // Setters

    /**
     * @param idNumber The ID Number to set for a Member object
     */
    public void setIdNumber(int idNumber) { this.idNumber = idNumber; }

    /**
     * @param firstName The first name to set for a Member object
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * @param lastName The last name to set for a Member object
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * @param dateJoined The date to set the Date Joined to for a Member
     */
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }

}

package ca.ubc.cs304.src.model;

import java.time.LocalDate;

public class ClientModel {
    private int clientID;
    private String c_phoneNum;
    private LocalDate birthdate;
    private String firstName;
    private String lastName;

    public ClientModel(int clientID, String phoneNum, LocalDate birthdate, String firstName, String lastName) {
        this.clientID = clientID;
        this.c_phoneNum = phoneNum;
        this.birthdate = birthdate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getID() {
        return clientID;
    }

    public String getNum() {return c_phoneNum;}

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
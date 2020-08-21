package Rever.PillX.User;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class User {

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }
    @Id
    public String fullName;

    public LocalDate dateOfBirth;
    public Gender gender = Gender.UNKNOWN;
    public String phoneNumber;
    public String address;
    public String medicalHistory; //Not sure what type this should be, string for now
    public List<String> allergies = new ArrayList<>(); //Not sure what type this should be, string for now

    public User() {}

    public User(String name) {
        this.fullName = name;
    }
}



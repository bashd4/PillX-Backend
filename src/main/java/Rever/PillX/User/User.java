package Rever.PillX.User;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.UserMedicine;
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

    public String password; //TODO: Implement this securely after core functionality is done
    public LocalDate dateOfBirth;
    public Gender gender = Gender.UNKNOWN;
    public List<String> allergies = new ArrayList<>(); //Not sure what type this should be, string for now
    public List<UserMedicine> medicines = new ArrayList<>();

    public User() {}

    public User (String name, LocalDate dateOfBirth, Gender gender, List<String> allergies,
                 List<UserMedicine> medicines) {
        this.fullName = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.allergies = allergies;
        this.medicines = medicines;
    }

    public void DeleteMedicineByAustR(String austR) {
        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).fullNameAustR.equals(UserMedicine.ConvertFullNameAndAustR(this.fullName, austR))) {
                medicines.remove(i);
                break;
            }
        }
    }
}



package Rever.PillX.User;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.UserMedicine;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class User {

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }
    @Id
    public String email;

    public String fullName;
    public String password; //TODO: Implement this securely after core functionality is done
    public LocalDate dateOfBirth;
    public Gender gender = Gender.UNKNOWN;
    public List<String> allergies = new ArrayList<>(); //Not sure what type this should be, string for now
    public List<UserMedicine> medicines = new ArrayList<>();

    public User() {}

    public User (String email, String name, LocalDate dateOfBirth, Gender gender, List<String> allergies,
                 List<UserMedicine> medicines) {
        this.email = email;
        this.fullName = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.allergies = allergies;
        this.medicines = medicines;
    }

    public void deleteMedicineByAustR(String austR) {
        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).fullNameAustR.equals(UserMedicine.ConvertFullNameAndAustR(this.fullName, austR))) {
                medicines.remove(i);
                break;
            }
        }
    }

    public UserMedicine findMedicineByAustR(String austR) {
        for (UserMedicine medicine : medicines) {
            if (medicine.austR.equals(austR)) {
                return medicine;
            }
        }
        return null;
    }

    public List<UserMedicine> GetMedicineOnDate(LocalDate date) {
        List<UserMedicine> medicinesOnDate = new ArrayList<>();
        for (int i = 0; i < medicines.size(); i++) {
            //TODO: implement this once temporaladjuster is added to DosageTimes
        }
        return medicinesOnDate;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}



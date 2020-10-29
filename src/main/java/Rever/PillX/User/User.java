package Rever.PillX.User;

import Rever.PillX.Medicine.PillReminder;
import Rever.PillX.Medicine.UserMedicine;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public User (String email, String name, String password, LocalDate dateOfBirth, Gender gender, List<String> allergies,
                 List<UserMedicine> medicines) {
        this.email = email;
        this.fullName = name;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.allergies = allergies;
        this.medicines = medicines;
    }

    public void deleteMedicineByidentifier(String identifier) {
        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).identifier != null && medicines.get(i).identifier.equals(identifier)) {
                medicines.remove(i);
                break;
            }
        }
    }

    public UserMedicine findMedicineByidentifier(String identifier) {
        for (UserMedicine medicine : medicines) {
            if (medicine.identifier != null && medicine.identifier.equals(identifier)) {
                return medicine;
            }
        }
        return null;
    }

    public List<MedicineOnDateResponse> GetMedicineOnDate(LocalDate date) {
        List<MedicineOnDateResponse> medicinesOnDate = new ArrayList<>();
        for (UserMedicine medicine : medicines) {
            List<PillReminder> times = new ArrayList<>();
            for (PillReminder pillTime : medicine.dosageSetting.pillDateTime) {
                if (pillTime.time.toLocalDate().isEqual(date)) {
                    times.add(pillTime);
                }
            }
            if (times.size() > 0) {
                medicinesOnDate.add(new MedicineOnDateResponse(medicine, times));
            }
        }
        return medicinesOnDate;
    }

    public List<MedicineOnDateResponse> GetMedicineBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<MedicineOnDateResponse> medicinesOnDate = new ArrayList<>();
        for (UserMedicine medicine : medicines) {
            List<PillReminder> times = new ArrayList<>();
            for (PillReminder pillTime : medicine.dosageSetting.pillDateTime) {
                if ((pillTime.time.toLocalDate().isAfter(startDate) || pillTime.time.toLocalDate().isEqual(startDate)) &&
                        (pillTime.time.toLocalDate().isBefore(endDate) ||
                                pillTime.time.toLocalDate().isEqual(endDate))) {
                    times.add(pillTime);
                }
            }
            if (times.size() > 0) {
                medicinesOnDate.add(new MedicineOnDateResponse(medicine, times));
            }
        }
        return medicinesOnDate;
    }

    public String TakePill(UserMedicine medicine, LocalDateTime taken) {
        for (int i = 0; i < medicine.dosageSetting.pillDateTime.size(); i++) {
            if (medicine.dosageSetting.pillDateTime.get(i).time.equals(taken)) {
                medicine.dosageSetting.pillDateTime.get(i).take();
                return "Success";
            }
        }
        return "Failure";
    }

    public String unTakePill(UserMedicine medicine, LocalDateTime untaken) {
        for (int i = 0; i < medicine.dosageSetting.pillDateTime.size(); i++) {
            if (medicine.dosageSetting.pillDateTime.get(i).time.equals(untaken)) {
                medicine.dosageSetting.pillDateTime.get(i).untake();
                return "Success";
            }
        }
        return "Failure";
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailValid(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}




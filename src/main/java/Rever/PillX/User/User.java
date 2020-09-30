package Rever.PillX.User;

import Rever.PillX.Medicine.Medicine;
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

    public void deleteMedicineByAustR(String austR) {
        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).austR.equals(austR)) {
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

    public Map<UserMedicine, List<LocalDateTime>> GetMedicineOnDate(LocalDate date) {
        Map<UserMedicine, List<LocalDateTime>> medicinesOnDate = new TreeMap<>();
        for (UserMedicine medicine : medicines) {
            int j = 0;
            List<LocalDateTime> times = new ArrayList<>();
            while (j < medicine.dosageSetting.pillDateTimes.size() && medicine.dosageSetting.pillDateTimes.get(j).toLocalDate().equals(date)) {
                times.add(medicine.dosageSetting.pillDateTimes.get(j++));
            }
            medicinesOnDate.put(medicine, times);
        }
        return medicinesOnDate;
    }

    public Map<UserMedicine, List<LocalDateTime>> GetMedicineBetweenDates(LocalDate startDate, LocalDate endDate) {
        Map<UserMedicine, List<LocalDateTime>> medicinesOnDate = new TreeMap<>();
        for (UserMedicine medicine : medicines) {
            int j = 0;
            List<LocalDateTime> times = new ArrayList<>();
            while (j < medicine.dosageSetting.pillDateTimes.size() && ((medicine.dosageSetting.pillDateTimes.get(j).toLocalDate().isAfter(startDate) ||
                    medicine.dosageSetting.pillDateTimes.get(j).toLocalDate().isEqual(startDate)) &&
                    (medicine.dosageSetting.pillDateTimes.get(j).toLocalDate().isBefore(endDate) ||
                            medicine.dosageSetting.pillDateTimes.get(j).toLocalDate().isEqual(endDate)))) {
                times.add(medicine.dosageSetting.pillDateTimes.get(j));
                j++;
            }
            medicinesOnDate.put(medicine, times);
        }
        return medicinesOnDate;
    }

    public String TakePill(UserMedicine medicine, LocalDateTime taken) {
        for (int i = 0; i < medicine.dosageSetting.pillDateTimes.size(); i++) {
            if (medicine.dosageSetting.pillDateTimes.get(i).equals(taken)) {
                medicine.dosageSetting.pillDateTimesDone.add(medicine.dosageSetting.pillDateTimes.get(i));
                medicine.dosageSetting.pillDateTimes.remove(i);
                return "Success";
            }
        }
        return "Failure";
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}



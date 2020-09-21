package Rever.PillX.Medicine;

import Rever.PillX.User.User;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

public class UserMedicine extends AbsMedicine implements Comparable<UserMedicine> {

    //This field is a comma separated fullname and austR number, i.e. Ben Ashdown,03AC3F
    @Id
    public String fullNameAustR;

    public DosageTimes dosageSetting;
    public LocalDate startDate;

    public UserMedicine() {}

    public UserMedicine(String fullName, Medicine medicine) {
        super(medicine);
        this.fullNameAustR = ConvertFullNameAndAustR(fullName, medicine.austR);
    }

    public UserMedicine(String fullName, Medicine medicine, DosageTimes dosageSetting, LocalDate startDate) {
        this(fullName, medicine);
        this.dosageSetting = dosageSetting;
        this.startDate = startDate;
    }

    @Override
    public int compareTo(UserMedicine userMedicine) {
        return userMedicine.hashCode() - this.hashCode();
    }

    public static String ConvertFullNameAndAustR(String fullName, String austR) {
        return fullName + "," + austR;
    }
}

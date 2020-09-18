package Rever.PillX.Medicine;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

public class UserMedicine extends AbsMedicine {

    //This field is a comma separated fullname and austR number, i.e. Ben Ashdown,03AC3F
    @Id
    public String fullNameAustR;

    public int medicationCycle; //this datatype might need to be more customisable, currently represents days between doses
    public DosageTimes dosageSetting;
    public LocalDate startDate;

    public UserMedicine() {}

    public UserMedicine(String fullName, Medicine medicine) {
        super(medicine);
        this.fullNameAustR = ConvertFullNameAndAustR(fullName, medicine.austR);
    }

    public UserMedicine(String fullName, Medicine medicine, int medicationCycle, DosageTimes dosageSetting, LocalDate startDate) {
        this(fullName, medicine);
        this.medicationCycle = medicationCycle;
        this.dosageSetting = dosageSetting;
        this.startDate = startDate;
    }

    public static String ConvertFullNameAndAustR(String fullName, String austR) {
        return fullName + "," + austR;
    }
}

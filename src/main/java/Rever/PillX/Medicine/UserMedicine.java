package Rever.PillX.Medicine;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

public class UserMedicine extends AbsMedicine {

    //This field is a comma separated fullname and austR number, i.e. Ben Ashdown,03AC3F
    @Id
    public String fullNameAustR;

    public int medicationCycle; //this datatype might need to be more customisable, currently represents days between doses
    public Dosage dosageSetting;
    public List<LocalDate> recommendedDates; //Not sure if this should be in the Medicine class (is it unique per user?)
    public LocalDate startDate;
    public List<LocalDate> consumptionDates;

    public UserMedicine() {}

    public UserMedicine(String fullName, Medicine medicine) {
        super(medicine);
        this.fullNameAustR = ConvertFullNameAndAustR(fullName, medicine.austR);
    }

    public UserMedicine(String fullName, Medicine medicine, int medicationCycle, Dosage dosageSetting, List<LocalDate> recommendedDates,
                        LocalDate startDate, List<LocalDate> consumptionDates) {
        this(fullName, medicine);
        this.medicationCycle = medicationCycle;
        this.dosageSetting = dosageSetting;
        this.recommendedDates = recommendedDates;
        this.startDate = startDate;
        this.consumptionDates = consumptionDates;
    }

    public static String ConvertFullNameAndAustR(String fullName, String austR) {
        return fullName + "," + austR;
    }
}

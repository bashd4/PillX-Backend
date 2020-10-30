package Rever.PillX.Medicine;

import java.time.LocalDate;

/*
 * Represents a medicine that has been assigned to a user
 */
public class UserMedicine extends AbsMedicine  {


    public DosageTimes dosageSetting;
    public LocalDate startDate;
    public String customName;

    public UserMedicine() {}

    public UserMedicine(Medicine medicine) {
        super(medicine);
    }

    public UserMedicine(Medicine medicine, DosageTimes dosageSetting, LocalDate startDate) {
        this(medicine);
        this.dosageSetting = dosageSetting;
        this.startDate = startDate;
    }
}

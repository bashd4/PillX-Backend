package Rever.PillX.Medicine;

import Rever.PillX.User.User;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class UserMedicine extends AbsMedicine implements Comparable<UserMedicine>  {


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

    @Override
    public int compareTo(UserMedicine userMedicine) {
        return userMedicine.hashCode() - this.hashCode();
    }
}

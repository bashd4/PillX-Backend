package Rever.PillX.User;

import Rever.PillX.Medicine.PillReminder;
import Rever.PillX.Medicine.UserMedicine;

import java.util.List;

/*
 * HTTP Response object including medicine and times
 */
public class MedicineOnDateResponse {
    public UserMedicine medicine;
    public List<PillReminder> times;

    public MedicineOnDateResponse() {}

    public MedicineOnDateResponse(UserMedicine medicine, List<PillReminder> times) {
        this.medicine = medicine;
        this.times = times;
    }

    public UserMedicine GetMedicine() {
        return medicine;
    }

    public List<PillReminder> GetTimes() {
        return times;
    }
}

package Rever.PillX.Medicine;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

public class Medicine {

    //Add new administration methods here
    public enum AdministrationMethods {
        PILL, INTRAVENOUS
    }

    @Id
    public String austR;

    public String name;
    public String description;
    public String dosageDescription;
    public AdministrationMethods routeOfAdministration;
    public String sideEffects;

    public Dosage recommendedDosage;
    public List<String> ingredients;
    public List<String> drugInteractions;

    //We can probably group all these attributes into its own class
    public int medicationCycle; //this datatype might need to be more customisable, currently represents days between doses
    public Dosage dosageSetting;
    public List<LocalDate> recommendedDates;
    public LocalDate startDate;
    public List<LocalDate> consumptionDates;

    public Medicine() {}

    public Medicine(String austR) {
        this.austR = austR;
    }
}

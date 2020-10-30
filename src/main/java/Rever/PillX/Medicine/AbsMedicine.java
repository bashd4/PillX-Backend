package Rever.PillX.Medicine;

import java.util.List;

/*
 * Represents a generic medicine
 */
public abstract class AbsMedicine {

    public enum ActionSites {
        BRAIN, KNEE, HEART, PANCREAS, AREA_APPLIED
    }

    public enum Administration {
        ORAL, TOPICAL, INJECTION
    }

    public String identifier;
    public String barcode;
    public String name;
    public String description;
    public String dosageDescription;

    public List<Administration> administrationMethod;
    public List<ActionSites> actionSites;

    public DosageTimes recommendedDosage;
    public List<String> ingredients;
    public List<String> drugInteractions;

    public AbsMedicine() {}

    public AbsMedicine(String identifier) {
        this.identifier = identifier;
    }

    public AbsMedicine(Medicine medicine) {
        this.identifier = medicine.identifier;
        this.name = medicine.name;
        this.description = medicine.description;
        this.dosageDescription = medicine.dosageDescription;
        this.administrationMethod = medicine.administrationMethod;
        this.actionSites = medicine.actionSites;
        this.recommendedDosage = medicine.recommendedDosage;
        this.ingredients = medicine.ingredients;
        this.drugInteractions = medicine.drugInteractions;
    }
}

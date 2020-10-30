package Rever.PillX.Medicine;

/*
 * HTTP Response object containing a medicine's identifier and name
 */
public class MedicineReturnInfo {
    public String identifier;
    public String name;

    public MedicineReturnInfo(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }
}
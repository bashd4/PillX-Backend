package Rever.PillX.Medicine;

import org.springframework.data.annotation.Id;

public class Medicine extends AbsMedicine {

    @Id
    public String identifier;

    public Medicine() {}

    public Medicine(String identifier) {
        this.identifier = identifier;
    }

    public Medicine(Medicine medicine) {
        super(medicine);
        this.identifier = medicine.identifier;
    }
}

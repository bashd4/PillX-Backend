package Rever.PillX.Medicine;

import org.springframework.data.annotation.Id;

public class Medicine extends AbsMedicine {

    @Id
    public String austR;

    public Medicine() {}

    public Medicine(String austR) {
        this.austR = austR;
    }

    public Medicine(Medicine medicine) {
        super(medicine);
        this.austR = medicine.austR;
    }
}

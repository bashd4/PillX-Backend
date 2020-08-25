package Rever.PillX.Medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicineController {

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/medicine/add")
    public String addMedicine(@RequestParam String austR) {
        Medicine medicine = new Medicine(austR);

        medicineRepository.save(medicine);
        return String.format("Added medicine %s", austR);
    }

    @RequestMapping(value = "/medicine/get")
    public Medicine getMedicine(@RequestParam String austR) {
        return medicineRepository.findByAustR(austR);
    }
}

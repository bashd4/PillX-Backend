package Rever.PillX.Medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MedicineController {

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/medicine/add")
    public String addMedicine(@RequestParam String austR, @RequestParam(required=false) String name, @RequestParam(required=false) String description,
                              @RequestParam(required=false) String dosageDescription, @RequestParam(required=false)Medicine.AdministrationMethods method,
                              @RequestParam(required=false) String sideEffects, @RequestParam(required=false) Dosage recommendedDosage,
                              @RequestParam(required=false) List<String> ingredients, @RequestParam(required=false) List<String> drugInteractions,
                              @RequestParam(required=false) Integer medicationCycle, @RequestParam(required=false) Dosage dosageSetting,
                              @RequestParam(required=false) List<LocalDate> recommendedDates, @RequestParam(required=false) LocalDate startDate,
                              @RequestParam(required=false) List<LocalDate> consumptionDates) {
        Medicine medicine = new Medicine(austR);
        medicine.name = name;
        medicine.description = description;
        medicine.dosageDescription = dosageDescription;
        medicine.routeOfAdministration = method;
        medicine.sideEffects = sideEffects;
        medicine.recommendedDosage = recommendedDosage;

        medicineRepository.save(medicine);
        return String.format("Added medicine %s", austR);
    }

    @RequestMapping(value = "/medicine/get")
    public Medicine getMedicine(@RequestParam String austR) {
        return medicineRepository.findByAustR(austR);
    }

    @RequestMapping(value = "/medicine/remove")
    public String removeMedicine(@RequestParam String austR) {
        medicineRepository.deleteById(austR);

        return String.format("Deleted medicine %s", austR);
    }

    @RequestMapping(value = "medicine/getAll")
    public List<Medicine> getAllMedicine() {
        return medicineRepository.findAll();
    }
}

package Rever.PillX.Medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MedicineController {

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/medicine/add")
    public String addMedicine(@RequestParam String austR, @RequestParam(required=false) String name, @RequestParam(required=false) String description,
                              @RequestParam(required=false) String dosageDescription, @RequestParam(required=false)Medicine.AdministrationMethods administrationMethod,
                              @RequestParam(required=false) String sideEffects, @RequestParam(required=false) Dosage recommendedDosage,
                              @RequestParam(required=false) List<String> ingredients, @RequestParam(required=false) List<String> drugInteractions) {
        Medicine medicine = new Medicine(austR);
        medicine.name = name;
        medicine.description = description;
        medicine.dosageDescription = dosageDescription;
        medicine.routeOfAdministration = administrationMethod;
        medicine.sideEffects = sideEffects;
        medicine.recommendedDosage = recommendedDosage;
        medicine.ingredients = ingredients;
        medicine.drugInteractions = drugInteractions;

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

    //region Updating medicine fields

    @RequestMapping(value = "medicine/update/name")
    public String updateName(@RequestParam String austR, @RequestParam String name) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.name = name;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/update/description")
    public String updateDescription(@RequestParam String austR, @RequestParam String description) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.description = description;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/update/dosageDescription")
    public String updateDosageDescription(@RequestParam String austR, @RequestParam String dosageDescription) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.dosageDescription = dosageDescription;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/update/administrationMethod")
    public String updateAdministrationMethod(@RequestParam String austR, @RequestParam AbsMedicine.AdministrationMethods administrationMethod) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.routeOfAdministration = administrationMethod;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/update/sideEffects")
    public String updateSideEffects(@RequestParam String austR, @RequestParam String sideEffects) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.sideEffects = sideEffects;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/update/recommendedDosage")
    public String updateRecommendedDosage(@RequestParam String austR, @RequestParam boolean intervalUsage, @RequestParam LocalDate startDate,
                                          @RequestParam LocalDate endDate, @RequestParam LocalDateTime time, @RequestParam Dosage.Intervals intervalType,
                                          @RequestParam int interval, @RequestParam int[] weekdays) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.recommendedDosage = new Dosage(intervalUsage, startDate, endDate, time, intervalType, interval, weekdays);
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/update/ingredients")
    public String updateIngredients(@RequestParam String austR, @RequestParam List<String> ingredients) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.ingredients = ingredients;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/update/drugInteractions")
    public String updateDrugInteractions(@RequestParam String austR, @RequestParam List<String> drugInteractions) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        if (medicine != null) {
            medicine.drugInteractions = drugInteractions;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }
    //endregion
}

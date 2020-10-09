package Rever.PillX.Medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MedicineController {

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/medicine/add")
    public String addMedicine(@RequestParam String identifier, @RequestParam(required=false) String name, @RequestParam(required=false) String description,
                              @RequestParam(required=false) String dosageDescription, @RequestParam(required=false)Medicine.AdministrationMethods administrationMethod,
                              @RequestParam(required=false) String sideEffects, @RequestParam(required=false) DosageTimes recommendedDosage,
                              @RequestParam(required=false) List<String> ingredients, @RequestParam(required=false) List<String> drugInteractions) {
        Medicine medicine = new Medicine(identifier);
        medicine.name = name;
        medicine.description = description;
        medicine.dosageDescription = dosageDescription;
        medicine.routeOfAdministration = administrationMethod;
        medicine.sideEffects = sideEffects;
        medicine.recommendedDosage = recommendedDosage;
        medicine.ingredients = ingredients;
        medicine.drugInteractions = drugInteractions;

        medicineRepository.save(medicine);
        return String.format("Added medicine %s", identifier);
    }

    @RequestMapping(value = "/medicine/get")
    public Medicine getMedicine(@RequestParam String identifier) {
        return medicineRepository.findByidentifier(identifier);
    }

    @RequestMapping(value = "/medicine/remove")
    public String removeMedicine(@RequestParam String identifier) {
        medicineRepository.deleteById(identifier);

        return String.format("Deleted medicine %s", identifier);
    }

    @RequestMapping(value = "medicine/getAll")
    public List<Medicine> getAllMedicine() {
        return medicineRepository.findAll();
    }

    //region Updating medicine fields

    @RequestMapping(value = "medicine/name/update")
    public String updateName(@RequestParam String identifier, @RequestParam String name) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.name = name;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/description/update")
    public String updateDescription(@RequestParam String identifier, @RequestParam String description) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.description = description;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/dosageDescription/update")
    public String updateDosageDescription(@RequestParam String identifier, @RequestParam String dosageDescription) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.dosageDescription = dosageDescription;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/administrationMethod/update")
    public String updateAdministrationMethod(@RequestParam String identifier, @RequestParam AbsMedicine.AdministrationMethods administrationMethod) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.routeOfAdministration = administrationMethod;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/sideEffects/update")
    public String updateSideEffects(@RequestParam String identifier, @RequestParam String sideEffects) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.sideEffects = sideEffects;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/recommendedDosage/update")
    public String updateRecommendedDosage(@RequestParam String identifier, @RequestParam boolean intervalUsage,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time, @RequestParam DosageTimes.Intervals intervalType,
                                          @RequestParam int interval, @RequestParam boolean[] weekdays) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.recommendedDosage = new DosageTimes(intervalUsage, startDate, endDate, time, intervalType, interval, weekdays);
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/ingredients/update")
    public String updateIngredients(@RequestParam String identifier, @RequestParam List<String> ingredients) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.ingredients = ingredients;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "medicine/drugInteractions/update")
    public String updateDrugInteractions(@RequestParam String identifier, @RequestParam List<String> drugInteractions) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        if (medicine != null) {
            medicine.drugInteractions = drugInteractions;
            medicineRepository.save(medicine);
            return "Success";
        }
        return "Failure";
    }
    //endregion
}

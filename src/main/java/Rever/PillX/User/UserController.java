package Rever.PillX.User;

import Rever.PillX.Medicine.Dosage;
import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import Rever.PillX.Medicine.UserMedicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/user/add")
    public String addUser(@RequestParam String email, @RequestParam(required=false) String fullName,
                          @RequestParam(required=false) LocalDate dateOfBirth, @RequestParam(required=false) User.Gender gender,
                          @RequestParam(required=false) List<String> allergies, @RequestParam(required=false) List<UserMedicine> medicines) {

        if (email == null || !User.validateEmail(email)) {
            return String.format("Invalid email address %s", email);
        }

        User newUser = new User(email, fullName, dateOfBirth, (gender != null) ? gender : User.Gender.UNKNOWN,
                (allergies != null) ? allergies : new ArrayList<>(), (medicines != null) ? medicines : new ArrayList<>());

        userRepository.save(newUser);
        return String.format("Saved new user %s", email);
    }

    @RequestMapping(value = "/user/get")
    public User getUser(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            //Error, email does not exist, not sure what to return to request
            System.out.println("User does not exist");
        }
        return user;
    }

    @RequestMapping(value = "user/delete")
    public void deleteUser(@RequestParam String email) {
        userRepository.deleteById(email);
    }

    @RequestMapping(value = "user/medicine/delete")
    public String deleteMedicineFromUser(@RequestParam String email, @RequestParam String austR) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.deleteMedicineByAustR(austR);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/medicine/add") //NOTE: Requires austR to be an existing medicine in the "Medicine" database
    public String addMedicineToUser(@RequestParam String email, @RequestParam String austR) {
        Medicine medicine = medicineRepository.findByAustR(austR);
        User user = userRepository.findByEmail(email);
        if (medicine != null && user != null) {
            UserMedicine userMedicine = new UserMedicine(user.fullName, medicine);
            user.medicines.add(userMedicine);
        } else {
            return "Failure, medicine or user did not exist";
        }
        return "Success";
    }

    @RequestMapping(value = "user/medicine/getAll")
    public List<UserMedicine> getAllUserMedicine(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.medicines;
        }
        return null;
    }

    @RequestMapping(value = "/user/medicine/dosage/add")
    public String addDosage(@RequestParam String email, @RequestParam String austR, @RequestParam int dosageAmount, @RequestParam int dailyDosageAmount,
                            @RequestParam int frequency) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            UserMedicine userMedicine = user.findMedicineByAustR(austR);
            if (userMedicine != null) {
                userMedicine.recommendedDosage = new Dosage(dosageAmount, dailyDosageAmount, frequency);
                return "Success";
            }
        }
        return "Failure";
    }
}

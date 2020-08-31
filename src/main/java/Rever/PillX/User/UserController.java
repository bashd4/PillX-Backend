package Rever.PillX.User;

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

        User newUser = new User(email, fullName, dateOfBirth, (gender != null) ? gender : User.Gender.UNKNOWN,
                (allergies != null) ? allergies : new ArrayList<>(), (medicines != null) ? medicines : new ArrayList<>());

        userRepository.save(newUser);
        return String.format("Saved new user %s", fullName);
    }

    @RequestMapping(value = "/user/get")
    public User getUser(@RequestParam String email) {
        return userRepository.findByEmail(email);
    }

    @RequestMapping(value = "user/medicine/delete")
    public String deleteMedicineFromUser(@RequestParam String email, @RequestParam String austR) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.DeleteMedicineByAustR(austR);
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
}

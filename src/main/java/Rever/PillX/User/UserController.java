package Rever.PillX.User;

import Rever.PillX.Medicine.DosageTimes;
import Rever.PillX.Medicine.DosageTimes;
import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import Rever.PillX.Medicine.UserMedicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/user/add")
    public String addUser(@RequestParam String email, @RequestParam(required=false) String fullName,  @RequestParam String password,
                          @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth, @RequestParam(required=false) User.Gender gender,
                          @RequestParam(required=false) List<String> allergies, @RequestParam(required=false) List<UserMedicine> medicines) {

        if (email == null || !User.validateEmail(email)) {
            return String.format("Invalid email address %s", email);
        }
        User newUser = new User(email, fullName, password, dateOfBirth, (gender != null) ? gender : User.Gender.UNKNOWN,
                (allergies != null) ? allergies : new ArrayList<>(), (medicines != null) ? medicines : new ArrayList<>());

        userRepository.save(newUser);
        return String.format("Saved new user %s", email);
    }

    @RequestMapping(value = "/user/add/json")
    public String addUser(@RequestBody User user) {

        if (user.email == null || !User.validateEmail(user.email)) {
            return String.format("Invalid email address %s", user.email);
        }

        userRepository.save(user);
        return String.format("Saved new user %s", user.email);
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

    //region Update User fields
    @RequestMapping(value = "user/fullName/update")
    public String updateName(@RequestParam String email, @RequestParam String fullName) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.fullName = fullName;
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/password/update")
    public String updatePassword(@RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.password = password;
            userRepository.save(user);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/dateOfBirth/update")
    public String updateDateOfBirth(@RequestParam String email, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.dateOfBirth = dateOfBirth;
            userRepository.save(user);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/gender/update")
    public String updateGender(@RequestParam String email, @RequestParam User.Gender gender) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.gender = gender;
            userRepository.save(user);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/allergies/update")
    public String updateAllergies(@RequestParam String email, @RequestParam List<String> allergies) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.allergies = allergies;
            userRepository.save(user);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/allergies/add")
    public String addAllergy(@RequestParam String email, @RequestParam String allergy) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            for (String userAllergy : user.allergies) {
                if (userAllergy.equals(allergy)) {
                    return "Allergy already added";
                }
            }
            user.allergies.add(allergy);
            userRepository.save(user);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/allergies/delete")
    public String deleteAllergy(@RequestParam String email, @RequestParam String allergy) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            for (int i = 0; i < user.allergies.size(); i++) {
                if (user.allergies.get(i).equals(allergy)) {
                    user.allergies.remove(i);
                    break;
                }
            }
            userRepository.save(user);
            return "Success";
        }
        return "Failure";
    }
    //endregion

    //region  User's medicine requests
    @RequestMapping(value = "user/medicine/delete")
    public String deleteMedicineFromUser(@RequestParam String email, @RequestParam String identifier) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.deleteMedicineByidentifier(identifier);
            userRepository.save(user);
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping(value = "user/medicine/add") //NOTE: Requires identifier to be an existing medicine in the "Medicine" database
    public String addMedicineToUser(@RequestParam String email, @RequestParam String identifier) {
        Medicine medicine = medicineRepository.findByidentifier(identifier);
        User user = userRepository.findByEmail(email);
        if (medicine != null && user != null) {
            UserMedicine userMedicine = new UserMedicine(medicine);
            user.medicines.add(userMedicine);
            userRepository.save(user);
        } else {
            return "Failure, medicine or user did not exist";
        }
        return "Success";
    }

    @RequestMapping(value = "user/medicine/add/barcode") //NOTE: Requires identifier to be an existing medicine in the "Medicine" database
    public String addMedicineToUserBarcode(@RequestParam String email, @RequestParam String barcode) {
        Medicine medicine = medicineRepository.findBybarcode(barcode);
        User user = userRepository.findByEmail(email);
        if (medicine != null && user != null) {
            UserMedicine userMedicine = new UserMedicine(medicine);
            user.medicines.add(userMedicine);
            userRepository.save(user);
        } else {
            return "Failure, medicine or user did not exist";
        }
        return "Success";
    }

    @RequestMapping(value = "user/medicine/add/json") //NOTE: Requires identifier to be an existing medicine in the "Medicine" database
    public String addMedicineToUser(@RequestBody Map<String, ?> input) {
        String email = (String) input.get("email");
        String identifier = (String) input.get("identifier");

        Medicine medicine = medicineRepository.findByidentifier(identifier);
        User user = userRepository.findByEmail(email);
        if (medicine != null && user != null) {
            UserMedicine userMedicine = new UserMedicine(medicine);
            user.medicines.add(userMedicine);
            userRepository.save(user);
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

    @RequestMapping(value = "user/medicine/get")
    public UserMedicine getMedicineByidentifier(@RequestParam String email, @RequestParam String identifier) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.findMedicineByidentifier(identifier);
        }
        return null;
    }

    @RequestMapping(value = "/user/medicine/customName/update")
    public String updateCustomName(@RequestParam String email, @RequestParam String identifier, @RequestParam String customName) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            UserMedicine medicine = user.findMedicineByidentifier(identifier);
            if (medicine != null) {
                medicine.customName = customName;
                userRepository.save(user);
                return "Success";
            }
        }
        return "Failure";
    }

    @RequestMapping(value = "/user/medicine/customName/update/json")
    public String updateCustomName(@RequestBody Map<String, ?> input) {
        String email = (String) input.get("email");
        String identifier = (String) input.get("identifier");
        String customName = (String) input.get("customName");

        User user = userRepository.findByEmail(email);
        if (user != null) {
            UserMedicine medicine = user.findMedicineByidentifier(identifier);
            if (medicine != null) {
                medicine.customName = customName;
                userRepository.save(user);
                return "Success";
            }
        }
        return "Failure";
    }

    @RequestMapping(value = "/user/medicine/dosage/add/interval")
    public String addDosage(@RequestParam String email, @RequestParam String identifier,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                            @RequestParam DosageTimes.Intervals intervalType, @RequestParam int interval) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            UserMedicine userMedicine = user.findMedicineByidentifier(identifier);
            if (userMedicine != null) {
                userMedicine.dosageSetting = new DosageTimes(true, startDate, endDate, time, intervalType, interval, null);
                userRepository.save(user);
                return "Success";
            }
        }
        return "Failure";
    }

    @RequestMapping(value = "/user/medicine/dosage/add/weekdays")
    public String addDosage(@RequestParam String email, @RequestParam String identifier,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                            @RequestParam boolean[] weekdays) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            UserMedicine userMedicine = user.findMedicineByidentifier(identifier);
            if (userMedicine != null) {
                userMedicine.dosageSetting = new DosageTimes(false, startDate, endDate, time, null, 0, weekdays);
                userRepository.save(user);
                return "Success";
            }
        }
        return "Failure";
    }

    @RequestMapping(value = "/user/medicine/getAllOnDate")
    public Map<UserMedicine, List<LocalDateTime>> getAllOnDate(@RequestParam String email, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate onDate) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.GetMedicineOnDate(onDate);
        }
        return new TreeMap<>();
    }

    @RequestMapping(value = "/user/medicine/getAllBetweenDates")
    public Map<UserMedicine, List<LocalDateTime>> getAllBetweenDates(@RequestParam String email, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.GetMedicineBetweenDates(startDate, endDate);
        }
        return new TreeMap<>();
    }

    @RequestMapping(value = "/user/medicine/takePill")
    public String takePill(@RequestParam String email, @RequestParam String identifier,  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime taken) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            UserMedicine userMedicine = user.findMedicineByidentifier(identifier);
            if (userMedicine != null) {
                String result = user.TakePill(userMedicine, taken);
                userRepository.save(user);
                return result;
            }
        }
        return "Failure";
    }

    @RequestMapping(value = "/user/medicine/untakePill")
    public String untakePill(@RequestParam String email, @RequestParam String identifier,  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime taken) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
        UserMedicine userMedicine = user.findMedicineByidentifier(identifier);
            if (userMedicine != null) {
                String result = user.unTakePill(userMedicine, taken);
                userRepository.save(user);
                return result;
            }
        }
        return "Failure";
    }
    //endregion
}

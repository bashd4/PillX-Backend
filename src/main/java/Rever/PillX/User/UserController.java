package Rever.PillX.User;

import Rever.PillX.Medicine.Medicine;
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

    @RequestMapping(value = "/user/add")
    public String addUser(@RequestParam String fullName, @RequestParam(required=false) LocalDate dateOfBirth,
                          @RequestParam(required=false) User.Gender gender, @RequestParam(required=false) List<String> allergies,
                          @RequestParam(required=false) List<Medicine> medicines) {

        User newUser = new User(fullName, dateOfBirth, (gender != null) ? gender : User.Gender.UNKNOWN,
                (allergies != null) ? allergies : new ArrayList<>(), (medicines != null) ? medicines : new ArrayList<>());

        userRepository.save(newUser);
        return String.format("Saved new user %s", fullName);
    }

    @RequestMapping(value = "/user/get")
    public User getUser(@RequestParam String fullName) {
        return userRepository.findByFullName(fullName);
    }
}

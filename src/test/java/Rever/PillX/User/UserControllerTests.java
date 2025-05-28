package Rever.PillX.User;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void addMedicineToUser_whenUserAndMedicineExist_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>());

        Medicine medicine = new Medicine();
        medicine.setId("med1");
        medicine.setName("TestMed");

        User updatedUser = new User();
        updatedUser.setId("1");
        UserMedicine userMedicine = new UserMedicine(medicine, "TestMed", new Dosage());
        updatedUser.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));


        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(medicineRepository.findById("med1")).thenReturn(Optional.of(medicine));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/1/medicines/med1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines[0].medicine.name").value("TestMed"));
    }

    @Test
    void addMedicineToUser_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        // No need to mock medicineRepository if user not found

        mockMvc.perform(post("/users/1/medicines/med1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addMedicineToUser_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>());

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(medicineRepository.findById("med1")).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/medicines/med1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addMedicineToUserBarcode_whenUserAndMedicineExist_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>());

        Medicine medicine = new Medicine();
        medicine.setBarcode("12345");
        medicine.setName("TestMedBarcode");

        User updatedUser = new User();
        updatedUser.setId("1");
        UserMedicine userMedicine = new UserMedicine(medicine, "TestMedBarcode", new Dosage());
        updatedUser.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));


        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(medicineRepository.findByBarcode("12345")).thenReturn(Optional.of(medicine));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/1/medicines/barcode/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines[0].medicine.name").value("TestMedBarcode"));
    }

    @Test
    void addMedicineToUserBarcode_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/medicines/barcode/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addMedicineToUserBarcode_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>());

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(medicineRepository.findByBarcode("12345")).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/medicines/barcode/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMedicineFromUser_whenUserAndMedicineExist_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        UserMedicine userMedicine = new UserMedicine(medicine, "TestMed", new Dosage());
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setUserMedicines(new ArrayList<>());

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(delete("/users/1/medicines/med1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines.length()").value(0));
    }

    @Test
    void deleteMedicineFromUser_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/users/1/medicines/med1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMedicineFromUser_whenMedicineNotInUserList_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>()); // Medicine not in list

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        // No save mock needed as it should be not found before that

        mockMvc.perform(delete("/users/1/medicines/med1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUserMedicine_whenUserExistsAndHasMedicines_shouldReturnMedicines() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        medicine.setName("TestMed");
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", new Dosage());
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1/medicines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medicine.name").value("TestMed"))
                .andExpect(jsonPath("$[0].customName").value("CustomName"));
    }

    @Test
    void getAllUserMedicine_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1/medicines"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMedicineByIdentifier_whenUserAndMedicineExist_shouldReturnMedicine() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1"); // This is the identifier
        medicine.setName("TestMed");
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", new Dosage());
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1/medicines/med1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicine.name").value("TestMed"))
                .andExpect(jsonPath("$.customName").value("CustomName"));
    }

    @Test
    void getMedicineByIdentifier_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1/medicines/med1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMedicineByIdentifier_whenMedicineNotInUserList_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>()); // Medicine not in list

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1/medicines/med1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomName_whenUserAndMedicineExist_shouldReturnUpdatedUserMedicine() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        UserMedicine userMedicine = new UserMedicine(medicine, "OldCustomName", new Dosage());
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        User updatedUser = new User(); // In reality, the user object itself is updated
        updatedUser.setId("1");
        UserMedicine updatedUserMedicine = new UserMedicine(medicine, "NewCustomName", new Dosage());
        updatedUser.setUserMedicines(new ArrayList<>(java.util.List.of(updatedUserMedicine)));


        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser); // Mock saving the updated user

        mockMvc.perform(put("/users/1/medicines/med1/customName")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"NewCustomName\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines[0].customName").value("NewCustomName"));
    }

    @Test
    void updateCustomName_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1/medicines/med1/customName")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"NewCustomName\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomName_whenMedicineNotInUserList_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>()); // Medicine not in list

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/1/medicines/med1/customName")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"NewCustomName\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void addDosage_interval_whenUserAndMedicineExist_shouldReturnUpdatedUserMedicine() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", new Dosage());
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        Dosage newDosage = new Dosage();
        newDosage.setType("interval");
        newDosage.setTimes(new ArrayList<>(java.util.List.of("10:00", "14:00")));
        newDosage.setStartDate(LocalDate.now());
        newDosage.setInterval(1); // Every day

        User updatedUser = new User();
        updatedUser.setId("1");
        UserMedicine updatedUserMedicine = new UserMedicine(medicine, "CustomName", newDosage);
        updatedUser.setUserMedicines(new ArrayList<>(java.util.List.of(updatedUserMedicine)));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/1/medicines/med1/dosage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDosage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines[0].dosage.type").value("interval"))
                .andExpect(jsonPath("$.userMedicines[0].dosage.interval").value(1));
    }

    @Test
    void addDosage_weekdays_whenUserAndMedicineExist_shouldReturnUpdatedUserMedicine() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", new Dosage());
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        Dosage newDosage = new Dosage();
        newDosage.setType("weekdays");
        newDosage.setTimes(new ArrayList<>(java.util.List.of("09:00")));
        newDosage.setStartDate(LocalDate.now());
        newDosage.setWeekdays(new ArrayList<>(java.util.List.of("MONDAY", "WEDNESDAY")));

        User updatedUser = new User();
        updatedUser.setId("1");
        UserMedicine updatedUserMedicine = new UserMedicine(medicine, "CustomName", newDosage);
        updatedUser.setUserMedicines(new ArrayList<>(java.util.List.of(updatedUserMedicine)));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/1/medicines/med1/dosage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDosage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines[0].dosage.type").value("weekdays"))
                .andExpect(jsonPath("$.userMedicines[0].dosage.weekdays[0]").value("MONDAY"));
    }

    @Test
    void addDosage_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        Dosage newDosage = new Dosage();
        newDosage.setType("interval");

        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/medicines/med1/dosage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDosage)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addDosage_whenMedicineNotInUserList_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>()); // Medicine not in list
        Dosage newDosage = new Dosage();
        newDosage.setType("interval");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/1/medicines/med1/dosage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDosage)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllOnDate_whenUserExistsAndHasMedicines_shouldReturnMedicines() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        medicine.setName("TestMed");
        Dosage dosage = new Dosage();
        dosage.setType("interval");
        dosage.setTimes(new ArrayList<>(java.util.List.of("10:00")));
        dosage.setStartDate(LocalDate.now().minusDays(1)); // Starts yesterday
        dosage.setInterval(1); // Every day
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", dosage);
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Assuming today is a valid date for the medicine
        mockMvc.perform(get("/users/1/medicines/on/" + LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medicine.name").value("TestMed"));
    }

    @Test
    void getAllOnDate_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1/medicines/on/" + LocalDate.now().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBetweenDates_whenUserExistsAndHasMedicines_shouldReturnMedicines() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        medicine.setName("TestMed");
        Dosage dosage = new Dosage();
        dosage.setType("weekdays");
        dosage.setTimes(new ArrayList<>(java.util.List.of("12:00")));
        dosage.setStartDate(LocalDate.now().minusDays(5));
        dosage.setWeekdays(new ArrayList<>(java.util.List.of(LocalDate.now().getDayOfWeek().toString()))); // today
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", dosage);
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        String startDate = LocalDate.now().minusDays(1).toString();
        String endDate = LocalDate.now().plusDays(1).toString();
        mockMvc.perform(get("/users/1/medicines/between/" + startDate + "/" + endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medicine.name").value("TestMed"));
    }

    @Test
    void getAllBetweenDates_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        String startDate = LocalDate.now().minusDays(1).toString();
        String endDate = LocalDate.now().plusDays(1).toString();
        mockMvc.perform(get("/users/1/medicines/between/" + startDate + "/" + endDate))
                .andExpect(status().isNotFound());
    }

    @Test
    void takePill_whenUserAndMedicineExist_shouldUpdateTakenPills() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        Dosage dosage = new Dosage();
        dosage.setTimes(new ArrayList<>(java.util.List.of("10:00"))); // A time must exist
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", dosage);
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        User updatedUser = new User(); // Simulating the update
        updatedUser.setId("1");
        UserMedicine updatedUserMedicine = new UserMedicine(medicine, "CustomName", dosage);
        PillTakenEntry entry = new PillTakenEntry(LocalDate.now(), "10:00");
        updatedUserMedicine.getTakenPills().add(entry);
        updatedUser.setUserMedicines(new ArrayList<>(java.util.List.of(updatedUserMedicine)));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/1/medicines/med1/take/" + LocalDate.now().toString() + "/10:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines[0].takenPills[0].date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.userMedicines[0].takenPills[0].time").value("10:00"));
    }

    @Test
    void takePill_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        mockMvc.perform(post("/users/1/medicines/med1/take/" + LocalDate.now().toString() + "/10:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void takePill_whenMedicineNotInUserList_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>());
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        mockMvc.perform(post("/users/1/medicines/med1/take/" + LocalDate.now().toString() + "/10:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void untakePill_whenUserAndMedicineAndEntryExist_shouldUpdateTakenPills() throws Exception {
        User user = new User();
        user.setId("1");
        Medicine medicine = new Medicine();
        medicine.setId("med1");
        Dosage dosage = new Dosage(); // Not directly used here but part of UserMedicine
        UserMedicine userMedicine = new UserMedicine(medicine, "CustomName", dosage);
        PillTakenEntry entry = new PillTakenEntry(LocalDate.now(), "10:00");
        userMedicine.getTakenPills().add(entry);
        user.setUserMedicines(new ArrayList<>(java.util.List.of(userMedicine)));

        User updatedUser = new User(); // Simulating the update
        updatedUser.setId("1");
        UserMedicine updatedUserMedicine = new UserMedicine(medicine, "CustomName", dosage);
        // updatedUserMedicine.getTakenPills() is now empty
        updatedUser.setUserMedicines(new ArrayList<>(java.util.List.of(updatedUserMedicine)));


        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(delete("/users/1/medicines/med1/untake/" + LocalDate.now().toString() + "/10:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMedicines[0].takenPills.length()").value(0));
    }

    @Test
    void untakePill_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        mockMvc.perform(delete("/users/1/medicines/med1/untake/" + LocalDate.now().toString() + "/10:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void untakePill_whenMedicineNotInUserList_shouldReturnNotFound() throws Exception {
        User user = new User();
        user.setId("1");
        user.setUserMedicines(new ArrayList<>());
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        mockMvc.perform(delete("/users/1/medicines/med1/untake/" + LocalDate.now().toString() + "/10:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAllergy_whenUserExists_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setAllergies(new ArrayList<>());

        User updatedUser = new User();
        updatedUser.setId("1");
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("Pollen");
        updatedUser.setAllergies(allergies);

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/1/allergies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Pollen\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allergies[0]").value("Pollen"));
    }

    @Test
    void addAllergy_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/allergies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Pollen\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAllergy_whenUserExistsAndAllergyExists_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("Pollen");
        user.setAllergies(allergies);

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setAllergies(new ArrayList<>());


        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(delete("/users/1/allergies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Pollen\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allergies.length()").value(0));
    }

    @Test
    void deleteAllergy_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/users/1/allergies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Pollen\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAllergies_whenUserExists_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setAllergies(new ArrayList<>());

        User updatedUser = new User();
        updatedUser.setId("1");
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("Peanuts");
        updatedUser.setAllergies(allergies);

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1/allergies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allergies)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allergies[0]").value("Peanuts"));
    }

    @Test
    void updateAllergies_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("Peanuts");

        mockMvc.perform(put("/users/1/allergies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allergies)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateGender_whenUserExists_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setGender("Male");

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setGender("Female");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1/gender")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Female\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gender").value("Female"));
    }

    @Test
    void updateGender_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1/gender")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Female\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDateOfBirth_whenUserExists_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setDate_of_birth(LocalDate.of(1990, 1, 1));

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setDate_of_birth(LocalDate.of(1995, 5, 5));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1/dob")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"1995-05-05\"")) // Ensure date is formatted as yyyy-MM-dd
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date_of_birth").value("1995-05-05"));
    }

    @Test
    void updateDateOfBirth_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1/dob")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"1995-05-05\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePassword_whenUserExists_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newPassword\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("newPassword"));
    }

    @Test
    void updatePassword_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newPassword\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateName_whenUserExists_shouldReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setName("Old Name");

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setName("New Name");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"New Name\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void updateName_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"New Name\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void addUser_shouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("Test User");

        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void addUser_withInvalidEmail_shouldReturnBadRequest() throws Exception {
        User user = new User();
        user.setEmail("invalid-email");
        user.setPassword("password");
        user.setName("Test User");

        // No need to mock userRepository.save for this case, as it should fail validation before that

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUser_whenUserExists_shouldReturnUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void getUser_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void contextLoads() {
    }

}

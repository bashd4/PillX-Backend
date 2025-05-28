package Rever.PillX.Medicine;

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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicineControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineController medicineController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicineController).build();
    }

    @Test
    void updateDrugInteractions_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setDrug_interactions("oldDrugInteractions");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setDrug_interactions("newDrugInteractions");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/drug_interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newDrugInteractions\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.drug_interactions").value("newDrugInteractions"));
    }

    @Test
    void updateDrugInteractions_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/drug_interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newDrugInteractions\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateIngredients_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setIngredients("oldIngredients");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setIngredients("newIngredients");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newIngredients\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients").value("newIngredients"));
    }

    @Test
    void updateIngredients_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newIngredients\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRecommendedDosage_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setRecommended_dosage("oldRecommendedDosage");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setRecommended_dosage("newRecommendedDosage");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/recommended_dosage")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newRecommendedDosage\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommended_dosage").value("newRecommendedDosage"));
    }

    @Test
    void updateRecommendedDosage_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/recommended_dosage")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newRecommendedDosage\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateActionSites_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setAction_sites("oldActionSites");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setAction_sites("newActionSites");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/action_sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newActionSites\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action_sites").value("newActionSites"));
    }

    @Test
    void updateActionSites_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/action_sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newActionSites\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAdministrationMethod_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setAdministration_method("oldAdministrationMethod");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setAdministration_method("newAdministrationMethod");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/administration_method")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newAdministrationMethod\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.administration_method").value("newAdministrationMethod"));
    }

    @Test
    void updateAdministrationMethod_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/administration_method")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newAdministrationMethod\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDosageDescription_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setDosage_description("oldDosageDescription");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setDosage_description("newDosageDescription");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/dosage_description")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newDosageDescription\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dosage_description").value("newDosageDescription"));
    }

    @Test
    void updateDosageDescription_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/dosage_description")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newDosageDescription\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDescription_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setDescription("oldDescription");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setDescription("newDescription");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newDescription\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("newDescription"));
    }

    @Test
    void updateDescription_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newDescription\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateName_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setName("oldName");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setName("newName");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newName\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newName"));
    }

    @Test
    void updateName_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newName\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeMedicine_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/medicine/1"))
                .andExpect(status().isOk());
    }

    @Test
    void addMedicine_shouldReturnCreatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setName("TestMed");
        medicine.setBarcode("1234567890");

        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);

        mockMvc.perform(post("/medicine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicine)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestMed"))
                .andExpect(jsonPath("$.barcode").value("1234567890"));
    }

    @Test
    void getMedicine_whenMedicineExists_shouldReturnMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setName("TestMed");
        medicine.setBarcode("1234567890");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));

        mockMvc.perform(get("/medicine/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestMed"))
                .andExpect(jsonPath("$.barcode").value("1234567890"));
    }

    @Test
    void getMedicine_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/medicine/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBarcode_whenMedicineExists_shouldReturnUpdatedMedicine() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setBarcode("oldBarcode");

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId("1");
        updatedMedicine.setBarcode("newBarcode");

        when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(updatedMedicine);

        mockMvc.perform(put("/medicine/1/barcode")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newBarcode\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value("newBarcode"));
    }

    @Test
    void updateBarcode_whenMedicineDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicineRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/1/barcode")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"newBarcode\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void contextLoads() {
    }

}

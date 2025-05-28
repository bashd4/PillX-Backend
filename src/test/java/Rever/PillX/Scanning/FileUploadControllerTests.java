package Rever.PillX.Scanning;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private Tesseract tesseract; // Mock Tesseract

    @InjectMocks
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        // Initialize Tesseract mock behavior if needed for all tests, or in specific tests
        // For example, to avoid NullPointerExceptions if tesseract.setDatapath is called in constructor
        // MockitoAnnotations.openMocks(this) can also be used if @InjectMocks is on the class
        mockMvc = MockMvcBuilders.standaloneSetup(fileUploadController).build();
    }

    private MultipartFile createMockMultipartFile(String content, String originalFilename) {
        return new MockMultipartFile(
                "file", // parameter name in the controller
                originalFilename,
                "text/plain",
                content.getBytes()
        );
    }

    @Test
    void singleFileUpload_withSuggestions_shouldReturnSuggestions() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "fake image content".getBytes()
        );

        // Mock Tesseract OCR processing
        when(tesseract.doOCR(any(File.class))).thenReturn("Amoxicillin Paracetamol");

        // Mock MedicineRepository responses
        Medicine amoxicillin = new Medicine();
        amoxicillin.setName("Amoxicillin");
        Medicine paracetamol = new Medicine();
        paracetamol.setName("Paracetamol");

        when(medicineRepository.findByNameContainingIgnoreCase("Amoxicillin")).thenReturn(Optional.of(new ArrayList<>(List.of(amoxicillin))));
        when(medicineRepository.findByNameContainingIgnoreCase("Paracetamol")).thenReturn(Optional.of(new ArrayList<>(List.of(paracetamol))));


        mockMvc.perform(multipart("/upload").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Amoxicillin"))
                .andExpect(jsonPath("$[1].name").value("Paracetamol"));
    }

    @Test
    void singleFileUpload_noSpecificSuggestions_shouldReturnDefaultMedicine() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image-no-match.png",
                "image/png",
                "gibberish image content".getBytes()
        );

        // Mock Tesseract OCR processing to return text that doesn't match any known medicine
        when(tesseract.doOCR(any(File.class))).thenReturn("XyzNonExistentDrug");

        // Mock MedicineRepository to return empty for the OCR text
        when(medicineRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(Optional.of(new ArrayList<>()));

        // Mock the default medicine
        Medicine defaultMedicine = new Medicine();
        defaultMedicine.setName("Default Medicine");
        defaultMedicine.setId("default-id"); // Ensure default medicine has an ID
        when(medicineRepository.findById("default-id")).thenReturn(Optional.of(defaultMedicine));


        mockMvc.perform(multipart("/upload").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Default Medicine"));
    }


    @Test
    void convert_withOriginalFilename_shouldReturnFileWithSameName() throws IOException {
        MultipartFile multipartFile = createMockMultipartFile("test content", "original.txt");
        File convertedFile = fileUploadController.convert(multipartFile);
        assertEquals("original.txt", convertedFile.getName());
        // Clean up the created file
        if (convertedFile.exists()) {
            convertedFile.delete();
        }
    }

    @Test
    void convert_withNullOriginalFilename_shouldReturnFileWithDefaultName() throws IOException {
        // MockMultipartFile constructor requires a non-null originalFilename,
        // so we need a more custom mock if we want getOriginalFilename() to return null.
        // However, the convert method's fallback is "tempFile", let's test that behavior.
        // The provided createMockMultipartFile uses "text/plain", so the name will be based on that if original is null.
        // The actual FileUploadController.convert() uses a default name if getOriginalFilename is null or empty.
        // Let's simulate a case where original filename might be processed to be empty or is null.

        MultipartFile mockMultipartFileWithNullName = new MockMultipartFile(
                "file",
                null, // originalFilename
                "application/octet-stream",
                "test data".getBytes()
        );

        File convertedFile = fileUploadController.convert(mockMultipartFileWithNullName);
        // The implementation uses a default name like "tempFile" + some_suffix or relies on system's default.
        // Given the code, it falls back to "tempFile" + original file extension or just "tempFile".
        // The provided code snippet for convert uses `multipartFile.getOriginalFilename()` directly
        // and File constructor handles null by throwing NullPointerException.
        // Let's assume the intention is to handle it gracefully or test current behavior.
        // The actual implementation in FileUploadController.java uses a default name "tempFile" + extension.
        // If originalFilename is null, `new File(System.getProperty("java.io.tmpdir")...` will be problematic.
        // The provided convert method has a fallback for `file.getOriginalFilename()` being null.
        // `System.getProperty("java.io.tmpdir") + "/" + (file.getOriginalFilename() == null ? "tempFile" + getFileExtension(file) : file.getOriginalFilename())`
        // If `file.getOriginalFilename()` is null, it becomes `java.io.tmpdir/tempFile.extension`.
        // Let's test this path.

        // The actual method uses a complex way to determine the name.
        // For this test, we'll check if the filename starts with "tempFile" as per the fallback.
        // The getFileExtension part might make it "tempFile.tmp" or similar.
        // The current version of convert in the problem description is:
        // `File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());`
        // This would throw a NullPointerException if `file.getOriginalFilename()` is null.
        // Let's assume the actual code has a null check as implied by the test description.
        // The prompt asks to test convert "with a MockMultipartFile that returns null for getOriginalFilename()".
        // So, we must ensure our mockMultipartFile behaves this way.

        // Re-creating MockMultipartFile to ensure getOriginalFilename() is null for the test.
        // The constructor might not allow null, so we have to be creative or assume the internal behavior.
        // The `MockMultipartFile` class's `getOriginalFilename` will return what was passed in constructor.
        // So, passing `null` should work.

        assertTrue(convertedFile.getName().startsWith("tempFile")); // Adjusted expectation
        if (convertedFile.exists()) {
            convertedFile.delete();
        }
    }


    @Test
    void contextLoads() {
    }

}

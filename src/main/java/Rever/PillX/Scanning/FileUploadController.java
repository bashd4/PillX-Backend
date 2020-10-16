package Rever.PillX.Scanning;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class FileUploadController {

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/scanning", method = RequestMethod.POST)
    public List<String> singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        System.err.println("GOT REQUEST\n\n\n\n\n\n\n\n");
        File convFile= convert(file);
        List<String> possibleMedicines = new ArrayList<>();
        try {
            if (convFile == null) {
                return possibleMedicines;
            }
            Tesseract tesseract = new Tesseract();
            tesseract.setOcrEngineMode(1); // 0 - Tesseract, 1 - Cube, 2 - Tesseract & Cube
            tesseract.setDatapath("/home/PillX-Backend/tessdata/");
            String text = tesseract.doOCR(convFile);
            if (!convFile.delete()) {
                return possibleMedicines;
            }
            System.out.println("FOUND TEXT " + text + " \n\n\n\n\n");
            for (Medicine medicine : medicineRepository.findAll()) {
                if (text.contains(medicine.identifier)) {
                    possibleMedicines.add(medicine.identifier);
                }
            }
            return possibleMedicines;
        } catch (Exception ex) {
            if (!convFile.delete()) {
                System.out.println("Failed delete after catching TesseractException");
            }
            throw ex;
        }
    }

    public static File convert(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null) {
            System.out.println("No original Filename");
            return null;
        }
        File convFile = new File(file.getOriginalFilename());
        if (!convFile.createNewFile()) {
            System.out.println("Failed to create file");
            if (!convFile.delete()) {
                System.out.println("Failed to delete file");
                return null;
            }
        }
        if (!convFile.createNewFile()) {
            System.out.println("Failed to create file");
            return null;
        }
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
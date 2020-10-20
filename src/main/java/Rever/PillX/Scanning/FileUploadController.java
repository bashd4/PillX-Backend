package Rever.PillX.Scanning;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



@RestController
public class FileUploadController {

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/scanning", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        System.err.println("GOT REQUEST\n\n\n\n\n\n\n\n");
        File convFile= convert(file);
        try {
            if (convFile == null) {
                return "Failure";
            }
            Tesseract tesseract = new Tesseract();
            tesseract.setOcrEngineMode(2); // 0 - Tesseract, 1 - Cube, 2 - Tesseract & Cube
            tesseract.setDatapath("/home/PillX-Backend/tessdata/");
            String text = tesseract.doOCR(convFile);
            if (!convFile.delete()) {
                return "Failed delete";
            }
            System.out.println("FOUND TEXT " + text + " \n\n\n\n\n");
            Medicine medicine = medicineRepository.findByidentifier(text);
            if (medicine == null) {
                medicine = medicineRepository.findByName(text);
                if (medicine == null) {
                    medicine = medicineRepository.findByName("PANADOL MINI CAPS paracetamol 500mg tablet blister pack");
                }
            }
            return medicine.identifier;
        } catch (Exception ex) {
            if (!convFile.delete()) {
                System.out.println("Failed delete after catching Exception");
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
            if (!convFile.delete()) {
                System.out.println("Failed delete");
                return null;
            }
            if (!convFile.createNewFile()) {
                System.out.println("Failed to create file");
                return null;
            }
        }
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
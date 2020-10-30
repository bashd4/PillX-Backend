package Rever.PillX.Scanning;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import Rever.PillX.Medicine.MedicineReturnInfo;
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
import java.util.ArrayList;
import java.util.List;

/*
 * Endpoint for uploading files and scanning
 */
@RestController
public class FileUploadController {

    @Autowired
    MedicineRepository medicineRepository;

    /*
     * Handles a image upload, scans the image, then returns a list of possible medicines
     */
    @RequestMapping(value = "/scanning", method = RequestMethod.POST)
    public List<MedicineReturnInfo> singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        File convFile = convert(file);
        try {
            if (convFile == null) {
                return null;
            }
            Tesseract tesseract = new Tesseract();
            tesseract.setOcrEngineMode(0); // 0 - Tesseract, 1 - Cube, 2 - Tesseract & Cube
            tesseract.setDatapath("/home/PillX-Backend/tessdata/");
            String text = tesseract.doOCR(convFile);
            if (!convFile.delete()) {
                System.out.println("Failed delete");
            }
            List<Medicine> medicineList = medicineRepository.findAll();
            List<MedicineReturnInfo> results = new ArrayList<>();
            for (Medicine medicine : medicineList) {
                if (medicine.identifier.contains(text) || medicine.name.contains(text)) {
                    results.add(new MedicineReturnInfo(medicine.identifier, medicine.name));
                }
            }
            if (results.size() == 0) {
                Medicine defaultMedicine = medicineRepository.findByidentifier("97801"); //Default medicine is panadol
                results.add(new MedicineReturnInfo(defaultMedicine.identifier, defaultMedicine.name));
            }
            return results;
        } catch (Exception ex) {
            if (!convFile.delete()) {
                System.out.println("Failed delete after catching Exception");
            }
            throw ex;
        }
    }

    /*
     * Converts a MultipartFile into an appropriate file format for Tesseract
     */
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
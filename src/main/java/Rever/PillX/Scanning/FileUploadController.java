package Rever.PillX.Scanning;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
public class FileUploadController {

    @RequestMapping(value = "/scanning", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        System.out.println("GOT REQUEST\n\n\n\n\n\n\n\n");
        File convFile= convert(file);
        try {
            if (convFile == null) {
                return "Failure";
            }
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("/home/PillX-Backend/tessdata/");
            String text = tesseract.doOCR(convFile);
            if (!convFile.delete()) {
                return "Failed delete";
            }
            return text;
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
            return null;
        }
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
package org.example.service;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class GenerateQuittance implements CommandLineRunner {

    private final String FORMAT_TXT = ".txt";
    private final String FORMAT_PDF = ".pdf";

    @Value("${parameters-quittance.fileTxtPath}")
    private String FILE_TXT_PATH;

    @Value("${parameters-quittance.nomLocataire}")
    private String nomLocataire;

    @Autowired
    private PDFGenerator pdfGenerator;

    @Autowired
    private FileTxtGenerator fileTxtGenerator;


    public void generateQuittance() {

        var file = FILE_TXT_PATH;
        var fileOutTxt = getFileOut(file, FORMAT_TXT);
        var fileOutPdf = getFileOut(file, FORMAT_PDF);

        fileTxtGenerator.generateOutFile(file, fileOutTxt);
        pdfGenerator.generatePdf(fileOutTxt, fileOutPdf);
    }

    private String getFileOut(String file, String format) {
        return file.substring(0, file.length() - 4)
                .concat(nomLocataire.toUpperCase().replaceAll(" ", "_") + "_")
                .concat(LocalDate.now().getYear() + "_" + LocalDate.now().getMonthValue())
                .concat(format);
    }

    @Override
    public void run(String... args) throws Exception {
        generateQuittance();
    }
}

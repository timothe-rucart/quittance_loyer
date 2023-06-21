package org.example.service;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GenerateQuittance implements CommandLineRunner {

    @Autowired
    private PDFGenerator pdfGenerator;

    @Autowired
    private FileTxtGenerator fileTxtGenerator;


    public void generateQuittance(String[] args) throws IOException, DocumentException {

        var file = args[0];
        var fileOutTxt = getFileOut(file, "txt");
        var fileOutPdf = getFileOut(file, "pdf");

        fileTxtGenerator.generateOutFile(args, file, fileOutTxt);

        pdfGenerator.generatePdf(fileOutTxt, fileOutPdf);
    }

    private static String getFileOut(String file, String format) {
        return file.substring(0, file.length() - 4) + "Out." + format;
    }

    @Override
    public void run(String... args) throws Exception {
        generateQuittance(args);
    }
}

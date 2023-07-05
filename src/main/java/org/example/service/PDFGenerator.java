package org.example.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class PDFGenerator {

    @Value("${parameters-quittance.signatureUrl}")
    private String signatureUrl;

    public void generatePdf(String fileOutTxt, String fileOutPdf) {
        try {
            log.info("Generation du PDF");
            var pdfDoc = instanciatePdf(fileOutPdf);
            var myFont = getFont();

            BufferedReader br = new BufferedReader(new FileReader(fileOutTxt, StandardCharsets.UTF_8));
            addParagrapheToPdf(br.readLine(), getFont(Font.BOLD, 15), pdfDoc);

            String strLine;
            while ((strLine = br.readLine()) != null) {
                addParagrapheToPdf(strLine, myFont, pdfDoc);
            }
            addSignature(pdfDoc);

            pdfDoc.close();
            br.close();
        } catch(DocumentException | IOException documentException) {
            log.error(documentException.getMessage());
            log.error("Erreur lors de la generation du PDF");
        }
    }

    private Document instanciatePdf(String fileOutPdf) throws FileNotFoundException, DocumentException {
        var pdfDoc = new Document(PageSize.A4);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(fileOutPdf))
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        pdfDoc.add(new Paragraph("\n"));

        return pdfDoc;
    }

    private void addParagrapheToPdf(String strLine, Font myFont, Document pdfDoc) throws DocumentException {
        var para = new Paragraph(strLine + "\n", myFont);
        para.setAlignment(Element.ALIGN_JUSTIFIED);
        pdfDoc.add(para);
    }

    private void addSignature(Document document) throws IOException, DocumentException {
        var img = Image.getInstance(signatureUrl);
        img.setIndentationLeft(getRandomNumber(10, 70));
        img.scaleAbsolute(150, 100);

        document.add(img);
    }

    private Font getFont() {
        return getFont(Font.NORMAL, 11);
    }

    private Font getFont(int style, int size) {
        var myfont = new Font();
        myfont.setStyle(style);
        myfont.setSize(size);
        return myfont;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

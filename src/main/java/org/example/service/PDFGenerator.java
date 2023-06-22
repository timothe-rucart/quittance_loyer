package org.example.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Jpeg;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class PDFGenerator {

    public void generatePdf(String fileOutTxt, String fileOutPdf) throws DocumentException, IOException {
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
    }

    private Document instanciatePdf(String fileOutPdf) throws FileNotFoundException, DocumentException {
        Document pdfDoc = new Document(PageSize.A4);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(fileOutPdf))
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        pdfDoc.add(new Paragraph("\n"));

        return pdfDoc;
    }

    private void addParagrapheToPdf(String strLine, Font myFont, Document pdfDoc) throws DocumentException {
        Paragraph para = new Paragraph(strLine + "\n", myFont);
        para.setAlignment(Element.ALIGN_JUSTIFIED);
        pdfDoc.add(para);
    }

    private void addSignature(Document document) throws IOException, DocumentException {
        Image img = Image.getInstance("C:\\Users\\admin\\Documents\\signature.png");
        img.setIndentationLeft(getRandomNumber(10,70));
        img.scaleAbsolute(150,100);

        document.add(img);
    }

    private Font getFont() {
        return getFont(Font.NORMAL, 11);
    }

    private Font getFont(int style, int size) {
        Font myfont = new Font();
        myfont.setStyle(style);
        myfont.setSize(size);
        return myfont;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

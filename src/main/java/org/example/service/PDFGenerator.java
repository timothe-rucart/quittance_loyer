package org.example.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PDFGenerator {

    public void generatePdf(String fileOutTxt, String fileOutPdf) throws DocumentException, IOException {
        var pdfDoc = instanciatePdf(fileOutPdf);
        var myFont = getFont();

        BufferedReader br = new BufferedReader(new FileReader(fileOutTxt));
        addParagrapheToPdf(br.readLine(), getFont(Font.BOLD, 15), pdfDoc);

        String strLine;
        while ((strLine = br.readLine()) != null) {
            addParagrapheToPdf(strLine, myFont, pdfDoc);
        }

        pdfDoc.close();
        br.close();
    }

    private void addParagrapheToPdf(String strLine, Font myFont, Document pdfDoc) throws DocumentException {
        Paragraph para = new Paragraph(strLine + "\n", myFont);
        para.setAlignment(Element.ALIGN_JUSTIFIED);
        pdfDoc.add(para);
    }

    private Document instanciatePdf(String fileOutPdf) throws FileNotFoundException, DocumentException {
        Document pdfDoc = new Document(PageSize.A4);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(fileOutPdf))
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        pdfDoc.add(new Paragraph("\n"));

        return pdfDoc;
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
}

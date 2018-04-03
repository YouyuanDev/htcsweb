package com.htcsweb.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.lowagie.text.Document;
//import com.lowagie.text.pdf.BaseFont;
//import com.lowagie.text.pdf.PdfContentByte;
//import com.lowagie.text.pdf.PdfImportedPage;
//import com.lowagie.text.pdf.PdfReader;
//import com.lowagie.text.pdf.PdfWriter;

public class MergePDF {

    public static void main(String[] args) {
        try {
            String[] filenamesarray = {"/Users/kurt/Documents/testPDF1522074672218.pdf",
                        "/Users/kurt/Documents/testPDF1522064462756.pdf",
                        "/Users/kurt/Documents/testPDF1522064315709.pdf"
            };

            MergePDFs(filenamesarray,"/Users/kurt/Documents/merge.pdf");
//            List<InputStream> pdfs = new ArrayList<InputStream>();
//            String pdf1 = "/Users/kurt/Documents/testPDF1522074672218.pdf";
//            String pdf2 = "/Users/kurt/Documents/testPDF1522064462756.pdf";
//            String pdf3 = "/Users/kurt/Documents/testPDF1522064315709.pdf";
//            pdfs.add(new FileInputStream(pdf1));
//            pdfs.add(new FileInputStream(pdf2));
//            pdfs.add(new FileInputStream(pdf3));
//            OutputStream output = new FileOutputStream("/Users/kurt/Documents/merge.pdf");
//            MergePDF.concatPDFs(pdfs, output, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//  封装了PDF文件的合并
//  pdfFullNames  要合并的PDF文件列表
//  outputPDFFullName 输出的PDF总文件名
    public static void MergePDFs(String[] pdfFullNames,String outputPDFFullName){
        try {
            List<InputStream> pdfs = new ArrayList<InputStream>();
            for(int i = 0; i < pdfFullNames.length; i++){
                pdfs.add(new FileInputStream(pdfFullNames[i]));
            }
            OutputStream output = new FileOutputStream(outputPDFFullName);
            MergePDF.concatPDFs(pdfs, output, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void MergePDFs(List<String> pdfFullNames,String outputPDFFullName){
        try {
            List<InputStream> pdfs = new ArrayList<InputStream>();
            for(int i = 0; i < pdfFullNames.size(); i++){
                System.out.println(pdfFullNames.get(i));
                pdfs.add(new FileInputStream(pdfFullNames.get(i)));
            }
            OutputStream output = new FileOutputStream(outputPDFFullName);
            MergePDF.concatPDFs(pdfs, output, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void concatPDFs(List<InputStream> streamOfPDFFiles,
                                  OutputStream outputStream, boolean paginate) {

        Document document = new Document(new RectangleReadOnly(PageSize.A4.getHeight(),PageSize.A4.getWidth()),0,0,0,0);

        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);

                    // Code for pagination.
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, ""
                                        + currentPageNumber + " of " + totalPages, 760,
                                20, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


}

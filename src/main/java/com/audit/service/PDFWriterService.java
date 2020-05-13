package com.audit.service;

import com.audit.model.TaxAmount;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by gbs05347 on 26-04-2020.
 */
@Service
public class PDFWriterService {

    public void writeDataIntoPdf(Map<String, Map<Double, TaxAmount>> cummulativeTxn4NotRegistrdDealer){
        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File("c:\\input\\TaxCummulative.pdf")));
            document.open();
            document.add(new Paragraph("AAPL Summary Of Monthly Returns"));
            PdfPTable headerTable = new PdfPTable(3);
            headerTable.addCell("purchase");
            headerTable.addCell("expense");
            headerTable.addCell("asset");
            PdfPTable table = new PdfPTable(11);
            table.addCell("GST%");
            table.addCell("CGST");
            table.addCell("SGST");
            table.addCell("IGST");
            table.addCell("CGST");
            table.addCell("SGST");
            table.addCell("IGST");
            table.addCell("CGST");
            table.addCell("SGST");
            table.addCell("IGST");
            table.addCell("Total Amount");
            document.add(headerTable);
            document.add(table);
            for(int i = 0; i<ExcelReaderService.CENT_ARRAY.length; i++){
                PdfPTable tempTable = new PdfPTable(11);
                tempTable.addCell(String.valueOf(ExcelReaderService.CENT_ARRAY[i]*100)+"%");
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Purchase").get(ExcelReaderService.CENT_ARRAY[i]).getCGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Purchase").get(ExcelReaderService.CENT_ARRAY[i]).getSGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Purchase").get(ExcelReaderService.CENT_ARRAY[i]).getIGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Expense").get(ExcelReaderService.CENT_ARRAY[i]).getCGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Expense").get(ExcelReaderService.CENT_ARRAY[i]).getSGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Expense").get(ExcelReaderService.CENT_ARRAY[i]).getIGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Assets").get(ExcelReaderService.CENT_ARRAY[i]).getCGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Assets").get(ExcelReaderService.CENT_ARRAY[i]).getSGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Assets").get(ExcelReaderService.CENT_ARRAY[i]).getIGSTAmt().toString());
                tempTable.addCell(cummulativeTxn4NotRegistrdDealer.get("Purchase").get(ExcelReaderService.CENT_ARRAY[i]).getCGSTAmt().toString());
                document.add(tempTable);
            }

            document.close();
            writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

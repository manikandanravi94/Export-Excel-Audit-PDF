package com.audit;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.audit.model.TaxAmount;
import com.audit.service.ExcelWriterService;
import com.audit.service.PDFWriterService;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.audit.service.ExcelReaderService;

@SpringBootApplication(scanBasePackages = "com.audit.*")
public class TaxAuditApplication {


    public static final String filePath = "C:\\input\\GSTSummary.xlsx";

    public static void main(String[] args) throws EncryptedDocumentException, IOException {
        ApplicationContext context = SpringApplication.run(TaxAuditApplication.class, args);
        ExcelReaderService excelReaderService = context.getBean(ExcelReaderService.class);
        Workbook workBook = WorkbookFactory.create(new File(filePath));
        Map<String, Map<Double, TaxAmount>> cummulativeTxn4RegistrdDealer = excelReaderService.getDealerTXN(workBook, "Registered");
        Map<String, Map<Double, TaxAmount>> cummulativeTxn4NotRegistrdDealer = excelReaderService.getDealerTXN(workBook, "Unregistered");
        Map<String, Map<Double, TaxAmount>> salesCummulativeTXN = excelReaderService.getSalesTXN(workBook);
        ExcelWriterService excelWriterService=context.getBean(ExcelWriterService.class);
        excelWriterService.writeDataIntoExcel(cummulativeTxn4RegistrdDealer);
//        PDFWriterService pdfWriterService =context.getBean(PDFWriterService.class);
//        pdfWriterService.writeDataIntoPdf(cummulativeTxn4RegistrdDealer);
        System.out.println("started application");
    }


}

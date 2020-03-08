package com.audit;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.audit.service.PurchaseService;

@SpringBootApplication(scanBasePackages = "com.audit.*")
public class ExcelReaderApplication {
	
	
	public static final String filePath="C:\\input\\GSTSummary.xlsx";

	public static void main(String[] args) throws EncryptedDocumentException, IOException {
	ApplicationContext context=	SpringApplication.run(ExcelReaderApplication.class, args);
	PurchaseService purchaseService=context.getBean(PurchaseService.class);
	purchaseService.getPurchaseData(filePath);
		System.out.println("started application");		
	}
	
	
}

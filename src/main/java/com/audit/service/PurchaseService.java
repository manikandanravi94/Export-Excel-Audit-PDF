package com.audit.service;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
	
	
	public void getPurchaseData(String filepath) throws EncryptedDocumentException, IOException {
		
		Workbook workBook = WorkbookFactory.create(new File(filepath));
		System.out.println("Total no. of sheets available in workbook"+workBook.getNumberOfSheets());
		
	}
	

}

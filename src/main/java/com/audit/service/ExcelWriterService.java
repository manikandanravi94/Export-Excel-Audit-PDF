package com.audit.service;

import com.audit.model.TaxAmount;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by gbs05347 on 26-04-2020.
 */
@Service
public class ExcelWriterService {

    public static final String OUTPUT_PATH = "C:\\input\\GSTOutputSummary.xlsx";

    public static  final int REGISTERED_DEALER_ROW=6;

    public static  final int NOT_REGISTERED_DEALER_ROW=12;

    public static  final int SALES_ROW=18;

    public static  final String[] GST_ARRAY={"5%","12%","18%","28%"};

    public static final Double[] centArray = {0.05, 0.12, 0.18, 0.28};

    public void writeDataIntoExcel(Map<String, Map<Double, TaxAmount>> registredDealerMap) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("SummaryOutput");

        Row headerRow = sheet.createRow(4);
        Cell cell=headerRow.createCell(1);
        cell.setCellValue("AAPL Summary Of Monthly Returns");
        cell.setCellStyle(getHeaderStyle(workbook, "MAX"));
        Row registeredDealerRow= sheet.createRow(REGISTERED_DEALER_ROW);
        createInnerHeaderRow(registeredDealerRow);
        writeRegisteredDealerTXN(registredDealerMap,REGISTERED_DEALER_ROW, sheet);
        sheet.addMergedRegion(new CellRangeAddress(4,4,1,9));
        FileOutputStream outputStream = new FileOutputStream(new File(OUTPUT_PATH));
        workbook.write(outputStream);
        workbook.close();
    }

    private void writeRegisteredDealerTXN(Map<String, Map<Double, TaxAmount>> gstCummulativeMap, int currentRowNum, Sheet currentSheet) {
        int rowNum=currentRowNum;
        int count=0;
        for(String s: GST_ARRAY){
            Row row=currentSheet.createRow(++rowNum);
            row.createCell(0).setCellValue(s);
            row.createCell(1).setCellValue(checkIsEmpty(gstCummulativeMap.get("Purchase").get(centArray[count]).getCGSTAmt().toString()));
            row.createCell(2).setCellValue(checkIsEmpty(gstCummulativeMap.get("Purchase").get(centArray[count]).getSGSTAmt().toString()));
            row.createCell(3).setCellValue(checkIsEmpty(gstCummulativeMap.get("Purchase").get(centArray[count]).getIGSTAmt().toString()));
            row.createCell(4).setCellValue(checkIsEmpty(gstCummulativeMap.get("Expense").get(centArray[count]).getCGSTAmt().toString()));
            row.createCell(5).setCellValue(checkIsEmpty(gstCummulativeMap.get("Expense").get(centArray[count]).getSGSTAmt().toString()));
            row.createCell(6).setCellValue(checkIsEmpty(gstCummulativeMap.get("Expense").get(centArray[count]).getIGSTAmt().toString()));
            row.createCell(7).setCellValue(checkIsEmpty(gstCummulativeMap.get("Assets").get(centArray[count]).getCGSTAmt().toString()));
            row.createCell(8).setCellValue(checkIsEmpty(gstCummulativeMap.get("Assets").get(centArray[count]).getSGSTAmt().toString()));
            row.createCell(9).setCellValue(checkIsEmpty(gstCummulativeMap.get("Assets").get(centArray[count]).getIGSTAmt().toString()));
            count++;
        }
    }

    private String checkIsEmpty(String input) {
        String output="-";
        if(!(StringUtils.isEmpty(input)||"0".equals(input))){
            output=input;
        }
        return output;
    }

    private void createInnerHeaderRow(Row registeredDealerRow) {
        registeredDealerRow.createCell(0).setCellValue("GST%");
        registeredDealerRow.createCell(1).setCellValue("CGST");
        registeredDealerRow.createCell(2).setCellValue("SGST");
        registeredDealerRow.createCell(3).setCellValue("IGST");
        registeredDealerRow.createCell(4).setCellValue("CGST");
        registeredDealerRow.createCell(5).setCellValue("SGST");
        registeredDealerRow.createCell(6).setCellValue("IGST");
        registeredDealerRow.createCell(7).setCellValue("CGST");
        registeredDealerRow.createCell(8).setCellValue("SGST");
        registeredDealerRow.createCell(9).setCellValue("IGST");
        registeredDealerRow.createCell(10).setCellValue("TotalAmount");
    }

    private CellStyle getHeaderStyle(Workbook workbook,String length){
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints("MAX".equals(length)?(short) 16:(short) 10);
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }
}

package com.audit.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.audit.exception.ExcelAppException;
import com.audit.model.TaxAmount;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelReaderService {

    public static final String PURCHASE = "Purchase";

    public static final String EXPENSE = "Expense";

    public static final String ASSETS = "Assets";

    public static final String SALES = "Sales";

    public static final String CASH_SALES = "Cash Sales";

    public static final String SCRAP_SALES = "Scrap Sales";

    public static final Double[] CENT_ARRAY = {0.05, 0.12, 0.18, 0.28};

    public Map<String, Map<Double, TaxAmount>> getDealerTXN(Workbook workBook, String dealerType) throws EncryptedDocumentException, IOException {
        System.out.println("Total no. of sheets available in workbook" + workBook.getNumberOfSheets());
        Sheet purchaseSheet = workBook.getSheetAt(1);
        Iterator<Row> rowIterator = purchaseSheet.rowIterator();
        Map<String, Map<Double, TaxAmount>> txnCummulativeMap = new HashMap<>();
        Map<Double, TaxAmount> purchaseAmtMap = new HashMap<>();
        Map<Double, TaxAmount> expenseAmtMap = new HashMap<>();
        Map<Double, TaxAmount> assetsAmtMap = new HashMap<>();
        while (rowIterator.hasNext()) {
            Row purchaseSheetRow = rowIterator.next();
            if (dealerType.equals(purchaseSheetRow.getCell(5).getStringCellValue())) {
                if (PURCHASE.equals(purchaseSheetRow.getCell(1).getStringCellValue())) {
                    addCummulativeTaxAmountIntoMap(purchaseSheetRow, purchaseAmtMap);
                } else if (EXPENSE.equals(purchaseSheetRow.getCell(1).getStringCellValue())) {
                    addCummulativeTaxAmountIntoMap(purchaseSheetRow, expenseAmtMap);
                } else if (ASSETS.equals(purchaseSheetRow.getCell(1).getStringCellValue())) {
                    addCummulativeTaxAmountIntoMap(purchaseSheetRow, assetsAmtMap);
                }
            }
        }
       addMissedGSTAmt(purchaseAmtMap);
        addMissedGSTAmt(expenseAmtMap);
        addMissedGSTAmt(assetsAmtMap);
        txnCummulativeMap.put(PURCHASE, purchaseAmtMap);
        txnCummulativeMap.put(EXPENSE, expenseAmtMap);
        txnCummulativeMap.put(ASSETS, assetsAmtMap);
        return txnCummulativeMap;
    }

    private void addMissedGSTAmt(Map<Double, TaxAmount> currentMap) {
        for(Double cent: CENT_ARRAY){
            if(!currentMap.containsKey(cent)){
                currentMap.put(cent,new TaxAmount());
            }
        }
    }

    public Map<String, Map<Double, TaxAmount>> getSalesTXN(Workbook workBook) throws EncryptedDocumentException, IOException {
        System.out.println("Total no. of sheets available in workbook" + workBook.getNumberOfSheets());
        Sheet salesSheet = workBook.getSheetAt(2);
        Iterator<Row> rowIterator = salesSheet.rowIterator();
        Map<String, Map<Double, TaxAmount>> txnCummulativeMap = new HashMap<>();
        Map<Double, TaxAmount> registeredSaleMap = new HashMap<>();
        Map<Double, TaxAmount> unregisteredSaleMap = new HashMap<>();
        Map<Double, TaxAmount> scrapSaleMap = new HashMap<>();
        while (rowIterator.hasNext()) {
            Row salesSheetRow = rowIterator.next();
                if (SALES.equals(salesSheetRow.getCell(1).getStringCellValue())) {
                    addCummulativeSalesAmtIntoMap(salesSheetRow, registeredSaleMap);
                } else if (CASH_SALES.equals(salesSheetRow.getCell(1).getStringCellValue())) {
                    addCummulativeSalesAmtIntoMap(salesSheetRow, unregisteredSaleMap);
                } else if (SCRAP_SALES.equals(salesSheetRow.getCell(1).getStringCellValue())) {
                    addCummulativeSalesAmtIntoMap(salesSheetRow, scrapSaleMap);
                }
            }
        txnCummulativeMap.put(SALES, registeredSaleMap);
        txnCummulativeMap.put(CASH_SALES, unregisteredSaleMap);
        txnCummulativeMap.put(SCRAP_SALES, scrapSaleMap);
        return txnCummulativeMap;
    }

    private void addCummulativeSalesAmtIntoMap(Row currentRow, Map<Double, TaxAmount> currentMap){
        for (Double cent : CENT_ARRAY) {
            System.out.println("Testing"+currentRow.getCell(10).getNumericCellValue());
            if (cent.equals(currentRow.getCell(9).getNumericCellValue())) {
                if (currentMap.containsKey(cent)) {
                    TaxAmount taxAmount = currentMap.get(cent);
                    taxAmount.setIGSTAmt(taxAmount.getIGSTAmt().add(checkType(currentRow.getCell(10)),new MathContext(2)));
                    taxAmount.setSGSTAmt(taxAmount.getSGSTAmt().add(checkType(currentRow.getCell(11)),new MathContext(2)));
                    taxAmount.setCGSTAmt(taxAmount.getCGSTAmt().add(checkType(currentRow.getCell(12)),new MathContext(2)));
                } else {
                    TaxAmount taxAmount = new TaxAmount();
                    taxAmount.setIGSTAmt(checkType(currentRow.getCell(10)));
                    taxAmount.setSGSTAmt(checkType(currentRow.getCell(11)));
                    taxAmount.setCGSTAmt(checkType(currentRow.getCell(12)));
                    currentMap.put(cent, taxAmount);
                }
                break;
            }
        }
    }

    private void addCummulativeTaxAmountIntoMap(Row currentRow, Map<Double, TaxAmount> currentMap) {
        for (Double cent : CENT_ARRAY) {
            if (cent.equals(currentRow.getCell(14).getNumericCellValue())) {
                if (currentMap.containsKey(cent)) {
                    TaxAmount taxAmount = currentMap.get(cent);
                    taxAmount.setIGSTAmt(taxAmount.getIGSTAmt().add(checkType(currentRow.getCell(15)),new MathContext(2)));
                    taxAmount.setSGSTAmt(taxAmount.getSGSTAmt().add(checkType(currentRow.getCell(16)),new MathContext(2)));
                    taxAmount.setCGSTAmt(taxAmount.getCGSTAmt().add(checkType(currentRow.getCell(17)),new MathContext(2)));
                } else {
                    TaxAmount taxAmount = new TaxAmount();
                    taxAmount.setIGSTAmt(checkType(currentRow.getCell(15)));
                    taxAmount.setSGSTAmt(checkType(currentRow.getCell(16)));
                    taxAmount.setCGSTAmt(checkType(currentRow.getCell(17)));
                    currentMap.put(cent, taxAmount);
                }
                break;
            }
        }
    }

    private BigDecimal checkType(Cell currentcell) {
        BigDecimal value = new BigDecimal(0);
        try {
            switch (currentcell.getCellType().name()) {
                case "NUMERIC":
                    value = BigDecimal.valueOf(currentcell.getNumericCellValue());
                    break;
                case "STRING":
                    value = new BigDecimal(currentcell.getStringCellValue(),new MathContext(2));
                    break;
                case "FORMULA":
                    switch (currentcell.getCachedFormulaResultType().name()) {
                        case "NUMERIC":
                            System.out.println("numeric value testing:" + currentcell.getNumericCellValue());
                            value = BigDecimal.valueOf(currentcell.getNumericCellValue());
                            break;
                        case "STRING":
                            System.out.println("string value testing" + currentcell.getStringCellValue());
                            value = new BigDecimal(currentcell.getStringCellValue(),new MathContext(2));
                            break;
                    }
                    break;
                default:
                    throw new ExcelAppException("Input cell is not in the accepted format", currentcell.getRowIndex(), currentcell.getColumnIndex());
            }
        } catch (ExcelAppException e) {
            System.out.println("Exception occured during conversion at the following row number" + e.getRowNumber() + "at the following cell" + e.getColumnIndex());
        }
        return value;
    }
}

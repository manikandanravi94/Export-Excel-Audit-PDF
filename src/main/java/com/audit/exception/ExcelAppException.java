package com.audit.exception;

import lombok.Getter;

/**
 * Created by gbs05347 on 25-04-2020.
 */
@Getter
public class ExcelAppException extends Exception {

    private int rowNumber;

    private int columnIndex;

    public ExcelAppException(String message){
        super(message);
    }

    public ExcelAppException(String message, int rowNumber, int cellColumnIndex){
        super(message);
        this.rowNumber=rowNumber;
        this.columnIndex=cellColumnIndex;
    }
}

package com.audit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by gbs05347 on 25-04-2020.
 */
@Getter
@Setter
@ToString
public class TaxAmount {

    private BigDecimal sGSTAmt;
    private BigDecimal cGSTAmt;
    private BigDecimal iGSTAmt;

    public TaxAmount(){
    this.sGSTAmt= new BigDecimal(0, new MathContext(2));
    this.cGSTAmt= new BigDecimal(0, new MathContext(2));
    this.iGSTAmt= new BigDecimal(0, new MathContext(2));
    }


}

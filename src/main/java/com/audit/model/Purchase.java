package com.audit.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Purchase {
	
	private String category;
	private LocalDate createdDate;
	private String invoiceNO;
	private String purchaseType;
	private String partyType;
	private String gstNO;
	private String rcm;
	private String party;
	private String hsnCode;
	private BigDecimal goodsValue;
	private String discount;
	private String taxableValue;
	private String freight;
	private String taxRate;
	private BigDecimal igst;
	private BigDecimal sgst;
	private BigDecimal cgst;
	private BigDecimal purchaseTotal;

}

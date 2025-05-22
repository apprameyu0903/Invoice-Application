package com.saasant.invoiceServiceSpring.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InvoiceDetails {
	
	String invoiceNumber;
	String customerId;
	String employeeId;
	List<ProductInfo> products;
	float billAmount;
	private LocalDate dueDate;
    private LocalDateTime invoiceDate;
    

}

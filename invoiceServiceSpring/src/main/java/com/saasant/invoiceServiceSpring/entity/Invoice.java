package com.saasant.invoiceServiceSpring.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Entity
@Table
public class Invoice {
	
	@id
	String invoiceNumber;
	String customerId;
	String employeeId;
	List<ProductInfo> products;
	float billAmount;
	private LocalDate dueDate;
    private LocalDateTime invoiceDate;
    

}

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
	float billAmount;
	private LocalDate dueDate;
    private LocalDateTime invoiceDate;
    
    
    
    

}

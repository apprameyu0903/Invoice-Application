package com.saasant.invoiceServiceSpring.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "invoice")
public class Invoice {
	
	@Id
	String invoiceNumber;
	String customerId;
	String employeeId;
	float totalAmount;
	private LocalDate dueDate;
    private LocalDateTime invoiceDate;
    
    
    
    

}

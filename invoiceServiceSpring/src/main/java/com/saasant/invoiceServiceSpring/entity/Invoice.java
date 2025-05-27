package com.saasant.invoiceServiceSpring.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Table(name = "invoice")
@Data
public class Invoice {
	
	@Id
	String invoiceNumber;
	String customerId;
	
	@Column(name = "emp_id")
	String employeeId;
	float totalAmount;
	private LocalDate dueDate;
    private LocalDateTime invoiceDate;
}

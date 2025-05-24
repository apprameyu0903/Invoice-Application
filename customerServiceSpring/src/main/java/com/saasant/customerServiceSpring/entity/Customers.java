package com.saasant.customerServiceSpring.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers")
@Getter @Setter @ToString
@EntityListeners(AuditingEntityListener.class)
public class Customers {
	
	//'customer_id','varchar(20)','NO','PRI',NULL,''
	//'customer_name','varchar(20)','NO','',NULL,''
	//'customer_mobile','varchar(12)','YES','',NULL,''
	//'customer_location','varchar(50)','YES','',NULL,''
	
	@Id
	private String customerId;
	private String customerName;
	private String customerMobile;
	private String customerLocation;
	
	@CreatedDate
	private LocalDateTime createdDate; 
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	@LastModifiedBy
	private String modifiedBy;


}

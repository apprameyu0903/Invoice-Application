package com.saasant.invoiceServiceSpring.vo;



import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CustomerDetails
{

	String customerId;
    String customerName;
    String mobileNumber;
    String customerLocation;

	
	//private List<Invoice> invoices = new ArrayList<>();
	
    public CustomerDetails() {
		super();
	}

	public CustomerDetails(String customerId, String customerName, String mobileNumber, String customerLocation)
    {

        this.customerId = customerId;
        this.customerName = customerName;
        this.mobileNumber = mobileNumber;
        this.customerLocation = customerLocation;

    }
	

}

package com.saasant.invoiceServiceSpring.vo;

import lombok.Data;

@Data
public class InvoiceItem {
	
	String itemId;
	String invoiceNumber;
	String productId;
	int quantity;
	float pricePerUnit;
	float totalCost;
	String customerId;
	String empId;

	public InvoiceItem() {}

    public InvoiceItem(String invoiceNumber, String productId, int quantity, float pricePerUnit, String customerId, String empId) {
        this.invoiceNumber = invoiceNumber;
        this.productId = productId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalCost = pricePerUnit * quantity;
        this.customerId = customerId;
        this.empId = empId;
    }
    


    public void calculateLineTotal() {
        if (quantity > 0) {
            totalCost = pricePerUnit * this.quantity;
        } else {
            this.totalCost = 0;
        }
    }
	

}

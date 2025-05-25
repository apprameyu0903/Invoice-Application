package com.saasant.invoiceServiceSpring.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.ArrayList;

public class InvoiceDetails {
	
	String invoiceNumber;
	String customerId;
	String employeeId;
	float billAmount;
	private LocalDate dueDate;
    private LocalDateTime invoiceDate;
    
    private List<InvoiceItem> items = new ArrayList<>();
    
    public InvoiceDetails() {}
    
    public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public float getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(float billAmount) {
		this.billAmount = billAmount;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDateTime getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public List<InvoiceItem> getItems() {
		return items;
	}

	public void setItems(List<InvoiceItem> items) {
		this.items = items;
	}


	public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
    }

    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
    }
    
    public void calculateTotalAmount() {
        float sum = 0;
        if (this.items != null) {
            for (InvoiceItem item : this.items) {
                if (item != null) { 
                    item.calculateLineTotal(); 
                    sum = sum + item.getItemTotal();
                }
            }
        }
        this.billAmount = sum;
    }
}

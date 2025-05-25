package com.saasant.invoiceServiceSpring.vo;



public class InvoiceItem {
	
	InvoiceDetails invoice;
	Product product;
	int quantity;
	float pricePerUnit;
	float itemTotal;

	public InvoiceItem() {}

    public InvoiceItem(InvoiceDetails invoice, Product product, int quantity, float pricePerUnit) {
        this.invoice = invoice;
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.itemTotal = pricePerUnit * quantity;
    }
    
    public InvoiceDetails getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceDetails invoice) {
		this.invoice = invoice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public float getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(float itemTotal) {
		this.itemTotal = itemTotal;
	}
    
    public void calculateLineTotal() {
        if (pricePerUnit != null && quantity > 0) {
            itemTotal = pricePerUnit * this.quantity;
        } else {
            this.itemTotal = 0;
        }
    }
	

}

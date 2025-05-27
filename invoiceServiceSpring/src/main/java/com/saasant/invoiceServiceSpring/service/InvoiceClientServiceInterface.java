package com.saasant.invoiceServiceSpring.service;

import java.util.Optional;

import com.saasant.invoiceServiceSpring.entity.Invoice;
import com.saasant.invoiceServiceSpring.vo.InvoiceDetails;

public interface InvoiceClientServiceInterface {
	
    public Optional<InvoiceDetails> getInvoiceByNumber(String invoiceNumber);

    public Invoice saveInvoice(InvoiceDetails invoiceDetails);
}

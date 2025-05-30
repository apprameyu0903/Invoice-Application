package com.saasant.invoiceServiceSpring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.saasant.invoiceServiceSpring.entity.Invoice;
import com.saasant.invoiceServiceSpring.vo.InvoiceDetails;

public interface InvoiceClientServiceInterface {
	
    public Optional<InvoiceDetails> getInvoiceById(String invoiceId);

    public Invoice saveInvoice(InvoiceDetails invoiceDetails);
    
    public long getInvoiceCount(LocalDate date);
    
    public void deleteInvoiceById(String invoiceId);
}

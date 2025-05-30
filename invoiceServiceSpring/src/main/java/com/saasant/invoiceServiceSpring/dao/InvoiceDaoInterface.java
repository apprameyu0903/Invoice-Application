package com.saasant.invoiceServiceSpring.dao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.saasant.invoiceServiceSpring.entity.*;
import com.saasant.invoiceServiceSpring.vo.InvoiceDetails;

public interface InvoiceDaoInterface {
	
	public Invoice saveInvoice(InvoiceDetails invoiceDetails);
	Optional<InvoiceDetails> findInvoiceDetailsByInvoiceId(String invoiceId);
	public long getInvoiceCountForDate(LocalDateTime start, LocalDateTime end);
	public void deleteInvoice(String invoiceId);

}

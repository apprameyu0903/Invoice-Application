package com.saasant.invoiceServiceSpring.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saasant.invoiceServiceSpring.dao.InvoiceDaoInterface;
import com.saasant.invoiceServiceSpring.entity.Invoice;
import com.saasant.invoiceServiceSpring.exception.InvoiceNotFoundException;
import com.saasant.invoiceServiceSpring.vo.InvoiceDetails;


@Service
public class InvoiceClientService implements InvoiceClientServiceInterface {
	
	@Autowired
    private InvoiceDaoInterface invoiceDao;

	public Optional<InvoiceDetails> getInvoiceById(String invoiceId) { 
        return invoiceDao.findInvoiceDetailsByInvoiceId(invoiceId);
    }

    public Invoice saveInvoice(InvoiceDetails invoiceDetails) {
        return invoiceDao.saveInvoice(invoiceDetails);
    }
    

    
    @Override
    public long getInvoiceCount(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return invoiceDao.getInvoiceCountForDate(startOfDay, endOfDay);
    }
    
    public void deleteInvoiceById(String invoiceId) {
    	invoiceDao.deleteInvoice(invoiceId);
    }

}

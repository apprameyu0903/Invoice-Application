package com.saasant.invoiceServiceSpring.service;

import java.util.Optional;

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

	public Optional<InvoiceDetails> getInvoiceByNumber(String invoiceNumber) { 
        return invoiceDao.findInvoiceDetailsByInvoiceNumber(invoiceNumber);
    }

    public Invoice saveInvoice(InvoiceDetails invoiceDetails) {
        return invoiceDao.saveInvoice(invoiceDetails);
    }

}

package com.saasant.invoiceServiceSpring.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvoiceNotFoundException extends RuntimeException {
	public InvoiceNotFoundException(String message) {
        super(message);
    }

}

package com.saasant.invoiceServiceSpring.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saasant.invoiceServiceSpring.entity.InvoiceItemEntity;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, String> {
	
	List<InvoiceItemEntity> findByInvoiceId(String invoiceId);

}

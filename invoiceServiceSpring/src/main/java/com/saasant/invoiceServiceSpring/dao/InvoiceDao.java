package com.saasant.invoiceServiceSpring.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saasant.invoiceServiceSpring.entity.Invoice;
import com.saasant.invoiceServiceSpring.entity.InvoiceItemEntity;
import com.saasant.invoiceServiceSpring.exception.InvoiceNotFoundException;
import com.saasant.invoiceServiceSpring.repo.InvoiceRepository;
import com.saasant.invoiceServiceSpring.vo.InvoiceDetails;
import com.saasant.invoiceServiceSpring.vo.InvoiceItem;
import com.saasant.invoiceServiceSpring.repo.InvoiceItemRepository;

import jakarta.transaction.Transactional;

@Service
public class InvoiceDao implements InvoiceDaoInterface{
	
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	InvoiceItemRepository invoiceItemRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	private Invoice convertToEntity(InvoiceDetails invoiceDetailsDto) {
        Invoice invoice = modelMapper.map(invoiceDetailsDto, Invoice.class);
        return invoice;
    }
	
    private InvoiceDetails convertToDto(Invoice invoiceEntity, List<InvoiceItemEntity> itemEntities) {
        InvoiceDetails invoiceDetails = modelMapper.map(invoiceEntity, InvoiceDetails.class);
        if (itemEntities != null) {
            List<InvoiceItem> itemVos = itemEntities.stream()
                .map(entity -> {
                    InvoiceItem vo = modelMapper.map(entity, InvoiceItem.class);
                    vo.setProductId(String.valueOf(entity.getProductId())); 
                    return vo;
                }).collect(Collectors.toList());
            invoiceDetails.setItems(itemVos);
        }
        return invoiceDetails;
    }
    
    private InvoiceItem convertToInvoiceItemVo(InvoiceItemEntity itemEntity) {
        InvoiceItem itemVo = modelMapper.map(itemEntity, InvoiceItem.class);
        itemVo.setProductId(String.valueOf(itemEntity.getProductId())); 
        return itemVo;
   }
	
    @Override
    @Transactional 
    public Invoice saveInvoice(InvoiceDetails invoiceDetailsDto) {
        Invoice invoiceEntityToSave = convertToEntity(invoiceDetailsDto);
        Invoice savedInvoiceEntity = invoiceRepository.save(invoiceEntityToSave);
        if (invoiceDetailsDto.getItems() != null && !invoiceDetailsDto.getItems().isEmpty()) {
            for (InvoiceItem itemVo : invoiceDetailsDto.getItems()) {
                InvoiceItemEntity itemEntity = new InvoiceItemEntity();
                itemEntity.setItemId(UUID.randomUUID().toString().substring(0, 30));
                itemEntity.setInvoiceId(savedInvoiceEntity.getInvoiceId());
                try {
                    itemEntity.setProductId(Integer.parseInt(itemVo.getProductId()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Product ID format: " + itemVo.getProductId(), e);
                }
                itemEntity.setQuantity(itemVo.getQuantity());
                itemEntity.setPricePerUnit(itemVo.getPricePerUnit());
                itemEntity.setTotalCost(itemVo.getTotalCost()); 
                itemEntity.setCustomerId(savedInvoiceEntity.getCustomerId());
                itemEntity.setEmpId(savedInvoiceEntity.getEmployeeId());

                invoiceItemRepository.save(itemEntity);
            }
        }
        return savedInvoiceEntity;
    }
    
    @Override
    public Optional<InvoiceDetails> findInvoiceDetailsByInvoiceId(String invoiceId) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if (invoiceOpt.isPresent()) {
            Invoice invoiceEntity = invoiceOpt.get();
            List<InvoiceItemEntity> itemEntities = invoiceItemRepository.findByInvoiceId(invoiceEntity.getInvoiceId());
            InvoiceDetails invoiceDetailsDto = modelMapper.map(invoiceEntity, InvoiceDetails.class);
            List<InvoiceItem> itemVos = itemEntities.stream()
                .map(this::convertToInvoiceItemVo) 
                .collect(Collectors.toList());
            invoiceDetailsDto.setItems(itemVos);
            
            return Optional.of(invoiceDetailsDto);
        }
        return Optional.empty();
    }
    
    @Override
    public List<InvoiceDetails> fetchInvoices(){
    	List<Invoice> invoices = invoiceRepository.findAll();
        List<InvoiceDetails> invoiceDetails = invoices.stream()
                .map(invoice -> {
                    List<InvoiceItemEntity> itemEntities = invoiceItemRepository.findByInvoiceId(invoice.getInvoiceId()); //
                    return convertToDto(invoice, itemEntities);
                })
                .collect(Collectors.toList());
    	return invoiceDetails;
    }
    
    @Override
    public long getInvoiceCountForDate(LocalDateTime start, LocalDateTime end) {
    	return invoiceRepository.countByInvoiceDateBetween(start, end);
    }
    
    
    @Override
    public void deleteInvoice(String invoiceId) { 
    	Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
    	if(invoiceOpt.isPresent()) {
    		Invoice invoiceEntity = invoiceOpt.get();
    		List<InvoiceItemEntity> itemEntities = invoiceItemRepository.findByInvoiceId(invoiceEntity.getInvoiceId());
    		for (InvoiceItemEntity item : itemEntities) {
    			invoiceItemRepository.delete(item);
    			
    		}
    	}
    	invoiceRepository.deleteById(invoiceId);
    	
    }
    
    
    @Override
    @Transactional
    public Invoice updateInvoice(String invoiceId, InvoiceDetails invoiceDetails) {
        Invoice existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice with ID " + invoiceId + " not found."));
        existingInvoice.setInvoiceId(invoiceId);
        existingInvoice.setInvoiceNumber(invoiceDetails.getInvoiceNumber());
        existingInvoice.setCustomerId(invoiceDetails.getCustomerId());
        existingInvoice.setEmployeeId(invoiceDetails.getEmployeeId());
        existingInvoice.setTotalAmount(invoiceDetails.getTotalAmount());
        existingInvoice.setDueDate(invoiceDetails.getDueDate());
        existingInvoice.setInvoiceDate(invoiceDetails.getInvoiceDate());

        List<InvoiceItemEntity> existingItems = invoiceItemRepository.findByInvoiceId(invoiceId);
        invoiceItemRepository.deleteAll(existingItems);
        if (invoiceDetails.getItems() != null && !invoiceDetails.getItems().isEmpty()) {
            for (InvoiceItem itemVo : invoiceDetails.getItems()) {
                InvoiceItemEntity itemEntity = new InvoiceItemEntity();
                itemEntity.setItemId(UUID.randomUUID().toString().substring(0, 30));
                itemEntity.setInvoiceId(existingInvoice.getInvoiceId());
                try {
                    itemEntity.setProductId(Integer.parseInt(itemVo.getProductId()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Product ID format: " + itemVo.getProductId(), e);
                }
                itemEntity.setQuantity(itemVo.getQuantity());
                itemEntity.setPricePerUnit(itemVo.getPricePerUnit());
                itemEntity.setTotalCost(itemVo.getTotalCost());
                itemEntity.setCustomerId(existingInvoice.getCustomerId());
                itemEntity.setEmpId(existingInvoice.getEmployeeId());
                invoiceItemRepository.save(itemEntity);
            }
        }
        return invoiceRepository.save(existingInvoice);
    }
}

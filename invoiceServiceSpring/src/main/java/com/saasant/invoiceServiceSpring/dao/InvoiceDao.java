package com.saasant.invoiceServiceSpring.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saasant.invoiceServiceSpring.entity.Invoice;
import com.saasant.invoiceServiceSpring.entity.InvoiceItemEntity;
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
                itemEntity.setInvoiceNumber(savedInvoiceEntity.getInvoiceNumber());
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
        // The interface requires returning the Invoice entity
        return savedInvoiceEntity;
    }
    
    @Override
    public Optional<InvoiceDetails> findInvoiceDetailsByInvoiceNumber(String invoiceNumber) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceNumber);
        if (invoiceOpt.isPresent()) {
            Invoice invoiceEntity = invoiceOpt.get();
            List<InvoiceItemEntity> itemEntities = invoiceItemRepository.findByInvoiceNumber(invoiceEntity.getInvoiceNumber());
            
            // Convert entities to DTO
            InvoiceDetails invoiceDetailsDto = modelMapper.map(invoiceEntity, InvoiceDetails.class);
            List<InvoiceItem> itemVos = itemEntities.stream()
                .map(this::convertToInvoiceItemVo) 
                .collect(Collectors.toList());
            invoiceDetailsDto.setItems(itemVos);
            
            return Optional.of(invoiceDetailsDto);
        }
        return Optional.empty();
    }


}

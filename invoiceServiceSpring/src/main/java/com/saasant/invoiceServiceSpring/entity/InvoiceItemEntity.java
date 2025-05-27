package com.saasant.invoiceServiceSpring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "invoiceitems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemEntity {

    @Id
    private String itemId;
    private String invoiceNumber;
    private int productId;
    private float quantity;
    private float pricePerUnit;
    private float totalCost;
    private String customerId;
    private String empId;
}
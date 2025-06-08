package com.saasant.customerServiceSpring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saasant.customerServiceSpring.vo.CustomerDetails;
import com.saasant.customerServiceSpring.entity.Customers;

public interface CustomerServiceInterface {
	
	CustomerDetails addCustomer(CustomerDetails customer);
	CustomerDetails updateCustomer(String customerId, CustomerDetails customer);
	void deleteCustomer(String customerId);
	CustomerDetails getCustomerById(String CustomerId);
	//List<CustomerDetails> getAllCustomers();
	
	Page<CustomerDetails> getAllCustomers(Pageable pageable); 
    Page<CustomerDetails> searchCustomers(String searchTerm, Pageable pageable);
    List<CustomerDetails> fetchAllCustomers();
}

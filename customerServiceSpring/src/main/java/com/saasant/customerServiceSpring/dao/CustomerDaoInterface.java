package com.saasant.customerServiceSpring.dao;
import com.saasant.customerServiceSpring.vo.CustomerDetails;
import com.saasant.customerServiceSpring.entity.Customers;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface CustomerDaoInterface {
	
	boolean addCustomer(CustomerDetails customer);
	CustomerDetails getCustomerById(String customerId);
	boolean updateCustomer(CustomerDetails customer);
	boolean deleteCustomer(String customerId);
	Page<CustomerDetails> getAllCustomers(Pageable pageable);
	Page<CustomerDetails> searchCustomers(String searchTerm, Pageable pageable);
	List<CustomerDetails> fetchAllCustomers();

}

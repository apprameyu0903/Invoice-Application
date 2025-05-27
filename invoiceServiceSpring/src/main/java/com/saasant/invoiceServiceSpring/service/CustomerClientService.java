package com.saasant.invoiceServiceSpring.service;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.saasant.invoiceServiceSpring.vo.*;
import org.slf4j.LoggerFactory;
import com.saasant.invoiceServiceSpring.exception.CustomerNotFoundException;

@Service
public class CustomerClientService {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(CustomerClientService.class);

	@Value("${customer.service.baseurl}")
	private String customerServiceBaseUrl;
	
	@Autowired
	public RestTemplate restTemplate;
	
	public Optional<CustomerDetails> getCustomerById(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            log.warn("Attempted to fetch customer with null or empty ID.");
            throw new CustomerNotFoundException("Customer ID cannot be null or empty.");
        }

        String url = customerServiceBaseUrl + "/" + customerId;
        log.info("Attempting to fetch customer details from URL: {}", url);

        try {
            ResponseEntity<CustomerDetails> response = restTemplate.getForEntity(url, CustomerDetails.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully fetched customer details for ID: {}", customerId);
                return Optional.of(response.getBody());
            } else {
                log.warn("Received non-OK status ({}) or empty body when fetching customer ID: {}", response.getStatusCode(), customerId);
                throw new CustomerNotFoundException("Customer ID cannot be null or empty.");
            }
        } catch (RestClientException ex) {
            // Handles other client-side errors (e.g., service unavailable, connection refused)
            log.error("Error calling Customer Service for ID: {}. URL: {}. Error: {}", customerId, url, ex.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            // Catch any other unexpected exceptions
            log.error("Unexpected error while fetching customer ID: {}. URL: {}. Error: {}", customerId, url, ex.getMessage(), ex);
            return Optional.empty();
        }
    }
	
    public boolean validateCustomerExists(String customerId) {
        return getCustomerById(customerId).isPresent();
    }
	
	
	
}

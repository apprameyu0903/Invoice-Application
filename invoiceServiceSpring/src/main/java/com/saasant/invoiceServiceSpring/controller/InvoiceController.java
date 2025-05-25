package com.saasant.invoiceServiceSpring.controller;

import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.saasant.invoiceServiceSpring.service.CustomerClientService;
import com.saasant.invoiceServiceSpring.service.EmployeeClientService;
import com.saasant.invoiceServiceSpring.service.ProductClientService;
import com.saasant.invoiceServiceSpring.vo.CustomerDetails;
import com.saasant.invoiceServiceSpring.vo.Employee;
import com.saasant.invoiceServiceSpring.vo.InvoiceDetails;
import com.saasant.invoiceServiceSpring.vo.InvoiceItem;
import com.saasant.invoiceServiceSpring.vo.Product;


@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
	
	private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);
	
	@Autowired
	CustomerClientService customerClientService;
	
	@Autowired
	EmployeeClientService employeeClientService;
	
	@Autowired
	ProductClientService productClientService;
	
	
	@GetMapping("/test/customer/{customerId}")
    public ResponseEntity<String> testGetCustomer(@PathVariable String customerId) {
        log.info("Test endpoint: Attempting to fetch customer with ID: {}", customerId);
        if (customerId == null || customerId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Test endpoint: Customer ID cannot be empty.");
        }
        // Using CustomerDetailsVO as per the CustomerClientService in the Canvas
        Optional<CustomerDetails> customerOpt = customerClientService.getCustomerById(customerId);
        if (customerOpt.isPresent()) {
            CustomerDetails customer = customerOpt.get();
            // Assuming CustomerDetailsVO has getCustomerName() or similar
            String customerName = customer.getCustomerName();
            log.info("Test endpoint: Successfully fetched customer: {} - {}", customerId, customerName);
            return ResponseEntity.ok("Test successful! Found customer: " + customerName + " (ID: " + customerId + ")");
        } else {
            log.warn("Test endpoint: Customer not found with ID: {}", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test failed: Customer not found with ID: " + customerId);
        }
    }
	
	@PostMapping
    public ResponseEntity<String> createInvoice(@RequestBody InvoiceDetails invoiceDetails) {
        log.info("Received request to create invoice for customer ID: {}", invoiceDetails.getCustomerId());

        // 1. Validate Customer
        String customerId = invoiceDetails.getCustomerId();
        if (customerId == null || customerId.trim().isEmpty()) {
            log.warn("Customer ID is missing in the request.");
            return ResponseEntity.badRequest().body("Customer ID is required.");
        }
        // The CustomerClientService from Canvas returns Optional<CustomerDetailsVO>
        Optional<CustomerDetails> customerOpt = customerClientService.getCustomerById(customerId);
        if (customerOpt.isEmpty()) {
            log.warn("Customer validation failed for ID: {}. Customer not found or error in service call.", customerId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Customer ID: " + customerId + ". Customer not found or unable to validate.");
        }
        CustomerDetails validatedCustomer = customerOpt.get();
        log.info("Customer {} validated successfully: {}", customerId, validatedCustomer.getCustomerName());

        // 2. Validate Employee
        String employeeId = invoiceDetails.getEmployeeId();
        if (employeeId == null || employeeId.trim().isEmpty()) {
            log.warn("Employee ID is missing in the request.");
            return ResponseEntity.badRequest().body("Employee ID is required.");
        }
        Optional<Employee> employeeOpt = employeeClientService.getEmployeeById(employeeId); // Assumes this service and VO exist
        if (employeeOpt.isEmpty()) {
            log.warn("Employee validation failed for ID: {}. Employee not found or error in service call.", employeeId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Employee ID: " + employeeId + ". Employee not found or unable to validate.");
        }
        Employee validatedEmployee = employeeOpt.get();
        log.info("Employee {} validated successfully: {}", employeeId, validatedEmployee.getEmpName());


        // 3. Validate Products
        if (invoiceDetails.getItems() == null || invoiceDetails.getItems().isEmpty()) {
            log.warn("Invoice items are missing or empty.");
            return ResponseEntity.badRequest().body("Invoice must contain at least one item.");
        }
        for (InvoiceItem item : invoiceDetails.getItems()) {
            if (item.getProductId() == null || item.getProductId().trim().isEmpty()) {
                log.warn("Product ID is missing for an item.");
                return ResponseEntity.badRequest().body("Product ID is missing for an item.");
            }
            int productIdInt;
            try {
                productIdInt = Integer.parseInt(item.getProductId()); // Assuming product ID in item is String, but ProductService uses int
            } catch (NumberFormatException e) {
                log.warn("Invalid Product ID format for item: {}", item.getProductId());
                return ResponseEntity.badRequest().body("Invalid Product ID format: " + item.getProductId());
            }

            // Assumes ProductClientService.isValidProduct and getProductById exist and use int for product ID
            if (!productClientService.isValidProduct(productIdInt)) {
               log.warn("Invalid product ID {} found in invoice items (not in cache or service).", productIdInt);
               return ResponseEntity.badRequest().body("Invalid product ID in items: " + productIdInt);
            }
            Optional<Product> productOpt = productClientService.getProductById(productIdInt);
            if(productOpt.isPresent()){
                Product validatedProduct = productOpt.get();
                log.info("Product {} (ID: {}) validated. Price from service: {}", validatedProduct.getName(), productIdInt, validatedProduct.getPrice());
            } else {
                log.warn("Product ID {} was marked valid but details not found. Possible inconsistency.", productIdInt);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating product details for ID: " + productIdInt);
            }
        }
        log.info("All product items validated successfully.");


        // 4. Set Invoice Number and Dates
        if (invoiceDetails.getInvoiceNumber() == null || invoiceDetails.getInvoiceNumber().trim().isEmpty()) {
            invoiceDetails.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
            log.info("Generated new invoice number: {}", invoiceDetails.getInvoiceNumber());
        }
        if (invoiceDetails.getInvoiceDate() == null) {
            invoiceDetails.setInvoiceDate(java.time.LocalDateTime.now());
        }
        if (invoiceDetails.getDueDate() == null) {
            invoiceDetails.setDueDate(java.time.LocalDate.now().plusDays(30));
        }

        // 5. Calculate Total Amount (ensure this uses the potentially updated item prices if you override them)
        invoiceDetails.calculateTotalAmount();
        log.info("Calculated total bill amount: {}", invoiceDetails.getBillAmount());

        // 6. Persist Invoice (Placeholder for actual persistence logic using an InvoiceRepository)
        try {
            //InvoiceEntity savedInvoice = invoiceRepository.save(convertToEntity(invoiceDetails));
            log.info("Invoice processing complete (persistence to be implemented) for invoice number: {}", invoiceDetails.getInvoiceNumber());
            String responseMessage = String.format("Invoice %s created successfully for customer %s, by employee %s. Total: %.2f. %d item(s) processed.",
                    invoiceDetails.getInvoiceNumber(),
                    validatedCustomer.getCustomerName(),
                    validatedEmployee.getEmpName(),
                    invoiceDetails.getBillAmount(),
                    invoiceDetails.getItems().size()
                    );
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);

        } catch (Exception e) {
            log.error("Error during invoice processing for customer {}: {}", customerId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the invoice.");
        }
    }
	
	
	
	

	

}

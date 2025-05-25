package com.saasant.invoiceServiceSpring.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.saasant.invoiceServiceSpring.vo.Employee;

import java.util.Optional;

import org.slf4j.Logger;

@Service
public class EmployeeClientService {

	
	private static final Logger log = LoggerFactory.getLogger(EmployeeClientService.class);
	
	@Value("${employee.service.baseurl}")
	private String employeeServiceBaseUrl;
	
	@Autowired
	public RestTemplate restTemplate;
	
	public Optional<Employee> getEmployeeById(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            log.warn("Attempted to fetch employee with null or empty ID.");
            return Optional.empty();
        }

        String url = employeeServiceBaseUrl + "/" + employeeId;
        log.info("Attempting to fetch employee details from URL: {}", url);

        try {
            ResponseEntity<Employee> response = restTemplate.getForEntity(url, Employee.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully fetched employee details for ID: {}", employeeId);
                return Optional.of(response.getBody());
            } else {
                log.warn("Received non-OK status ({}) or empty body when fetching employee ID: {}", response.getStatusCode(), employeeId);
                return Optional.empty();
            }
        } catch (HttpClientErrorException.NotFound ex) {
            log.info("Employee not found with ID: {}. URL: {}", employeeId, url);
            return Optional.empty();
        } catch (RestClientException ex) {
            log.error("Error calling Employee Service for ID: {}. URL: {}. Error: {}", employeeId, url, ex.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            log.error("Unexpected error while fetching employee ID: {}. URL: {}. Error: {}", employeeId, url, ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}

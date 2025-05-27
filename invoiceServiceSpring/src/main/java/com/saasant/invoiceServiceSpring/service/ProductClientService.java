package com.saasant.invoiceServiceSpring.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.saasant.invoiceServiceSpring.exception.EmployeeNotFoundException;
import com.saasant.invoiceServiceSpring.exception.ProductNotFoundException;
import com.saasant.invoiceServiceSpring.vo.Product;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;

@Service
public class ProductClientService {
	
	private static final Logger log = LoggerFactory.getLogger(ProductClientService.class);
	
	@Autowired
	public RestTemplate restTemplate;
	
	private final Map<Integer, Product> productCache = new ConcurrentHashMap<>();
	
	
	
	
	@Value("${product.service.baseurl}")
	private String productServiceBaseUrl;
	
    public void loadProductsIntoCache() {
        String url = productServiceBaseUrl;
        log.info("Attempting to load all products into cache from URL: {}", url);
        try {
            ResponseEntity<List<Product>> response = restTemplate.exchange(url,HttpMethod.GET,null,new ParameterizedTypeReference<List<Product>>() {});
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                productCache.clear(); 
                Map<Integer, Product> fetchedProducts = response.getBody().stream()
                        .collect(Collectors.toMap(Product::getProductId, product -> product));
                productCache.putAll(fetchedProducts);
                log.info("Successfully loaded {} products into cache.", productCache.size());
            } else {
                log.warn("Received non-OK status ({}) or empty body when fetching all products. URL: {}", response.getStatusCode(), url);
            }
        } catch (RestClientException ex) {
            log.error("Error calling Product Service to fetch all products. URL: {}. Error: {}", url, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error while fetching all products. URL: {}. Error: {}", url, ex.getMessage(), ex);
        }
    }


    public Product getProductById(int productId) {
        if (productCache.containsKey(productId)) {
            log.debug("Fetching product ID {} from cache.", productId);
            Product product = productCache.get(productId);
            if (product != null) {
                return product;
            }
        }
        log.warn("Product ID {} not found in cache.", productId);
        throw new ProductNotFoundException("Product not found with ID: " + productId);
    }

    public boolean isValidProduct(int productId) {
        boolean exists = productCache.containsKey(productId);
        if(exists) {
            log.debug("Product ID {} found in cache during validation.", productId);
        } else {
            log.warn("Product ID {} not found in cache during validation. Consider reloading cache or checking ProductService.", productId);
        }
        return exists;
    }
	
	

}

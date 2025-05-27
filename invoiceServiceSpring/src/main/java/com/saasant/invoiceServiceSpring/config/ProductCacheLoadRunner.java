package com.saasant.invoiceServiceSpring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.saasant.invoiceServiceSpring.service.ProductClientService;


@Component
public class ProductCacheLoadRunner implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(ProductCacheLoadRunner.class);
	
	@Autowired
    private ProductClientService productClientService;
	
	@Override
    public void run(String... args) throws Exception {
        log.info("CommandLineRunner: Initiating product cache loading...");
        productClientService.loadProductsIntoCache();
        log.info("CommandLineRunner: Product cache loading process completed.");
    }
	
	

}

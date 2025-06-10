package com.saasant.customerServiceSpring.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.saasant.customerServiceSpring.entity.Customers;
import com.saasant.customerServiceSpring.vo.CustomerDetails;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, String> {
	
	@Query("select c FROM Customers c where " +
	           "lower(c.customerId) like lower(concat('%',:searchTerm,'%')) or " +
	           "lower(c.customerName) like lower(concat('%', :searchTerm, '%')) or " +
	           "lower(c.customerMobile) like lower(concat('%', :searchTerm, '%')) or " +
	           "lower(c.customerLocation) like lower(concat('%', :searchTerm, '%'))")
	    Page<Customers> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}

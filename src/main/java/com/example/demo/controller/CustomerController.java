package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dto.Customer;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	private CustomerService service;
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {

		List<Customer> customers = service.findAll();
		log.info("Customers list size= {}", customers.size());

		if (customers.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {

		Customer customer = service.findById(id);
		if (customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			log.info("Customer with id={} not found", id);
			throw new ResourceNotFoundException("Customer with id=" + id + " not found");
		}
	}

	@PostMapping("/customers")
	public ResponseEntity<Object> saveCustomer(@Valid @RequestBody Customer customer) {
		Customer newCustomer = service.saveCustomer(customer);
		if (newCustomer != null) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(newCustomer.getId()).toUri();
			return ResponseEntity.created(location).build();
		} else {
			log.info("Customer returned is NULL - Duplicate Customer found in the database");
			throw new ResourceAlreadyExistsException("Duplicate customer found with same username = "+ customer.getUsername() );
		}
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
		if (service.deleteById(id)) {
			return ResponseEntity.noContent().build();
		} else {
			log.info("Customer with id= {} not found", id);
			throw new ResourceNotFoundException("Customer with id = " + id + " not found");
		}

	}

	@PutMapping("/customers/{id}")
	public ResponseEntity<Object> updateCustomer(@RequestBody Customer updatedCustomer, @PathVariable long id) {
		Customer existingCustomer = service.findById(id);

		if (existingCustomer == null) {
			log.info("Customer with id={} not found", id);
			throw new ResourceNotFoundException("Customer with id=" + id + " not found");
		}
		// Update the specific fields of the existing customer
		if (updatedCustomer.getUsername() != null) {
			existingCustomer.setUsername(updatedCustomer.getUsername());
		}
		if (updatedCustomer.getPassword() != null) {
			existingCustomer.setPassword(updatedCustomer.getPassword());
		}
		if (service.updateCustomer(existingCustomer)) {
			return ResponseEntity.noContent().build();
		} else {
			log.info("Failed to update customer with id={}", id);
			throw new RuntimeException("Failed to update customer with id=" + id);
		}
	}

	@GetMapping("/customers/username/{username}")
	public ResponseEntity<Customer> findCustomerByUsername(@PathVariable String username) {
		Customer customer = service.findByUsername(username);
		if (customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			log.info("Customer with username={} not found", username);
	        throw new ResourceNotFoundException("Customer with username=" + username + " not found");
		}
	}
}

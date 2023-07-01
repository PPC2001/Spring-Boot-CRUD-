package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.Customer;
import com.example.demo.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository repository;

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	public CustomerServiceImpl(CustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Customer> findAll() {
		return repository.findAll();
	}

	@Override
	public Customer findById(long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Customer findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Override
	public Customer saveCustomer(Customer customer) {

		Customer duplicateCustomer = this.findByUsername(customer.getUsername());
		if (duplicateCustomer == null) {
			return repository.save(customer);
		}
		log.info("Customer with username ={} found in database", customer.getUsername());
		return null;
	}

	@Override
	public boolean updateCustomer(Customer customer) {
		log.info("Customer to be updated: id={}", customer.getId());

		Customer customerFromDb = this.findById(customer.getId());

		if (customerFromDb != null) {
			log.info("Customer found in Database **** updating the customer with id={}", customer.getId());
			repository.save(customer);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteById(long id) {
		Optional<Customer> customer = repository.findById(id);

		if (customer.isPresent()) {
			repository.deleteById(id);
			return true;
		} else {
			return false;
		}

	}

}

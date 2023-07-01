package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.Customer;

public interface CustomerService {

	public List<Customer> findAll();

	public Customer findById(long id);

	public Customer findByUsername(String username);

	public Customer saveCustomer(Customer customer);

	public boolean updateCustomer(Customer customer);

	public boolean deleteById(long id);
}

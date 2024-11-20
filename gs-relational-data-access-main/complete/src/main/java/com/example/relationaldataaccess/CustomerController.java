package com.example.relationaldataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// GET all customers
	@GetMapping
	public List<Customer> getAllCustomers() {
		return jdbcTemplate.query(
				"SELECT id, first_name, last_name FROM customers",
				(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
		);
	}

	// GET specific customer
	@GetMapping("/{id}")
	public Customer getCustomerById(@PathVariable Long id) {
		return jdbcTemplate.queryForObject(
				"SELECT id, first_name, last_name FROM customers WHERE id = ?",
				(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")),
				id
		);
	}
}

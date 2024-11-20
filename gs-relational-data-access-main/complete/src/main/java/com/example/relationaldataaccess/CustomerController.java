package com.example.relationaldataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

	// GET specific customer by ID
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		try {
			Customer customer = jdbcTemplate.queryForObject(
					"SELECT id, first_name, last_name FROM customers WHERE id = ?",
					(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")),
					id
			);
			return ResponseEntity.ok(customer);
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// GET customers by first name
	@GetMapping("/name/{firstName}")
	public ResponseEntity<List<Customer>> getCustomersByFirstName(@PathVariable String firstName) {
		List<Customer> customers = jdbcTemplate.query(
				"SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
				(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")),
				firstName
		);

		if (customers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(customers);
	}
}

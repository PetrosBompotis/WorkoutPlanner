package com.petrosb.WorkoutPlanner.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerDataAccessService {
    private final CustomerRepository customerRepository;

    public CustomerDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> selectCustomerByID(Long id) {
        return customerRepository.findById(id);
    }

    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    public void deleteCustomerById(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    public boolean existsPersonWithId(Long id) {
        return customerRepository.existsCustomerById(id);
    }

    public void updateCustomerById(Customer customer) {
        customerRepository.save(customer);
    }

    public Optional<Customer> selectUserByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }
}

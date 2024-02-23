package com.petrosb.WorkoutPlanner.customer;

import com.petrosb.WorkoutPlanner.exception.DuplicateResourceException;
import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDataAccessService customerDataAccessService;

    public CustomerService(CustomerDataAccessService customerDataAccessService) {
        this.customerDataAccessService = customerDataAccessService;
    }

    public List<Customer> getAllCustomers(){
        return customerDataAccessService.selectAllCustomers();
    }

    public Customer getCustomer(Long id){
        return customerDataAccessService.selectCustomerByID(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){

        String email = customerRegistrationRequest.email();
        //check if email exists
        if(customerDataAccessService.existsPersonWithEmail(email)){
            throw new DuplicateResourceException("email already taken");
        }

        //otherwise add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.password(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );
        customerDataAccessService.insertCustomer(customer);

    }

    public void deleteCustomerById(Long custmerId){

        //check if id exists
        if(!customerDataAccessService.existsPersonWithId(custmerId)){
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(custmerId));
        }

        //otherwise remove
        customerDataAccessService.deleteCustomerById(custmerId);

    }

    public void updateCustomer(CustomerUpdateRequest updateRequest, Long customerId){

        Customer customer = customerDataAccessService.selectCustomerByID(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(customerId)
                ));
        boolean changes = false;
        //check if attributes need change exists
        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){
            if(customerDataAccessService.existsPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        //otherwise update

        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        customerDataAccessService.updateCustomerById(customer);
    }
}

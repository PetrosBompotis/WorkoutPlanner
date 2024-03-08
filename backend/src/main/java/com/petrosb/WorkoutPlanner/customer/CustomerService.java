package com.petrosb.WorkoutPlanner.customer;

import com.petrosb.WorkoutPlanner.exception.DuplicateResourceException;
import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import com.petrosb.WorkoutPlanner.role.Role;
import com.petrosb.WorkoutPlanner.role.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerDataAccessService customerDataAccessService;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public CustomerService(CustomerDataAccessService customerDataAccessService, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.customerDataAccessService = customerDataAccessService;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<CustomerDTO> getAllCustomers(){

        return customerDataAccessService.selectAllCustomers().stream().map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long id){
        return customerDataAccessService.selectCustomerByID(id).map(customerDTOMapper)
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

        Role userRole = roleRepository.findByAuthority("USER").get();
        List<Role> authorities = new ArrayList<>();
        authorities.add(userRole);

        //otherwise add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender(),
                authorities
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

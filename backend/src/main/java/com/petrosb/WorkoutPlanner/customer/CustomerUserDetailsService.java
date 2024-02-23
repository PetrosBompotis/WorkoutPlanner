package com.petrosb.WorkoutPlanner.customer;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private final CustomerDataAccessService customerDataAccessService;

    public CustomerUserDetailsService(CustomerDataAccessService customerDataAccessService) {
        this.customerDataAccessService = customerDataAccessService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerDataAccessService.selectUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                "Username: " + username + " not found!"));
    }
}

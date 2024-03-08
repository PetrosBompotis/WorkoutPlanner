package com.petrosb.WorkoutPlanner;

import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.CustomerRepository;
import com.petrosb.WorkoutPlanner.role.Role;
import com.petrosb.WorkoutPlanner.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.petrosb.WorkoutPlanner.customer.Gender.MALE;

@SpringBootApplication
public class WorkoutPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkoutPlannerApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncode){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			List<Role> roles= new ArrayList<>();
			roles.add(adminRole);

			Customer admin = new Customer(
					"admin",
					"admin@gmail.com",
					passwordEncode.encode("password"),
					25,
					MALE,
					roles);

			customerRepository.save(admin);
		};
	}

}

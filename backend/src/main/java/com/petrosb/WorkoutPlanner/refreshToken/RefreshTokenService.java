package com.petrosb.WorkoutPlanner.refreshToken;

import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.CustomerRepository;
import com.petrosb.WorkoutPlanner.exception.TokenRefreshException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerRepository customerRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, CustomerRepository customerRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.customerRepository = customerRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(userId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByCustomer(customer);
            if (optionalRefreshToken.isPresent()) {
                RefreshToken existingToken = optionalRefreshToken.get();
                existingToken.setToken(UUID.randomUUID().toString());
                existingToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
                return refreshTokenRepository.save(existingToken);
            } else {
                RefreshToken newRefreshToken = new RefreshToken();
                newRefreshToken.setCustomer(customer);
                newRefreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
                newRefreshToken.setToken(UUID.randomUUID().toString());
                return refreshTokenRepository.save(newRefreshToken);
            }
        } else {
            // Handle case where customer with given userId does not exist
            throw new RuntimeException("Customer with ID " + userId + " not found");
        }
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
}

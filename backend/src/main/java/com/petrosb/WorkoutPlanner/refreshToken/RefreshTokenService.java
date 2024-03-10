package com.petrosb.WorkoutPlanner.refreshToken;

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
    private Long refreshTokenDurationMs = 86400000L;
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
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setCustomer(customerRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
}

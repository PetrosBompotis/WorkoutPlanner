package com.petrosb.WorkoutPlanner.auth;

import com.petrosb.WorkoutPlanner.exception.TokenRefreshException;
import com.petrosb.WorkoutPlanner.jwt.JWTUtil;
import com.petrosb.WorkoutPlanner.refreshToken.RefreshToken;
import com.petrosb.WorkoutPlanner.refreshToken.RefreshTokenService;
import com.petrosb.WorkoutPlanner.refreshToken.TokenRefreshRequest;
import com.petrosb.WorkoutPlanner.refreshToken.TokenRefreshResponse;
import com.petrosb.WorkoutPlanner.role.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;

    public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, JWTUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getCustomer)
                .map(user -> {
                    String token = "";
                    String refreshToken = "";
                    boolean hasAdminRole = user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
                    if (hasAdminRole){
                        token = jwtUtil.issueToken(user.getEmail(),"ROLE_ADMIN");
                    }else{
                        token = jwtUtil.issueToken(user.getEmail(),"ROLE_USER");
                    }
                    refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
                    return ResponseEntity.ok(new TokenRefreshResponse(token, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}

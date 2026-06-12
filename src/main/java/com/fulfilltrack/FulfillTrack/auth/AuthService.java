package com.fulfilltrack.FulfillTrack.auth;

import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialEntity;
import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialRepository;
import com.fulfilltrack.FulfillTrack.auth.dto.AuthRequest;
import com.fulfilltrack.FulfillTrack.auth.dto.AuthResponse;
import com.fulfilltrack.FulfillTrack.auth.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredencialRepository credencialRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse authenticate(AuthRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.username(), input.password())
        );
        CredencialEntity user = credencialRepository.findByUsername(input.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        credencialRepository.save(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        CredencialEntity user =
                credencialRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("El refresh token no coincide");
        }
        if (!jwtService.validateRefreshToken(refreshToken, user)) {
            throw new IllegalArgumentException("El refresh token expiró o es inválido");
        }
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("La cuenta está deshabilitada");
        }
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(newRefreshToken);
        credencialRepository.save(user);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}

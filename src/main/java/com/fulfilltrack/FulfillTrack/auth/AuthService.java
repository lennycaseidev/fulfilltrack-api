package com.fulfilltrack.FulfillTrack.auth;

import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialEntity;
import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialRepository;
import com.fulfilltrack.FulfillTrack.auth.dto.AuthRequest;
import com.fulfilltrack.FulfillTrack.auth.dto.AuthResponse;
import com.fulfilltrack.FulfillTrack.auth.jwt.JwtService;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.UsuarioEmpresaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredencialRepository credencialRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioEmpresaRepository usuarioEmpresaRepository;

    @Transactional
    public AuthResponse authenticate(AuthRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.username(), input.password())
        );
        CredencialEntity user = credencialRepository.findByUsername(input.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String accessToken = jwtService.generateToken(user, buildExtraClaims(user));
        String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        credencialRepository.save(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    private Map<String, Object> buildExtraClaims(CredencialEntity credencial) {
        Map<String, Object> claims = new HashMap<>();
        if (credencial.getUsuario() != null) {
            usuarioEmpresaRepository.findByUsuario(credencial.getUsuario())
                    .ifPresent(ue -> claims.put("empresaUuid", ue.getEmpresa().getUuid().toString()));
        }
        return claims;
    }

    @Transactional
    public AuthResponse refreshAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        CredencialEntity user =
                credencialRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token does not match");
        }
        if (!jwtService.validateRefreshToken(refreshToken, user)) {
            throw new IllegalArgumentException("Refresh token expired or invalid");
        }
        String newAccessToken = jwtService.generateToken(user, buildExtraClaims(user));
        String newRefreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(newRefreshToken);
        credencialRepository.save(user);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}

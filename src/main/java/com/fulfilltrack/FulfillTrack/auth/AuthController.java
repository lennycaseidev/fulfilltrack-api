package com.fulfilltrack.FulfillTrack.auth;

import com.fulfilltrack.FulfillTrack.auth.dto.AuthRequest;
import com.fulfilltrack.FulfillTrack.auth.dto.AuthResponse;
import com.fulfilltrack.FulfillTrack.auth.dto.NewAccountRequest;
import com.fulfilltrack.FulfillTrack.auth.dto.RefreshTokenRequest;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioService;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/ingresar")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @PostMapping("/registrarse")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody NewAccountRequest newAccountRequest) {
        return new ResponseEntity<>(usuarioService.guardar(newAccountRequest), HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request.refreshToken()));
    }
}
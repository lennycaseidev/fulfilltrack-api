package com.fulfilltrack.FulfillTrack.auth.credenciales;

import com.fulfilltrack.FulfillTrack.common.exception.UsuarioNoEncontradoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredencialRepository credencialRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return credencialRepository.findByUsername(username).orElseThrow(()-> new UsuarioNoEncontradoException("no se ha encontrado el usuario"));
    }
}

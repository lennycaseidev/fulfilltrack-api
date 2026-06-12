package com.fulfilltrack.FulfillTrack.auth.config;

import com.fulfilltrack.FulfillTrack.auth.permisos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        crearPermisosIfNotExist();
        crearRolesIfNotExist();
    }

    private void crearPermisosIfNotExist() {
        Arrays.stream(Permisos.values()).forEach(p -> {
            if (permisoRepository.findByPermiso(p).isEmpty()) {
                permisoRepository.save(PermisoEntity.builder().permiso(p).build());
            }
        });
    }

    private void crearRolesIfNotExist() {
        crearRolConPermisos(Roles.ROLE_ADMIN, Permisos.values());

        crearRolConPermisos(Roles.ROLE_OPERADOR,
                Permisos.VER_PRODUCTOS, Permisos.VER_PRODUCTOS_EMPRESA, Permisos.VER_STOCK, Permisos.GESTIONAR_STOCK,
                Permisos.VER_PEDIDOS, Permisos.ACTUALIZAR_ESTADO_PEDIDO,
                Permisos.VER_DESPACHOS, Permisos.CREAR_DESPACHO,
                Permisos.VER_USUARIOS
        );

        crearRolConPermisos(Roles.ROLE_EMPRESA,
                Permisos.VER_PRODUCTOS,
                Permisos.VER_PEDIDOS, Permisos.CREAR_PEDIDO,
                Permisos.VER_LIQUIDACIONES
        );

        if (rolRepository.findByRol(Roles.ROLE_PENDIENTE).isEmpty()) {
            rolRepository.save(new RolEntity(Roles.ROLE_PENDIENTE));
        }
    }

    private void crearRolConPermisos(Roles rolEnum, Permisos... permisosList) {
        if (rolRepository.findByRol(rolEnum).isEmpty()) {
            RolEntity rol = new RolEntity(rolEnum);
            Set<PermisoEntity> permisos = permisoRepository.findByPermisoIn(
                    Arrays.asList(permisosList)
            );
            permisos.forEach(rol::agregarPermiso);
            rolRepository.save(rol);
        }
    }
}
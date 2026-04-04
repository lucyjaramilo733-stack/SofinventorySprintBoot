package com.sofinventory;

import com.sofinventory.entity.*;
import com.sofinventory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos del sistema.
 * Se ejecuta al iniciar la aplicación para cargar información básica
 * como tipos de documento, roles, permisos y usuario administrador.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final RoleRepository roleRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        cargarTiposDocumento();
        cargarRoles();
        cargarPermisos();
        cargarAdminInicial();
    }
    /// Carga tipos de documento si no existen
    private void cargarTiposDocumento() {
        if (tipoDocumentoRepository.count() == 0) {
            tipoDocumentoRepository.save(new TipoDocumento(null, "CC", "Cédula de Ciudadanía"));
            tipoDocumentoRepository.save(new TipoDocumento(null, "NIT", "Número de Identificación Tributaria"));
            tipoDocumentoRepository.save(new TipoDocumento(null, "CE", "Cédula de Extranjería"));
            tipoDocumentoRepository.save(new TipoDocumento(null, "TI", "Tarjeta de Identidad"));
            System.out.println("✅ Tipos de documento cargados");
        }
    }
    /// Carga roles básicos del sistema
    private void cargarRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN", "Administrador del sistema"));
            roleRepository.save(new Role(null, "USER", "Usuario estándar"));
            System.out.println("✅ Roles cargados");
        }
    }
    ///Carga permisos iniciales organizados por módulos.
    private void cargarPermisos() {
        if (permisoRepository.count() == 0) {
            String[][] permisos = {
                    {"usuarios.ver",       "Ver usuarios",        "USUARIOS"},
                    {"usuarios.crear",     "Crear usuarios",      "USUARIOS"},
                    {"usuarios.editar",    "Editar usuarios",     "USUARIOS"},
                    {"usuarios.eliminar",  "Eliminar usuarios",   "USUARIOS"},
                    {"dashboard.ver",      "Ver dashboard",       "DASHBOARD"},
                    {"inventario.ver",     "Ver inventario",      "INVENTARIO"},
                    {"inventario.crear",   "Crear inventario",    "INVENTARIO"},
                    {"inventario.editar",  "Editar inventario",   "INVENTARIO"},
                    {"inventario.eliminar","Eliminar inventario", "INVENTARIO"},
                    {"reportes.ver",       "Ver reportes",        "REPORTES"}
            };

            for (String[] p : permisos) {
                permisoRepository.save(new Permiso(null, p[0], p[1], p[2], true));
            }
            System.out.println("✅ Permisos cargados");
        }
    }
    ///Crea un usuario administrador por defecto si no existen usuarios.
    private void cargarAdminInicial() {
        if (usuarioRepository.count() == 0) {
            Role rolAdmin = roleRepository.findByNombre("ADMIN").orElseThrow();
            TipoDocumento cc = tipoDocumentoRepository.findByCodigo("CC").orElseThrow();

            Usuario admin = new Usuario();
            admin.setNombreCompleto("Administrador Sistema");
            admin.setEmail("admin@sofinventory.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin123"));
            admin.setNumeroDocumento("1234567890");
            admin.setRol(rolAdmin);
            admin.setTipoDocumento(cc);
            admin.setActive(true);

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado");
        }
    }
}

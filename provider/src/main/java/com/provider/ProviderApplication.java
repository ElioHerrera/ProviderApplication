package com.provider;

import com.provider.entities.*;
import com.provider.repositories.*;
import com.provider.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication

public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);

    }

    //Creación de objetos Inicial
/*
    @Bean
    CommandLineRunner init(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            ComercioRepository comercioRepository,
            EmpresaRepository empresaRepository,
            ProductoRepository productoRepository,
            ListaPrecioRepository listaPrecioRepository
    ) {
        return args -> {



            Permiso permisoAdmin = Permiso.builder().nombrePermiso("Administrador").build();
            Permiso permisoCliente = Permiso.builder().nombrePermiso("Cliente").build();
            Permiso permisoProveedor = Permiso.builder().nombrePermiso("Proveedor").build();

            Rol rolAdmin = Rol.builder().roleEnum(RoleEnum.ADMIN).listaDePermisos(Set.of(permisoAdmin)).build();
            Rol rolClient = Rol.builder().roleEnum(RoleEnum.CLIENT).listaDePermisos(Set.of(permisoCliente)).build();
            Rol rolProvider = Rol.builder().roleEnum(RoleEnum.PROVIDER).listaDePermisos(Set.of(permisoProveedor)).build();

            Set<Rol> listaRolAdmin = new HashSet<>();
            listaRolAdmin.add(rolAdmin);
            Set<Rol> listaRolClient = new HashSet<>();
            listaRolClient.add(rolClient);
            Set<Rol> listaRolProvider = new HashSet<>();
            listaRolProvider.add(rolProvider);

            // Crear usuarios y establecer relaciones con perfiles
            Usuario usuarioElioAdmin = Usuario.builder()
                    .username("admin")
                    .password("dev123e")
                    .email("admin@provider.com")
                    .tipoUsuario(Usuario.TipoUsuario.ADMINISTRADOR)
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpidered(true)
                    .build();

            Usuario usuarioElioProveedor = Usuario.builder()
                    .username("elioprovider")
                    .password("dev123e")
                    .email("provider@provider.com")
                    .tipoUsuario(Usuario.TipoUsuario.PROVEEDOR)
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpidered(true)
                    .build();

            Usuario usuarioElioComerciante = Usuario.builder()
                    .username("vicky")
                    .password("dev123e")
                    .email("vicky@provider.com")
                    .tipoUsuario(Usuario.TipoUsuario.COMERCIANTE)
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpidered(true)
                    .build();


            //Asignamos los roles a los usuarios
            usuarioElioComerciante.setRoles(listaRolClient);
            usuarioElioAdmin.setRoles(listaRolAdmin);
            usuarioElioProveedor.setRoles(listaRolProvider);


            // Guardar usuarios en la base de datos
            usuarioRepository.save(usuarioElioAdmin);
            usuarioRepository.save(usuarioElioProveedor);
            usuarioRepository.save(usuarioElioComerciante);


            // Crear perfiles
            Perfil perfilAdmin = Perfil.builder()
                    .nombre("Admin")
                    .apellido("Admin")
                    .fotoPerfil("default.png")
                    .descripcion("Administrador")
                    .usuario(usuarioElioAdmin)
                    .build();

            Perfil perfilProveedor = Perfil.builder()
                    .nombre("Elio")
                    .apellido("Herrera")
                    .fotoPerfil("default.png")
                    .descripcion("Proveedor")
                    .usuario(usuarioElioProveedor)
                    .build();

            Perfil perfilComerciante = Perfil.builder()
                    .nombre("Victoria")
                    .apellido("Gomez")
                    .fotoPerfil("default.png")
                    .descripcion("Comerciante")
                    .usuario(usuarioElioComerciante)
                    .build();

            perfilRepository.save(perfilAdmin);
            perfilRepository.save(perfilProveedor);
            perfilRepository.save(perfilComerciante);


            // Crear empresa para el proveedor
            Empresa empresaProveedor = Empresa.builder()
                    .nombre("Provider")
                    .rubro("Informática")
                    .telefono("3413101153")
                    .domicilio("Olive 1661")
                    .proveedor(perfilProveedor) // Asignar el perfil del proveedor
                    .build();

            // Guardar empresa en la base de datos
            empresaRepository.save(empresaProveedor);



            Comercio comercioComerciante = Comercio.builder()
                    .nombre("Athletika")
                    .telefono("3416251492")
                    .rubro("Ropa deportiva")
                    .domicilio("Genova 1566")
                    .comerciante(perfilComerciante) // Asignar el perfil del comerciante
                    .build();

            // Guardar comercio en la base de datos
            comercioRepository.save(comercioComerciante);


            // Crear productos para la empresa proveedora
            Producto producto1 = Producto.builder()
                    .nombre("Producto 1")
                    .descripcion("Descripción del Producto 1")
                    .precio(10.00)
                    .empresa(empresaProveedor) // Asignar la empresa proveedora
                    .build();

            Producto producto2 = Producto.builder()
                    .nombre("Producto 2")
                    .descripcion("Descripción del Producto 2")
                    .precio(20.00)
                    .empresa(empresaProveedor) // Asignar la empresa proveedora
                    .build();

            // Guardar productos en la base de datos
            productoRepository.save(producto1);
            productoRepository.save(producto2);

            // Crear lista de precios para la empresa proveedora
            ListaPrecio listaPrecio1 = ListaPrecio.builder()
                    .nombre("Lista de Precios 1")
                    .fecha(new Date())
                    .productos(Arrays.asList(producto1, producto2)) // Agregar los productos a la lista de precios
                    .empresa(empresaProveedor) // Asignar la empresa proveedora
                    .build();


            // Guardar lista de precios en la base de datos
            listaPrecioRepository.save(listaPrecio1);

            // Código para crear comercio y pedidos omitido por simplicidad
            // Crear comercio para el comerciante



        };
    }
    */

}
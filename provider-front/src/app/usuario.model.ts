export interface Usuario {
    id: number;
    email: string;
    username: string;
    tipoUsuario: string;
    perfil: Perfil; // Actualizamos el tipo de perfil a la nueva entidad Perfil
    roles: Rol[];
    enabled: boolean;
}

export interface Perfil {
    id: number;
    nombre: string;
    apellido: string;
    fotoPerfil: string; // Cambiamos el tipo a string para almacenar el nombre del archivo de la foto
    descripcion: string;
    relacionesComerciales: Perfil[];
    empresa: Empresa;
    comercio: Comercio;
    // Otros campos del perfil según la estructura actual
}

export interface Rol {
    id: number;
    roleEnum: string;
    listaDePermisos: string[];
}

export interface Empresa {
    id: number;
    nombre: string;
    rubro: string;
    telefono: string;
    domicilio: string;
    //productos: Producto[];
    //listasPrecios: ListaPrecio[];
    proveedor: Perfil;
}

export interface Comercio {
    id: number;
    nombre: string;
    telefono: string;
    rubro: string;
    domicilio: string;
    comerciante: Perfil;
    proveedores: Perfil[];
    //pedidos: Pedido[];
}

export interface Solicitud {
    id: number;
    solicitante: Perfil;
    solicitado: Perfil;
    fechaSolicitud: Date;
    aceptada: boolean;
}
export interface Usuario {
    id: number;
    email: string;
    username: string;
    tipoUsuario: string;
    perfil: Perfil;
    roles: Rol[];
    enabled: boolean;
}

export interface Perfil {
    idPerfil: number;
    nombre: string;
    apellido: string;
    fotoPerfil: string;
    descripcion: string;
    empresa: Empresa;
    comercio: Comercio;
}

export interface Rol {
    idRol: number;
    roleEnum: string;
    listaDePermisos: string[];
}

export interface Empresa {
    idEmpresa: number;
    nombre: string;
    rubro: string;
    telefono: string;
    domicilio: string;
    listas: ListaPrecios[];
   

}

export interface Producto {
    idProducto: number;
    idUsuario: number;
    fotoProducto: string;
    codigo: number;
    nombre: string;
    descripcion: string;
    lista1: number;
    lista2: number;
    lista3: number;
    editable: boolean; // Para la edición de nombre, descripción, etc.
    enabled: boolean;
    editableImagen: boolean; // Para la edición de la imagen del producto
  }

  export interface ListaPreciosRelacion{
    id: number;
    idEmpresa: number;
    nombre: string;
    }

  export interface ListaPrecios{
  idLista: number;
  nombre: string;
  }

export interface Comercio {
    idComercio: number;
    nombre: string;
    telefono: string;
    rubro: string;
    domicilio: string;
}

export interface Solicitud {
    idSolicitud: number;
    solicitante: PerfilRelacion;
    solicitado: PerfilRelacion;
    fechaSolicitud: Date;
    aceptada: boolean;
}

export interface PerfilRelacion {
    id: number;
    idPerfil: number;
    username: string;
    email: string;
    nombre: string;
    apellido: string;
    fotoPerfil: string;
    comercio: ComercioRelacion;
    empresa: EmpresaRelacion;
}

export interface EmpresaRelacion {
    idEmpresa: number;
    nombre: string;
    rubro: string;
    telefono: string;
    domicilio: string;
}

export interface ComercioRelacion {
    idComercio: number;
    nombre: string;
    telefono: string;
    rubro: string;
    domicilio: string;
    listasAsociadas: ListaPreciosRelacion[];
}


export interface Publicacion {
    idPublicacion: number;
    autor: PerfilRelacion;
    contenido: string;
    fotoPublicacion: string;
    fecha: Date;
}






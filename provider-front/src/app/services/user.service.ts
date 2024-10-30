import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from './helper';

import { Observable, catchError, map, tap, throwError } from 'rxjs';
import { Usuario, PerfilRelacion, Producto } from '../usuario.model'; // Importa el modelo de usuario

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  //StarController
  
  star(): Observable<{ status: string }> {
    return this.http.get<{ status: string }>(`${baseUrl}`);
  }

  //AuthController

  login(credentials: { username: string, password: string }): Observable<any> {
    return this.http.post<any>(`${baseUrl}/api/auth/login`, credentials);
  }
  public signup(user: any): Observable<any> {
    return this.http.post(`${baseUrl}/api/auth/signup`, user);
  }
  public verifyExistingUser(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${baseUrl}/api/auth/verifyexistinguser/${username}`);
  }
  public verifyExistingEmail(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${baseUrl}/api/auth/verifyexistingemail/${email}`);
  }
  public logout(): void {
    localStorage.removeItem('token');  // Eliminar el token
    localStorage.removeItem('currentUser');  // Eliminar los datos del usuario
    localStorage.removeItem('username');
  }

  //UsuarioController

  actualizarUsuario(usuario: Usuario): Observable<any> {
    const url = `${baseUrl}/api/usuarios/actualizar/${usuario.id}`;
    return this.http.put(url, usuario);
  }

  public obtenerProveedores(): Observable<PerfilRelacion[]> {
    return this.http.get<PerfilRelacion[]>(`${baseUrl}/api/usuarios/proveedores`).pipe(
      catchError(this.handleError)
    );
  }

  //Perfil Controller

  buscarProveedores(terminoBusqueda: string): Observable<PerfilRelacion[]> {
    return this.http.get<PerfilRelacion[]>(`${baseUrl}/api/proveedores/buscar?terminoBusqueda=${terminoBusqueda}`);
  }

  public obtenerPerfilUsuario(username: string) {
    return this.http.get<any>(`${baseUrl}/api/perfil/${username}`);
  }
  public obtenerRelacionesComerciales(perfilId: number): Observable<any[]> {
    return this.http.get<any[]>(`${baseUrl}/api/perfiles/${perfilId}/relaciones-comerciales`);
  }
  public obtenerUsuariosConSolicitudesPendientes(perfilId: number): Observable<any[]> {
    return this.http.get<any[]>(`${baseUrl}/api/perfiles/${perfilId}/solicitudes-pendientes`);
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Error al realizar la solicitud:', error);
    return throwError('Hubo un problema con el servidor. Intente de nuevo más tarde.');
  }
  //PrecioController
  asignarListaPrecios(userId: number, clienteId: number, listaId: number): Observable<any> {
    return this.http.post<any>(`${baseUrl}/api/precios/${userId}/asignarLista/${clienteId}/${listaId}`, {});
  }
  //Producto Controller 
  obtenerProductosPorUsuarioId(userId: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${baseUrl}/api/producto/lista/${userId}`);
  }
  crearProducto(userId: number): Observable<Producto> {
    return this.http.post<Producto>(`${baseUrl}/api/producto/crear/${userId}`, {});
  }
  guardarImagenProducto(file: File, userId: number, productoId: number): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId.toString());
    formData.append('productoId', productoId.toString());

    return this.http.post<any>(`${baseUrl}/api/img/product/upload`, formData);
  }
  public guardarImagenlocal(formData: FormData): Observable<any> {
    return this.http.post<any>(`${baseUrl}/api/img/upload/`, formData).pipe(
      catchError(this.handleError)
    );
  }
  obtenerImagenProducto(userId: number, fileName: string): string {
    return `${baseUrl}/api/img/uploads/product/${userId}/${encodeURIComponent(fileName)}`;
  }
  actualizarProducto(producto: Producto): Observable<any> {
    return this.http.put(`${baseUrl}/api/producto/actualizar/${producto.idProducto}`, producto);
  }
  eliminarProducto(productoId: number): Observable<any> {
    return this.http.delete(`${baseUrl}/api/producto/eliminar/${productoId}`);
  }
  alternarHabilitado(id: number, isEnabled: boolean): Observable<any> {
    return this.http.put(`${baseUrl}/api/producto/${id}/interruptor`, { isEnabled });
  }
  obtenerProductosProveedor(userId: number, proveedorId: number): Observable<any[]> {
    return this.http.get<any[]>(`${baseUrl}/api/producto/productosProveedor/${userId}/${proveedorId}`);
  }
  // Verificar si tiene Productos
  tieneProductos(userId: number): Observable<boolean> {
    return this.http.get<boolean>(`${baseUrl}/api/producto/tieneProductos/${userId}`);
  }


  subirImagenPerfil(formData: FormData): Observable<any> {
    return this.http.post(`${baseUrl}/api/img/upload`, formData);
  }


}
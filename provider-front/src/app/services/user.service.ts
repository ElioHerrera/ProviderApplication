import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from './helper';

import { Observable, catchError, map, tap, throwError } from 'rxjs';
import { Usuario , PerfilRelacion , Producto} from '../usuario.model'; // Importa el modelo de usuario

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  //Auth Controller
  public logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('user');
  }
  public login(loginData: { username: string, password: string }) {
    return this.httpClient.post<any>(`${baseUrl}/api/auth/login`, loginData);
  }
  public registrarUsuario(user: any): Observable<any> {
    return this.httpClient.post(`${baseUrl}/api/auth/signup`, user);
  }
  public verificarUsuarioExistente(username: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`${baseUrl}/api/auth/verificarUsuario/${username}`);
  }
  public verificarEmailExistente(email: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`${baseUrl}/api/auth/verificarEmail/${email}`);
  }
  //Usuario Controller
  public actualizarDatosUsuario(id: number) {
    return this.httpClient.get<Usuario>(`${baseUrl}/api/usuarios/actualizar/${id}`);
  }
  public actualizarNombre(userId: number, nuevoNombre: string): Observable<any> {
    const body = { nuevoNombre: nuevoNombre };
    return this.httpClient.put<any>(`${baseUrl}/api/usuarios/${userId}/nombre`, body);
  }
  public actualizarApellido(userId: number, nuevoApellido: string): Observable<any> {
    const body = { nuevoApellido: nuevoApellido };
    return this.httpClient.put<any>(`${baseUrl}/api/usuarios/${userId}/apellido`, body);
  }
  public actualizarDescripcion(userId: number, nuevaDescripcion: string): Observable<any> {
    const body = { nuevaDescripcion: nuevaDescripcion };
    return this.httpClient.put<any>(`${baseUrl}/api/usuarios/${userId}/descripcion`, body);
  }
  public obtenerProveedores(): Observable<PerfilRelacion[]> {
    return this.httpClient.get<PerfilRelacion[]>(`${baseUrl}/api/usuarios/proveedores`).pipe(
      catchError(this.handleError)
    );
  }
  //Perfil Controller
  public obtenerPerfilUsuario(username: string) {
    return this.httpClient.get<any>(`${baseUrl}/api/perfil/${username}`);
  }
  public obtenerRelacionesComerciales(perfilId: number): Observable<any[]> {
    return this.httpClient.get<any[]>(`${baseUrl}/api/perfiles/${perfilId}/relaciones-comerciales`);
  }
    public guardarImagenlocal(formData: FormData): Observable<any> {
    return this.httpClient.post<any>(`${baseUrl}/api/img/upload/`, formData).pipe(
      catchError(this.handleError)
    );
  }
  private handleError(error: HttpErrorResponse) {
    console.error('Error al realizar la solicitud:', error);
    return throwError('Hubo un problema con el servidor. Intente de nuevo más tarde.');
  }
  //Producto Controller

  obtenerProductosPorUsuarioId(userId: number): Observable<Producto[]> {
    return this.httpClient.get<Producto[]>(`${baseUrl}/api/producto/lista/${userId}`);
  }


  crearProducto(userId: number): Observable<Producto> {
    return this.httpClient.post<Producto>(`${baseUrl}/api/producto/crear/${userId}`, {});
  }



  guardarImagenProducto(file: File, userId: number, productoId: number): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId.toString());
    formData.append('productoId', productoId.toString());

    return this.httpClient.post<any>(`${baseUrl}/api/producto/upload`, formData);
  }

  obtenerImagenProducto(userId: number, fileName: string): string {
    return `${baseUrl}/api/producto/uploads/${userId}/${encodeURIComponent(fileName)}`;
  }

  actualizarProducto(producto: Producto): Observable<any> {
    return this.httpClient.put(`${baseUrl}/api/producto/actualizar/${producto.idProducto}`, producto);
  }

  eliminarProducto(productoId: number): Observable<any> {
    return this.httpClient.delete(`${baseUrl}/api/producto/eliminar/${productoId}`);
  }

 
  alternarHabilitado(id: number, isEnabled: boolean): Observable<any> {
    return this.httpClient.put(`${baseUrl}/api/producto/${id}/interruptor`, { isEnabled });
  }

}
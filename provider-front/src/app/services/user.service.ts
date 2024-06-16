import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from './helper';

import { Observable, catchError, map, tap, throwError } from 'rxjs';
import { Usuario } from '../usuario.model'; // Importa el modelo de usuario

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  public login(loginData: { username: string, password: string }) {
    return this.httpClient.post<any>(`${baseUrl}/api/auth/login`, loginData);
  }
  public logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('user');
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
  public obtenerProveedores(): Observable<Usuario[]> {
    return this.httpClient.get<Usuario[]>(`${baseUrl}/api/proveedores`).pipe(
      catchError(this.handleError)
    );
  }
  public obtenerPerfilUsuario(username: string) {
    return this.httpClient.get<any>(`${baseUrl}/api/perfil/${username}`);
  }
  public subirImagenPerfil(formData: FormData): Observable<any> {
    return this.httpClient.post<any>(`${baseUrl}/api/img/upload/`, formData).pipe(
      catchError(this.handleError)
    );
  }
  private handleError(error: HttpErrorResponse) {
    console.error('Error al realizar la solicitud:', error);
    return throwError('Hubo un problema con el servidor. Intente de nuevo más tarde.');
  }
  public obtenerRelacionesComerciales(userId: number): Observable<any[]> {
    return this.httpClient.get<any[]>(`${baseUrl}/api/perfiles/${userId}/relaciones-comerciales`);
  }
      /*
  obtenerProveedores(): Observable<Usuario[]> {
    return this.httpClient.get<Usuario[]>(`${baseUrl}/api/proveedores`).pipe(
      tap(response => console.log('Respuesta de proveedores:', response)), // Agrega un log aquí
      catchError(this.handleError)
    );
  */
}
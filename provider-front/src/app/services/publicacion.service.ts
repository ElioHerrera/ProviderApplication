import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import baseUrl from './helper';

@Injectable({
  providedIn: 'root'
})
export class PublicacionService {
  constructor(private http: HttpClient) { }

  crearPublicacion(file: File, userId: number, descripcion: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId.toString());
    formData.append('descripcion', descripcion);

    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.http.post<any>(`${baseUrl}/api/publicaciones/crear`, formData, { headers })
      .pipe(
        catchError(this.handleError)
      );
  }

  obtenerImgPublicacion(userId: number, fileName: string): string {
    return `${baseUrl}/api/publicaciones/uploads/product/${userId}/${encodeURIComponent(fileName)}`;
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Error al realizar la solicitud:', error);
    return throwError('Error al realizar la solicitud; por favor, inténtelo de nuevo más tarde.');
  }

  obtenerPublicacionesPorPerfilyAsociados(userId: number) {
    return this.http.get<any>(`${baseUrl}/api/publicaciones/${userId}`);
  }

  obtenerPublicacionesDelPerfil(userId: number) {
    return this.http.get<any>(`${baseUrl}/api/publicaciones/id/${userId}`);
  }

  eliminarPublicacion(publicacionId: number): Observable<any> {
    return this.http.delete<any>(`${baseUrl}/api/publicaciones/${publicacionId}/eliminar`);
  }

  darLike(publicacionId: number) {
    return this.http.post<any>(`${baseUrl}/api/publicaciones/${publicacionId}/like`, null);
  }
}
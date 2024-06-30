import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import  baseUrl from './helper';


@Injectable({
  providedIn: 'root'
})
export class SolicitudService {

  constructor(private httpClient: HttpClient) { }

  private iconActivo: number = 0;

  enviarSolicitudAProveedor(perfilUsuarioId: number, perfilProveedorId: number): Observable<any> {
    return this.httpClient.post<any>(`${baseUrl}/api/solicitudes/enviar/${perfilUsuarioId}/${perfilProveedorId}`, { perfilUsuarioId, perfilProveedorId });
  }
  verificarSolicitudEnviada(perfilUsuarioId: number, perfilProveedorId: number): Observable<{ exists: boolean }> {
    return this.httpClient.get<{ exists: boolean }>(`${baseUrl}/api/solicitudes/exists/${perfilUsuarioId}/${perfilProveedorId}`);
  }
  obtenerSolicitudesRecibidas(perfilId: number): Observable<any[]> {
    return this.httpClient.get<any[]>(`${baseUrl}/api/solicitudes/${perfilId}/solicitudes-recibidas`);
  }
  aceptarSolicitud(solicitudId: number): Observable<any> {
    return this.httpClient.put<any>(`${baseUrl}/api/solicitudes/aceptar/${solicitudId}`, null);
  }
  obtenerSolicitud(solicitudId: number): Observable<any> {
    return this.httpClient.get<any>(`${baseUrl}/api/solicitudes/obtener/${solicitudId}`);
  }
  cancelarSolicitud(solicitudId: number): Observable<any> {
    return this.httpClient.put<any>(`${baseUrl}/api/solicitudes/cancelar/${solicitudId}`, null);
  }

  getIconActivo(): number {
    return this.iconActivo;
  }

  setIconActivo(indice: number): void {
    this.iconActivo = indice;
  }

  
}

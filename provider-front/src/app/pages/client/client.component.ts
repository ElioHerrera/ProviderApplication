import { Component, OnInit } from '@angular/core';

import { NavprovComponent } from '../../components/navprov/navprov.component';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { FormsModule } from '@angular/forms';
import { SolicitudService } from '../../services/solicitud.service';
import { Usuario } from '../../usuario.model';
import  baseUrl  from './../../services/helper'; 

@Component({
  selector: 'app-client',
  standalone: true,
  imports: [
    NavprovComponent,
    CommonModule,
    MatTabsModule,
    MatToolbarModule,
    MatIconModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    FormsModule,
    MatListModule,
  ],
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent implements OnInit {
  user: any;
  username: string | null = null;
  userId: number | null = null;
  solicitudesRecibidas: any[] = []; // Define la propiedad solicitudesRecibidas como un arreglo vacío
  relacionesComerciales: any[] = [];


  constructor(
    private usuarioService: UserService,
    private solicitudService: SolicitudService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.user);

      console.log('Id del usuario:', this.user.id);

      this.obtenerSolicitudesRecibidas();
      this.obtenerRelacionesComerciales(this.user.perfil.id);
    }
  }

  

  getProfileImageUrl(proveedorId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${proveedorId}/${encodeURIComponent(fileName)}`;
    console.log('Generated URL:', url); // Añade este log
    return url;
  }

  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }
  
  obtenerSolicitudesRecibidas(): void {

    this.solicitudService.obtenerSolicitudesRecibidas(this.user.perfil.id).subscribe(
      (solicitudes) => {
        console.log('Solicitudes recibidas:', solicitudes);
        this.solicitudesRecibidas = solicitudes;
      },
      (error) => {
        console.error('Error al obtener las solicitudes recibidas:', error);
      }
    );
  }

  aceptarCancelarSolicitud(solicitud: any): void {
    if (solicitud.aceptada) {
      // Cancelar solicitud
      this.solicitudService.cancelarSolicitud(solicitud.id).subscribe(
        (response) => {
          console.log('Solicitud cancelada:', response);
          this.actualizarEstadoSolicitud(solicitud.id, false);
          this.obtenerRelacionesComerciales(this.user.perfil.id);
        },
        (error) => {
          console.error('Error al cancelar la solicitud:', error);
        }
      );
    } else {
      // Aceptar solicitud
      this.solicitudService.aceptarSolicitud(solicitud.id).subscribe(
        (response) => {
          console.log('Solicitud aceptada:', response);
          this.actualizarEstadoSolicitud(solicitud.id, true);
          this.obtenerRelacionesComerciales(this.user.perfil.id);
        },
        (error) => {
          console.error('Error al aceptar la solicitud:', error);
        }
      );
    }
  }

  actualizarEstadoSolicitud(solicitudId: number, aceptada: boolean): void {
    const solicitud = this.solicitudesRecibidas.find(s => s.id === solicitudId);
    if (solicitud) {
      solicitud.aceptada = aceptada;
    }
  }

  obtenerColorBoton(solicitud: any): { color: string, texto: string } {
    if (solicitud.aceptada) {
      return { color: 'warn', texto: 'Cancelar Solicitud' };
    } else {
      return { color: 'primary', texto: 'Aceptar' };
    }
  }
  obtenerRelacionesComerciales(userId: number): void { // Recibir el ID del usuario como argumento
    this.usuarioService.obtenerRelacionesComerciales(userId).subscribe(
      (relaciones) => {
        console.log('Relaciones comerciales:', relaciones);
        this.relacionesComerciales = relaciones;
      },
      (error) => {
        console.error('Error al obtener las relaciones comerciales:', error);
      }
    );
  }

}




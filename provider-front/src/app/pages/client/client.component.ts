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
  usuario: any;
  //username: string | null = null;
  //userId: number | null = null;
  solicitudesRecibidas: any[] = []; 
  relacionesComerciales: any[] = [];


  constructor(
    private usuarioService: UserService,
    private solicitudService: SolicitudService,
    private route: ActivatedRoute,
    private router: Router,
  
  ) { }

  ngOnInit(): void {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.usuario = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.usuario);

      console.log('Id del usuario:', this.usuario.id);

      this.obtenerSolicitudesRecibidas();
      this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil);
    }
  }

  

  getProfileImageUrl(proveedorId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${proveedorId}/${encodeURIComponent(fileName)}`;
    //console.log('Generated URL:', url); // Añade este log
    return url;
  }

  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }
  
  obtenerSolicitudesRecibidas(): void {

    this.solicitudService.obtenerSolicitudesRecibidas(this.usuario.perfil.idPerfil).subscribe(
      (solicitudes) => {
        console.log('Solicitudes recibidas:', solicitudes);
        this.solicitudesRecibidas = solicitudes;
      },
      (error) => {
        console.error('Error al obtener las solicitudes recibidas:', error);
      }
    );
  }
/*
  aceptarCancelarSolicitud(solicitud: any): void {
    if (solicitud.aceptada) {
      // Cancelar solicitud
      this.solicitudService.cancelarSolicitud(solicitud.idSolicitud).subscribe(
        (response) => {
          console.log('Solicitud cancelada:', response);
          this.actualizarEstadoSolicitud(solicitud.idSolicitud, false);
          this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil);
        },
        (error) => {
          console.error('Error al cancelar la solicitud:', error);
        }
      );
    } else {
      // Aceptar solicitud
      this.solicitudService.aceptarSolicitud(solicitud.idSolicitud).subscribe(
        (response) => {
          console.log('Solicitud aceptada:', response);
          this.actualizarEstadoSolicitud(solicitud.idSolicitud, true);
          this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil);
        },
        (error) => {
          console.error('Error al aceptar la solicitud:', error);
        }
      );
    }
  }
*/

aceptarCancelarSolicitud(solicitud: any): void {
  if (solicitud.aceptada) {
    // Cancelar solicitud
    this.solicitudService.cancelarSolicitud(solicitud.idSolicitud).subscribe(
      () => {
        console.log('Solicitud cancelada correctamente');
        this.actualizarEstadoSolicitud(solicitud.idSolicitud, false);
        this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil);
        this.obtenerSolicitudesRecibidas(); // Actualiza la lista después de cancelar
        this.actualizarColorBoton(solicitud); // Actualiza el color del botón
      },
      (error) => {
        console.error('Error al cancelar la solicitud:', error);
      }
    );
  } else {
    // Aceptar solicitud
    this.solicitudService.aceptarSolicitud(solicitud.idSolicitud).subscribe(
      () => {
        console.log('Solicitud aceptada correctamente');
        this.actualizarEstadoSolicitud(solicitud.idSolicitud, true);
        this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil);
        this.obtenerSolicitudesRecibidas(); // Actualiza la lista después de aceptar
        this.actualizarColorBoton(solicitud); // Actualiza el color del botón
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

actualizarColorBoton(solicitud: any): void {
  const index = this.solicitudesRecibidas.findIndex(s => s.id === solicitud.id);
  if (index !== -1) {
    const solicitudActualizada = this.solicitudesRecibidas[index];
    const nuevoColorBoton = this.obtenerColorBoton(solicitudActualizada);
    solicitud.colorBoton = nuevoColorBoton.color;
    solicitud.textoBoton = nuevoColorBoton.texto;
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




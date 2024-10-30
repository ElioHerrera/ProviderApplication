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
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ListaPrecios, ListaPreciosRelacion } from '../../usuario.model'; // Importa el DTO de listas de precio
import { MatDialog } from '@angular/material/dialog';
import baseUrl from './../../services/helper';
import { HomeComponent } from '../home/home.component';
import { ListasComponent } from '../../components/listas/listas.component';


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
    MatSnackBarModule,
  ],
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent implements OnInit {
  usuario: any;
  tieneProductos: boolean = false;
  solicitudesRecibidas: any[] = [];
  relacionesComerciales: any[] = [];
  listasDePrecioDisponible: any[] = [];


  constructor(
    private usuarioService: UserService,
    private solicitudService: SolicitudService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.usuario = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.usuario);

      console.log('Id del usuario:', this.usuario.id);

      this.obtenerSolicitudesRecibidas();
      this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil);
      this.verificarProductos(this.usuario.id);
      this.obtenerListas()
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
  solicitudAceptada(aceptada: boolean): boolean {
    // Devuelve true si la solicitud está aceptada (aceptada === true), false de lo contrario
    return aceptada;
  }
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
  verificarProductos(userId: number): void {
    this.usuarioService.tieneProductos(userId).subscribe(
      (resultado) => {
        this.tieneProductos = resultado;
      },
      (error) => {
        console.error('Error al verificar productos:', error);
      }
    );
  }
  // Asignar Listas
  obtenerListas(): void {
    if (this.usuario && this.usuario.perfil && this.usuario.perfil.empresa) {
      this.listasDePrecioDisponible = this.usuario.perfil.empresa.listasDePrecio;
      console.log('Listas de precios disponibles:', this.listasDePrecioDisponible);
    } else {
      console.error('No se encontraron listas de precios para el usuario.');
    }
  }
  seleccionarListaPrecios(clienteId: number): void {
    const dialogRef = this.dialog.open(ListasComponent, {
      data: {
        clienteId: clienteId,
        listasDePrecio: this.listasDePrecioDisponible
      }
    });  
    dialogRef.afterClosed().subscribe((listaSeleccionada: ListaPrecios | undefined) => {
      if (listaSeleccionada) {           
        console.log('Lista seleccionada en ClientComponent:', listaSeleccionada);       
            this.asignarListaPrecios(clienteId, listaSeleccionada.idLista); // Pasar el ID de la lista seleccionada
      } else {
        console.log('Diálogo cerrado sin selección de lista.');
      }
    });
  }
  asignarListaPrecios(clienteId: number, listaId: number): void {
    this.usuarioService.asignarListaPrecios(this.usuario.id, clienteId, listaId).subscribe(
      (response) => {
        this.snackBar.open(response.message, 'Cerrar', { duration: 3000 });
        this.obtenerSolicitudesRecibidas();
      },
      (error) => {
        console.error('Error al asignar lista de precios:', error);
        this.snackBar.open('Error al asignar lista de precios', 'Cerrar', { duration: 3000 });
      }
    );
  }
  obtenerListaAsignada(solicitud: any): ListaPreciosRelacion | null {
    if (solicitud.solicitante.comercio.listasAsignada && solicitud.solicitante.comercio.listasAsignada.length > 0) {
      for (const listaAsignada of solicitud.solicitante.comercio.listasAsignada) {
        const lista = this.listasDePrecioDisponible.find(ld => ld.idLista === listaAsignada.idLista);
        if (lista) {
          return lista;
        }
      }
    }
    return null;
  }

}




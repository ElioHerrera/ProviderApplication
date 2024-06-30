
import { Component, OnInit } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { SolicitudService } from '../../services/solicitud.service';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { PerfilRelacion, Usuario } from '../../usuario.model';
import { FormsModule } from '@angular/forms';
import baseUrl from '../../services/helper';

@Component({
  selector: 'app-provider',
  standalone: true,
  imports: [
    NavhomeComponent,
    CommonModule,
    MatTabsModule,
    MatToolbarModule,
    MatIconModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    FormsModule,
    MatListModule
  ],
  templateUrl: './provider.component.html',
  styleUrl: './provider.component.css'
})
export class ProviderComponent implements OnInit {

  usuario: any;
  proveedores: PerfilRelacion[] = [];
  proveedoresFiltrados: PerfilRelacion[] = [];
  filtroProveedor: string = '';
  solicitudesEnviadas: Map<number, boolean> = new Map<number, boolean>();
  relacionesComerciales: any[] = [];

  constructor(
    private usuarioService: UserService,
    private solicitudService: SolicitudService,
    private router: Router
  ) { }

  ngOnInit(): void {   

    // Recuperar los datos del usuario del almacenamiento local
    const storedUser: string | null = localStorage.getItem('currentUser');
    if (storedUser) {
      this.usuario = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.usuario);
    } else {
      console.error('No se encontraron datos del usuario en localStorage.');
    }

    //Obtener Datos
    this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil); //Pasar idPerfil
    this.obtenerProveedores();
    this.proveedoresFiltrados = this.proveedores;


    // Verificar si usuario y su perfil están definidos
    if (this.usuario && this.usuario.perfil.this.usuario.perfil.idPerfil) {
      console.log('Perfil del usuario:', this.usuario.perfil.idPerfil);
    } else {
      console.error('ID del perfil del usuario no está definido:', this.usuario);
    }
  }

  obtenerProveedores(): void {
    this.usuarioService.obtenerProveedores().subscribe(
      (response: PerfilRelacion[]) => {
        this.proveedores = response;
        this.proveedores.forEach(proveedor => {
          this.verificarSolicitudEnviada(proveedor.idPerfil);
        });
        console.log('Proveedores obtenidos:', this.proveedores);
      },
      (error) => {
        console.error('Error al obtener los proveedores:', error);
      }
    );
  }

  cargarProveedores(): void {
    this.proveedoresFiltrados.forEach(proveedor => {
      this.verificarSolicitudEnviada(proveedor.id);
    });
  }

  filtrarProveedores() {
    this.proveedoresFiltrados = this.proveedores.filter(proveedor =>
      proveedor.nombre.toLowerCase().includes(this.filtroProveedor.toLowerCase()) ||
      proveedor.apellido.toLowerCase().includes(this.filtroProveedor.toLowerCase()) ||
      proveedor.empresa.nombre.toLowerCase().includes(this.filtroProveedor.toLowerCase())
    );
    console.log(this.filtroProveedor);
  }



   // Obtener Imagen de Perfil
   obtenerImagenPerfil(userId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${userId}/${encodeURIComponent(fileName)}`;

    return url;
  }
  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }


  verificarSolicitudEnviada(proveedorId: number): void {

    if (this.usuario && this.usuario.perfil && this.usuario.perfil.idPerfil && proveedorId) {
      console.log('ID del perfil del usuario:', this.usuario.perfil.idPerfil);
      this.solicitudService.verificarSolicitudEnviada(this.usuario.perfil.idPerfil, proveedorId).subscribe(
        (response) => {
          if (response.exists) {
            this.solicitudesEnviadas.set(proveedorId, true);
          } else {
            this.solicitudesEnviadas.set(proveedorId, false);
          }
        },
        (error) => {
          console.error('Error al verificar la solicitud:', error);
        }
      );
    } else {
      console.error('Usuario no autenticado.');
    }
  }

  enviarSolicitud(perfilProveedorId: number): void {
    if (this.usuario && this.usuario.perfil && this.usuario.perfil.idPerfil) {
      // Imprimir la solicitud en la consola antes de enviarla
      const solicitud = {
        solicitanteId: this.usuario.perfil.idPerfil,
        solicitadoId: perfilProveedorId,
        fechaSolicitud: new Date(),
        aceptada: false
      };
      console.log('Solicitud a enviar:', solicitud);

      this.solicitudService.enviarSolicitudAProveedor(this.usuario.perfil.idPerfil, perfilProveedorId).subscribe(
        (response) => {
          console.log('Solicitud enviada:', response.mensaje);
          this.solicitudesEnviadas.set(perfilProveedorId, true);
        },
        (error) => {
          console.error('Error al enviar la solicitud:', error);
        }
      );
    } else {
      console.error('Usuario no autenticado.');
    }
  }



  obtenerRelacionesComerciales(perfilId: number): void { // Recibir el ID del usuario como argumento
    this.usuarioService.obtenerRelacionesComerciales(perfilId).subscribe(
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




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
import { Usuario } from '../../usuario.model';
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
  user: any;
  proveedores: Usuario[] = []; // Definimos un array para almacenar los proveedores
  proveedoresFiltrados: Usuario[] = []; // Lista de proveedores filtrados
  filtroProveedor: string = ''; // Término de búsqueda
  //solicitudesEnviadas: Map<number, boolean> = new Map(); // Mapa para las solicitudes enviadas
  solicitudesEnviadas: Map<number, boolean> = new Map<number, boolean>();


  constructor(
    private usuarioService: UserService,
    private solicitudService: SolicitudService,
    private router: Router
  ) { }

  ngOnInit(): void {

    this.obtenerProveedores();
    this.proveedoresFiltrados = this.proveedores;
    
    // Recuperar los datos del usuario del almacenamiento local
    const storedUser: string | null = localStorage.getItem('currentUser');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.user); // Imprimimos los datos del usuario por consola
    }

  }
  
  obtenerProveedores(): void {
    this.usuarioService.obtenerProveedores().subscribe(
      (response: Usuario[]) => {
        this.proveedores = response;
        this.proveedores.forEach(proveedor => {
       this.verificarSolicitudEnviada(proveedor.perfil.id);
        });
        console.log('Proveedores obtenidos:', this.proveedores);
      },
      (error) => {
        console.error('Error al obtener los proveedores:', error);
      }
    );
  }

  cargarProveedores(): void {
    // Aquí cargarías tus proveedores y luego verificarías las solicitudes
    this.proveedoresFiltrados.forEach(proveedor => {
      this.verificarSolicitudEnviada(proveedor.perfil.id);
    });
  }

  filtrarProveedores() {
    this.proveedoresFiltrados = this.proveedores.filter(proveedor =>
      proveedor.perfil.nombre.toLowerCase().includes(this.filtroProveedor.toLowerCase()) ||
      proveedor.perfil.apellido.toLowerCase().includes(this.filtroProveedor.toLowerCase()) ||
      proveedor.perfil.empresa.nombre.toLowerCase().includes(this.filtroProveedor.toLowerCase())
    );
    console.log(this.filtroProveedor); // Agregar un registro de consola para verificar qué se está filtrando
  }


   

  getProfileImageUrl(proveedorId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${proveedorId}/${encodeURIComponent(fileName)}`;
    console.log('Generated URL:', url); // Añade este log
    return url;
  }

  
  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }
  
  
  verificarSolicitudEnviada(proveedorId: number): void {
    if (this.user && this.user.perfil && this.user.perfil.id) {
      this.solicitudService.verificarSolicitudEnviada(this.user.perfil.id, proveedorId).subscribe(
        (response) => {
          // Actualiza el estado del botón en función de la respuesta
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
    if (this.user && this.user.perfil && this.user.perfil.id) {
      // Imprimir la solicitud en la consola antes de enviarla
      const solicitud = {
        solicitanteId: this.user.perfil.id,
        solicitadoId: perfilProveedorId,
        fechaSolicitud: new Date(),
        aceptada: false
      };
      console.log('Solicitud a enviar:', solicitud);

      this.solicitudService.enviarSolicitudAProveedor(this.user.perfil.id, perfilProveedorId).subscribe(
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
 



  
}



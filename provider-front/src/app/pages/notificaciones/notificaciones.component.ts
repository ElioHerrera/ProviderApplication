import { Component, OnInit } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component';
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { CommonModule } from '@angular/common';
// import { MatTabsModule } from '@angular/material/tabs';
// import { MatIconModule } from '@angular/material/icon';
// import { MatToolbarModule } from '@angular/material/toolbar';
// import { RouterLink } from '@angular/router';
// import { MatCardModule } from '@angular/material/card';
// import { MatButtonModule } from '@angular/material/button';
// import { FormsModule } from '@angular/forms';
// import { MatListModule } from '@angular/material/list';



import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Usuario } from '../../usuario.model';
import { SolicitudService } from '../../services/solicitud.service';

@Component({
  selector: 'app-notificaciones',
  standalone: true,
  imports: [
    NavhomeComponent,
    NavprovComponent,
    CommonModule,
    // MatTabsModule,
    // MatToolbarModule,
    // MatIconModule,
    // RouterLink,
    // MatCardModule,
    // MatButtonModule,
    // FormsModule,
    // MatListModule
  ],
  templateUrl: './notificaciones.component.html',
  styleUrl: './notificaciones.component.css'
})
export class NotificacionesComponent implements OnInit {
  user: any;
  proveedores: Usuario[] = []; // Definimos un array para almacenar los proveedores
  proveedoresFiltrados: Usuario[] = []; // Lista de proveedores filtrados
  filtroProveedor: string = ''; // Término de búsqueda
  solicitudesEnviadas: Map<number, boolean> = new Map(); // Mapa para las solicitudes enviadas
  esDealer: boolean = false;
  esProvider: boolean = false;


  constructor(
    private usuarioService: UserService,
    private solicitudService: SolicitudService,
    private router: Router) { } // Inyectamos el servicio UserService y Router en el constructor

  ngOnInit(): void {

   // this.obtenerProveedores();
    this.proveedoresFiltrados = this.proveedores;


    // Recuperar los datos del usuario del almacenamiento local
  
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.user); // Imprimimos los datos del usuario por consola
      const roles = this.user.roles.map((role: any) => role.roleEnum);
      this.esDealer = roles.includes('CLIENT');
      this.esProvider = roles.includes('PROVIDER');
    }

  }

  // [obtenerProveedores(): void {
  //   this.usuarioService.obtenerProveedores().subscribe(
  //     (response: Usuario[]) => {
  //       this.proveedores = response;
  //       this.proveedores.forEach(proveedor => {
  //         //this.verificarSolicitudEnviada(proveedor.id);
  //       });
  //       console.log('Proveedores obtenidos:', this.proveedores);
  //     },
  //     (error) => {
  //       console.error('Error al obtener los proveedores:', error);
  //     }
  //   );
  // }]

  // verificarSolicitudEnviada(proveedorId: number): void {
  //   if (this.user && this.user.id) {
  //     this.solicitudService.verificarSolicitudEnviada(this.user.id, proveedorId).subscribe(
  //       (response: { exists: boolean }) => {
  //         this.solicitudesEnviadas.set(proveedorId, response.exists);
  //       },
  //       (error) => {
  //         console.error('Error al verificar la solicitud:', error);
  //       }
  //     );
  //   }
  // }

  // enviarSolicitud(proveedorId: number): void {
  //   if (this.user && this.user.id) {
  //     this.solicitudService.enviarSolicitudAProveedor(this.user.id, proveedorId).subscribe(
  //       (response) => {
  //         console.log('Solicitud enviada:', response);
  //         this.solicitudesEnviadas.set(proveedorId, true);
  //       },
  //       (error) => {
  //         console.error('Error al enviar la solicitud:', error);
  //       }
  //     );
  //   } else {
  //     console.error('Usuario no autenticado.');
  //   }
  // }

  


}


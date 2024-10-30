import { Component, OnInit } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component'
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LazyLoadImageModule } from 'ng-lazyload-image';
import baseUrl from '../../services/helper';
import { PublicacionService } from '../../services/publicacion.service';
import { ConfigComponent } from '../config/config.component';
import { MatDialog } from '@angular/material/dialog';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../../services/auth.interceptor';
//import { Solicitud } from '../../usuario.model';


@Component({
  selector: 'app-home',
  standalone: true,
  providers: [
    provideNativeDateAdapter(),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  imports: [
    NavhomeComponent,
    NavprovComponent,
    FormsModule,
    CommonModule,
    RouterLink,
    LazyLoadImageModule 
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})

export class HomeComponent implements OnInit {

  usuario: any;
  esDealer: boolean = false;
  esProvider: boolean = false;
  isSidebarActive = false;

  //Variables para actualizar perfil
  panelOpenState = false;
  // nuevoNombre: string = '';
  // nuevoApellido: string = '';
  // nuevaDescripcion: string = '';
  // selectedFileProfile: File | null = null;

  //Variables de nueva publicacion
  descripcionPublicacion: string = '';
  selectedFilePublicacion: File | null = null;
  imagePreview: string | null = null;

  // Datos
  // relacionesComerciales: any[] = [];
  publicaciones: any[] = [];



  constructor(
    private usuarioService: UserService, 
    private publicacionService: PublicacionService, 
    private router: Router, 
    private snack: MatSnackBar,
    private dialog: MatDialog
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

    //Verificar si es Comerciante o Proveedor
    if (this.usuario) {
      const roles = this.usuario.roles.map((roles: any) => roles.rol);
      console.log(roles.rol)
      this.esDealer = roles.includes('CLIENT');
      this.esProvider = roles.includes('PROVIDER');

      //Obtener Datos        
      //this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil); //Pasar idPerfil
      this.obtenerPublicaciones(this.usuario.id); //Pasar idUsuario
    }

    const menuButton = document.querySelector('.menu-button') as HTMLElement;
    const sidebar = document.querySelector('.sidebar') as HTMLElement;

    menuButton.addEventListener('click', () => {
      const isActive = menuButton.classList.toggle('active'); // Cambia el estado activo del botón
      sidebar.classList.toggle('active', isActive); // Sincroniza el estado del sidebar
  
      // Mueve el botón según el estado activo
      if (isActive) {
        sidebar.style.left = '0'; // Despliega el sidebar
      } else {
        sidebar.style.left = '-500px'; // Oculta el sidebar
      }
    });

    // Manejar el cambio de tamaño de la ventana
  window.addEventListener('resize', () => {
    if (window.innerWidth > 1040) {
      menuButton.classList.remove('active'); // Desactiva el botón
      sidebar.classList.remove('active'); // Asegúrate de que el sidebar esté oculto
      sidebar.style.left = '-500px'; // Ocultar el sidebar
    }
  });
  }

  // Togglet datos personales

  toggleSidebar() {
    this.isSidebarActive = !this.isSidebarActive;
  }

  
  // Relaciones comerciales
  
  /* obtenerRelacionesComerciales(perfilId: number): void { // Recibir el ID del usuario como argumento
    this.usuarioService.obtenerRelacionesComerciales(perfilId).subscribe(
      (relaciones) => {
        console.log('Relaciones comerciales:', relaciones);
        this.relacionesComerciales = relaciones;
      },
      (error) => {
        console.error('Error al obtener las relaciones comerciales:', error);
      }
    );
  }*/

  // Publicaciones

  obtenerPublicaciones(usuarioId: number): void {
    this.publicacionService.obtenerPublicacionesPorPerfilyAsociados(usuarioId).subscribe(
      (response) => {
        this.publicaciones = response;
        console.log('Puclicaciones: ', this.publicaciones);
      },
      (error) => {
        console.error('Error al obtener las publicaciones:', error);
        this.snack.open('Error al obtener las publicaciones', 'Cerrar', { duration: 3000 });
      }
    );
  }

  crearPublicacion(): void {
    if (!this.selectedFilePublicacion || !this.descripcionPublicacion) {
      console.error('Debe seleccionar una imagen y escribir una descripción.');
      return;
    }

    this.publicacionService.crearPublicacion(this.selectedFilePublicacion, this.usuario.id, this.descripcionPublicacion)
      .subscribe(
        response => {
          console.log('Publicación creada exitosamente:', response);
          this.imagePreview = null;
          this.descripcionPublicacion = '';
          this.snack.open('Publicación creada exitosamente', 'Cerrar', { duration: 3000 });
          this.obtenerPublicaciones(this.usuario.id);
        },
        (error: HttpErrorResponse) => {
          console.error('Error al crear la publicación:', error);
        }
      );
  }

  onFileSelectedPublicacion(event: any): void {
    this.selectedFilePublicacion = event.target.files[0];
    if (this.selectedFilePublicacion) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
      };
      reader.readAsDataURL(this.selectedFilePublicacion);
    }
  }

  eliminarPublicacion(publicacionId: number): void {
    this.publicacionService.eliminarPublicacion(publicacionId)
      .subscribe(
        response => {
          console.log('Publicación eliminada exitosamente:', response);
          // Actualizar la lista de publicaciones después de eliminar
          this.obtenerPublicaciones(this.usuario.id);
          // Mostrar mensaje de éxito opcional
          this.snack.open('Publicación eliminada exitosamente', 'Cerrar', { duration: 3000 });
        },
        (error: HttpErrorResponse) => {
          console.error('Error al eliminar la publicación:', error);
          // Manejar el error si es necesario
        }
      );
  }

  // Obtener Imagenes

  obtenerImagenPerfil(userId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${userId}/${encodeURIComponent(fileName)}`;
    return url;
  }

  obtenerImgPublicacion(userId: number, fileName: string): string {
    const url = `${baseUrl}/api/publicaciones/uploads/${userId}/${encodeURIComponent(fileName)}`;
    return url;
  }

  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }

  abrirConfig(): void {
    const dialogRef = this.dialog.open(ConfigComponent);
  
    // Escucha cuando el diálogo se cierra
    dialogRef.afterClosed().subscribe(() => {
      // Recarga los datos del usuario desde localStorage
      const storedUser = localStorage.getItem('currentUser');
      if (storedUser) {
        this.usuario = JSON.parse(storedUser);
        console.log('Datos del usuario actualizados:', this.usuario);
      }
    });
  }

}
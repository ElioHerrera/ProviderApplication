import { Component, OnInit, HostListener } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component'
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatAccordion, MatExpansionModule } from '@angular/material/expansion';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LazyLoadImageModule } from 'ng-lazyload-image'; 

import baseUrl from '../../services/helper';
import { PublicacionService } from '../../services/publicacion.service';
import { Solicitud } from '../../usuario.model';
//import Swal from 'sweetalert2';


@Component({
  selector: 'app-home',
  standalone: true,
  providers: [provideNativeDateAdapter()],
  imports: [
    NavhomeComponent, NavprovComponent,
    CommonModule, FormsModule, RouterLink, MatAccordion, LazyLoadImageModule,
    MatTabsModule, MatToolbarModule, MatIconModule, MatCardModule, MatButtonModule, MatListModule,
    MatExpansionModule, MatFormFieldModule, MatInputModule, MatDatepickerModule, MatSnackBarModule
  ],

  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})

export class HomeComponent implements OnInit {

  usuario: any;
  esDealer: boolean = false;
  esProvider: boolean = false;

  //Variables para actualizar perfil
  panelOpenState = false;
  nuevoNombre: string = '';
  nuevoApellido: string = '';
  nuevaDescripcion: string = '';
  selectedFileProfile: File | null = null;

  //Variables de nueva publicacion
  descripcionPublicacion: string = '';
  selectedFilePublicacion: File | null = null;
  imagePreview: string | null = null;

  // Datos
  relacionesComerciales: any[] = [];
  publicaciones: any[] = [];


  isPersonalDataVisible = false; // Inicialmente, la sección de datos personales está oculta
  isButtonVisible = false; // Inicialmente, el botón está oculto

    // Toggle de la visibilidad de los datos personales
    togglePersonalData(): void {
      this.isPersonalDataVisible = !this.isPersonalDataVisible;
    }
  
    // Detecta el tamaño de la pantalla y muestra/oculta el botón en consecuencia
    @HostListener('window:resize', ['$event'])
    onResize(event: Event): void {
      this.updateButtonVisibility();
    }




  constructor(private http: HttpClient, private usuarioService: UserService, private publicacionService: PublicacionService, private router: Router, private snack: MatSnackBar) { }

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
      const roles = this.usuario.roles.map((role: any) => role.roleEnum);
      this.esDealer = roles.includes('CLIENT');
      this.esProvider = roles.includes('PROVIDER');

      //Obtener Datos        
      this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil); //Pasar idPerfil
      this.obtenerPublicaciones(this.usuario.id); //Pasar idUsuario

      this.updateButtonVisibility();


    }
  }


  private updateButtonVisibility(): void {
    const screenWidth = window.innerWidth;
    this.isButtonVisible = screenWidth < 1040;
    if (screenWidth >= 1040) {
      this.isPersonalDataVisible = true; // Mostrar la sección de datos personales en pantallas grandes
    }
  }


  // Actualizar datos usuario
  archivoSeleccionado(event: any): void {
    this.selectedFileProfile = event.target.files[0];
  }
  actializarImagenPerfil() {
    if (!this.selectedFileProfile) {
      console.error('Archivo no seleccionado');
      return; // Salir de la función si no hay archivo seleccionado
    }
    const formData = new FormData();
    formData.append('file', this.selectedFileProfile);
    formData.append('userId', this.usuario.id.toString()); // Asegurémonos de convertir el ID a string
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    this.http.post<any>(`${baseUrl}/api/img/upload`, formData, { headers })
      .subscribe(
        response => {
          console.log('Archivo subido exitosamente:', response);
          this.usuario.perfil.fotoPerfil = response.fileName; // Actualizamos el nombre de la foto de perfil
          localStorage.setItem('currentUser', JSON.stringify(this.usuario)); // Actualiza el almacenamiento local
          this.obtenerPublicaciones(this.usuario.id); //Actualizar publicaciones      
        },
        (error: HttpErrorResponse) => {
          console.error('Error al subir el archivo:', error);
        }
      );
  }
  actualizarDescripcionPerfil(id: number, nuevaDescripcionPerfil: string): void {
    if (!nuevaDescripcionPerfil || nuevaDescripcionPerfil.trim() === '') {
      console.error('La descripción no puede estar vacía');
      this.snack.open('La descripción no puede estar vacía', 'Cerrar', { duration: 3000 });
      return;
    }

    this.usuarioService.actualizarDescripcion(id, nuevaDescripcionPerfil).subscribe(
      response => {
        console.log('Descripción actualizada:', response);
        this.usuarioService.actualizarDatosUsuario(id).subscribe(
          usuarioActualizado => {
            console.log('Usuario actualizado:', usuarioActualizado);
            this.usuario = usuarioActualizado;
          },
          error => {
            console.error('Error al obtener el usuario actualizado:', error);
          }
        );
      },
      error => {
        console.error('Error al actualizar descripción:', error);
      }
    );
  }
  actualizarNombre(id: number, nuevoNombre: string) {

    if (!nuevoNombre || nuevoNombre.trim() === '') {
      console.error('El nombre no puede estar vacío');
      this.snack.open('El nombre no puede estar vacío', 'Cerrar', { duration: 3000 });
      return; // Salir del método si el nombre está vacío
    }

    this.usuarioService.actualizarNombre(id, nuevoNombre).subscribe(
      response => {
        console.log('Nombre actualizado:', response);
        // Realizar una solicitud GET para obtener todos los datos actualizados del usuario
        this.usuarioService.actualizarDatosUsuario(id).subscribe(
          usuarioActualizado => {
            console.log('Usuario actualizado:', usuarioActualizado);
            this.usuario = usuarioActualizado; // Actualizar el objeto user con los datos actualizados
          },
          error => {
            console.error('Error al obtener el usuario actualizado:', error);
            // Manejar el error si es necesario
          }
        );
      },
      error => {
        console.error('Error al actualizar nombre:', error);
        // Manejar el error si es necesario
      }
    );
  }
  actualizarApellido(id: number, nuevoApellido: string) {
    if (!nuevoApellido || nuevoApellido.trim() === '') {
      console.error('El apellido no puede estar vacío');
      this.snack.open('El apellido no puede estar vacío', 'Cerrar', { duration: 3000 });
      return; // Salir del método si el nombre está vacío
    }
    this.usuarioService.actualizarApellido(id, nuevoApellido).subscribe(
      response => {
        console.log('Nombre actualizado:', response);
        // Realizar una solicitud GET para obtener todos los datos actualizados del usuario
        this.usuarioService.actualizarDatosUsuario(id).subscribe(
          usuarioActualizado => {
            console.log('Usuario actualizado:', usuarioActualizado);
            this.usuario = usuarioActualizado; // Actualizar el objeto user con los datos actualizados
          },
          error => {
            console.error('Error al obtener el usuario actualizado:', error);
            // Manejar el error si es necesario
          }
        );
      },
      error => {
        console.error('Error al actualizar nombre:', error);
        // Manejar el error si es necesario
      }
    );
  }
  // Crear nueva publicación
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
  //Obtener datos
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
}
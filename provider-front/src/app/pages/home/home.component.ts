import { Component, OnInit, ViewChild } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
//import { Usuario } from '../../usuario.model';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';

import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatAccordion, MatExpansionModule } from '@angular/material/expansion';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { provideNativeDateAdapter } from '@angular/material/core';
import { FormsModule } from '@angular/forms';
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
//import Swal from 'sweetalert2';
import baseUrl from '../../services/helper';


@Component({
  selector: 'app-home',
  standalone: true,
  providers: [provideNativeDateAdapter()],
  imports: [
    NavhomeComponent, NavprovComponent,
    CommonModule, FormsModule, RouterLink,
    MatTabsModule, MatToolbarModule, MatIconModule, MatCardModule, MatButtonModule, MatListModule,
    MatExpansionModule, MatFormFieldModule, MatInputModule, MatDatepickerModule, MatSnackBarModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  user: any = {};
  esDealer: boolean = false;
  esProvider: boolean = false;
  userId: number | undefined; // Definir userId como una propiedad de la clase
  panelOpenState = false;
  selectedFile: string | null = null;
  nuevoNombre: string = '';
  nuevoApellido: string = '';
  nuevaDescripcion: string = '';
  relacionesComerciales: any[] = [];

  constructor(private http: HttpClient, private usuarioService: UserService, private router: Router, private snack: MatSnackBar) { }

  ngOnInit(): void {

    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.user);
      const roles = this.user.roles.map((role: any) => role.roleEnum);
      this.esDealer = roles.includes('CLIENT');
      this.esProvider = roles.includes('PROVIDER');
      this.obtenerRelacionesComerciales(this.user.perfil.id);
    }
  }
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }
  //Cargar Imagen de perfil
  onUpload() {
    if (!this.selectedFile) {
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('userId', this.user.id.toString()); // Asegurémonos de convertir el ID a string
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    this.http.post<any>(`${baseUrl}/api/img/upload`, formData, { headers })
      .subscribe(
        response => {
          console.log('Archivo subido exitosamente:', response);
          this.user.perfil.fotoPerfil = response.fileName; // Actualizamos el nombre de la foto de perfil
          localStorage.setItem('currentUser', JSON.stringify(this.user)); // Actualiza el almacenamiento local
        },
        (error: HttpErrorResponse) => {
          console.error('Error al subir el archivo:', error);
        }
      );
  }
  getProfileImageUrl(fileName: string): string {
    this.userId = this.user ? this.user.id : '';
    const url = `${baseUrl}/api/img/uploads/${this.userId}/${fileName}`;
    return url;
  }

  getProfileImageRelacion(proveedorId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${proveedorId}/${encodeURIComponent(fileName)}`;
    console.log('Generated URL:', url); // Añade este log
    return url;
  }
 
  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }
  
  // Actualizar Datos
  actualizarNombre(id: number, nuevoNombre: string) {
    this.usuarioService.actualizarNombre(id, nuevoNombre).subscribe(
      response => {
        console.log('Nombre actualizado:', response);
        // Realizar una solicitud GET para obtener todos los datos actualizados del usuario
        this.usuarioService.actualizarDatosUsuario(id).subscribe(
          usuarioActualizado => {
            console.log('Usuario actualizado:', usuarioActualizado);
            this.user = usuarioActualizado; // Actualizar el objeto user con los datos actualizados
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
    this.usuarioService.actualizarApellido(id, nuevoApellido).subscribe(
      response => {
        console.log('Nombre actualizado:', response);
        // Realizar una solicitud GET para obtener todos los datos actualizados del usuario
        this.usuarioService.actualizarDatosUsuario(id).subscribe(
          usuarioActualizado => {
            console.log('Usuario actualizado:', usuarioActualizado);
            this.user = usuarioActualizado; // Actualizar el objeto user con los datos actualizados
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
  actualizarDescripcion(id: number, nuevaDescripcion: string) {
    this.usuarioService.actualizarDescripcion(id, nuevaDescripcion).subscribe(
      response => {
        console.log('Nombre actualizado:', response);
        // Realizar una solicitud GET para obtener todos los datos actualizados del usuario
        this.usuarioService.actualizarDatosUsuario(id).subscribe(
          usuarioActualizado => {
            console.log('Usuario actualizado:', usuarioActualizado);
            this.user = usuarioActualizado; // Actualizar el objeto user con los datos actualizados
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
/*
obtenerRelacionesComerciales(userId: number): void {
  this.http.get<any[]>(`${baseUrl}/api/perfiles/${userId}/relaciones-comerciales`).subscribe(
    (relaciones) => {
      console.log('Relaciones comerciales recibidas:', relaciones);
      this.relacionesComerciales = relaciones;
    },
    (error: HttpErrorResponse) => {
      if (error.error instanceof ErrorEvent) {
        // Error del lado del cliente
        console.error('Error del lado del cliente:', error.error.message);
      } else {
        // Error del lado del servidor
        console.error(`Error del lado del servidor (status ${error.status}): ${error.error}`);
      }
    }
  );
}
*/
}





import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { NavhomeComponent } from '../../components/navhome/navhome.component';
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms'; // Import FormsModule
import { LazyLoadImageModule } from 'ng-lazyload-image';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';


import baseUrl from '../../services/helper';
import { HomeComponent } from '../home/home.component';
import { Usuario } from '../../usuario.model';
import Swal from 'sweetalert2';
import { ImagenPerfilComponent } from '../imagen-perfil/imagen-perfil.component';

@Component({
  selector: 'app-config',
  standalone: true,
  imports: [
    NavhomeComponent,
    NavprovComponent,
    CommonModule,

    FormsModule,
    LazyLoadImageModule,
    MatDialogModule,

    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule

  ],
  templateUrl: './config.component.html',
  styleUrl: './config.component.css'
})

export class ConfigComponent {

  usuario: any;
  nuevosDatosUsuario: any;

  selectedFileProfile: File | null = null;


  esDealer: boolean = false;
  esProvider: boolean = false;


  descripcionPerfil: string = "";
  nombre: string = '';
  apellido: string = '';
  empresaComercio: string = '';
  rubro: string = '';
  domicilio: string = '';
  telefono: string = '';
  nombreEditable: boolean = false;
  apellidoEditable: boolean = false;
  descripcionEditable: boolean = false;
  empresaComercioEditable: boolean = false;
  rubroEditable: boolean = false;
  domicilioEditable: boolean = false;
  telefonoEditable: boolean = false;


  constructor(
    private usuarioService: UserService,
    private router: Router,
    private snack: MatSnackBar,
    public dialogRef: MatDialogRef<HomeComponent>,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {

    // Recuperar los datos del usuario del almacenamiento local
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.usuario = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.usuario); // Imprimimos los datos del usuario por consola


      const roles = this.usuario.roles.map((role: any) => role.rol);
      this.esDealer = roles.includes('CLIENT');
      this.esProvider = roles.includes('PROVIDER');

      this.nombre = this.usuario.perfil.nombre;
      this.apellido = this.usuario.perfil.apellido;
      this.descripcionPerfil = this.usuario.perfil.descripcion;

      // Asigna valores específicos de empresa o comercio
      if (this.esProvider && this.usuario.perfil.empresa) {
        this.empresaComercio = this.usuario.perfil.empresa.nombre;
        this.rubro = this.usuario.perfil.empresa.rubro;
        this.domicilio = this.usuario.perfil.empresa.domicilio;
        this.telefono = this.usuario.perfil.empresa.telefono;
        console.log('Es Proveedor');
      } else if (this.esDealer && this.usuario.perfil.comercio) {
        this.empresaComercio = this.usuario.perfil.comercio.nombre; // En este caso, "nuevaEmpresa" representa el nombre del comercio
        this.rubro = this.usuario.perfil.comercio.rubro;
        this.domicilio = this.usuario.perfil.comercio.domicilio;
        this.telefono = this.usuario.perfil.comercio.telefono;
        console.log('Es Comerciante');
      }
    }

    console.log('Open Config');
  }

  // Obtener Imagenes

  obtenerImagenPerfil(userId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${userId}/${encodeURIComponent(fileName)}`;
    return url;
  }



  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }

  cancelar(): void {
    console.log('Cancelado');
    this.dialogRef.close();
  }

  toggleEditable(campo: string) {
    switch (campo) {
      case 'nombre':
        this.nombreEditable = !this.nombreEditable;
        break;
      case 'apellido':
        this.apellidoEditable = !this.apellidoEditable;
        break;
      case 'descripcion':
        this.descripcionEditable = !this.descripcionEditable;
        break;
      case 'empresa_comercio':
        this.empresaComercioEditable = !this.empresaComercioEditable;
        break;
      case 'rubro':
        this.rubroEditable = !this.rubroEditable;
        break;
      case 'domicilio':
        this.domicilioEditable = !this.domicilioEditable;
        break;
      case 'telefono':
        this.telefonoEditable = !this.telefonoEditable;
        break;
    }
  }

  actualizarDatos(): void {
    // Verifica que los campos requeridos no estén vacíos
    if (!this.nombre || !this.apellido || !this.descripcionPerfil || !this.empresaComercio || !this.rubro || !this.domicilio || !this.telefono) {
      this.snack.open('Todos los campos deben estar completos', 'Cerrar', { duration: 3000 });
      return;
    }

    // Determina si el usuario es una empresa o un comercio
    const esEmpresa = this.usuario.tipo === 'empresa'; // Asume que tienes una propiedad "tipo" en el usuario
    this.esProvider = esEmpresa;

    // Crea el objeto Usuario actualizado
    const usuarioActualizado: Usuario = {
      ...this.usuario, // Copia los datos actuales del usuario
      perfil: {
        ...this.usuario.perfil,
        nombre: this.nombre,
        apellido: this.apellido,
        descripcion: this.descripcionPerfil,
        ...(esEmpresa ? {
          empresa: {
            nombre: this.empresaComercio,
            rubro: this.rubro,
            domicilio: this.domicilio,
            telefono: this.telefono
          }
        } : {
          comercio: {
            nombre: this.empresaComercio,
            rubro: this.rubro,
            domicilio: this.domicilio,
            telefono: this.telefono
          }
        })
      }
    };

    //Actualizar localStorage
    localStorage.setItem('currentUser', JSON.stringify(usuarioActualizado)); // Actualiza el almacenamiento local

    this.usuarioService.actualizarUsuario(usuarioActualizado).subscribe(
      response => {
        // Muestra el SweetAlert de confirmación y cierra el diálogo si está abierto
        Swal.fire({
          title: 'Actualización exitosa',
          text: 'El perfil ha sido actualizado correctamente.',
          icon: 'success',
          confirmButtonText: 'Aceptar'
        }).then(() => {
          // Cierra el diálogo después de que el usuario presione "Aceptar"
          this.dialogRef.close();
        });
      },
      error => {
        this.snack.open('Error al actualizar el perfil', 'Cerrar', { duration: 3000 });
      }
    );
  }

  abrirEditImagen(): void {
    const dialogRef = this.dialog.open(ImagenPerfilComponent, {
      width: '400px',  // Ajusta el tamaño del diálogo si es necesario
      data: { usuario: this.usuario }  // Puedes pasar datos si el componente ImagenPerfilComponent los necesita
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Si el diálogo se cerró con éxito y se devuelve un resultado,
        // puedes actualizar la imagen de perfil o hacer otras acciones aquí.
        this.usuario.perfil.fotoPerfil = result.fotoPerfilActualizada;  // Ejemplo de actualización de imagen
      }
    });
  }























//  lizacion datos usuario

//   archivoSeleccionado(event: any): void {
//     this.selectedFileProfile = event.target.files[0];
//   }

//   actializarImagenPerfil() {
//     if (!this.selectedFileProfile) {
//       console.error('Archivo no seleccionado');
//       return; // Salir de la función si no hay archivo seleccionado
//     }
//     const formData = new FormData();
//     formData.append('file', this.selectedFileProfile);
//     formData.append('userId', this.usuario.id.toString()); // Asegurémonos de convertir el ID a string
//     const headers = new HttpHeaders();
//     headers.append('Content-Type', 'multipart/form-data');

//     this.http.post<any>(`${baseUrl}/api/img/upload`, formData, { headers })
//       .subscribe(
//         response => {
//           console.log('Archivo subido exitosamente:', response);
//           this.usuario.perfil.fotoPerfil = response.fileName; // Actualizamos el nombre de la foto de perfil
//           localStorage.setItem('currentUser', JSON.stringify(this.usuario)); // Actualiza el almacenamiento local

//         },
//         (error: HttpErrorResponse) => {
//           console.error('Error al subir el archivo:', error);
//         }
//       );
//   }


//   actualizarImagenPerfil(): void {
//     if (!this.selectedFileProfile) {
//       console.error('Archivo no seleccionado');
//       return;
//     }
//     this.usuarioService.subirImagenPerfil(this.usuario.id, this.selectedFileProfile).subscribe(
//       response => {
//         this.usuario.perfil.fotoPerfil = response.fileName;
//         localStorage.setItem('currentUser', JSON.stringify(this.usuario));
//       },
//       error => {
//         console.error('Error al subir el archivo:', error);
//       }
//     );
//   } // Actua


// }
}
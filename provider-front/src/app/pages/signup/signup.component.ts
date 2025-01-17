import { Component, OnInit } from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    MatSnackBarModule,
    NavbarComponent,
    CommonModule
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit {

  public usuario = {
    username: '',
    password: '',
    email: '',
    tipoUsuario: '',
    perfil: {
      nombre: '',
      apellido: '',
      empresa: {
        nombre: '',
        rubro: '',
        telefono: '',
        domicilio: ''
      },
      comercio: {
        nombre: '',
        rubro: '',
        telefono: '',
        domicilio: ''
      }
    }
  };

  constructor(private userService: UserService, private snack: MatSnackBar) { }

  ngOnInit(): void { }

  registrarUsuario(event: Event) {
    event.preventDefault(); // Prevenir el envío predeterminado del formulario

    // Validar campos
    if (!this.usuario.username || !this.usuario.password || !this.usuario.perfil.nombre || !this.usuario.perfil.apellido || !this.usuario.email || !this.usuario.tipoUsuario) {
      this.snack.open('Todos los campos son obligatorios', 'Aceptar', {
        duration: 3000,
        verticalPosition: 'top',
        horizontalPosition: 'right'
      });
      return;
    }

    if (this.usuario.tipoUsuario === 'PROVEEDOR' && (!this.usuario.perfil.empresa.nombre || !this.usuario.perfil.empresa.rubro || !this.usuario.perfil.empresa.telefono || !this.usuario.perfil.empresa.domicilio)) {
      this.snack.open('Por favor completa los campos de la empresa', 'Aceptar', {
        duration: 3000,
        verticalPosition: 'top',
        horizontalPosition: 'right'
      });
      return;
    }

    if (this.usuario.tipoUsuario === 'COMERCIANTE' && (!this.usuario.perfil.comercio.nombre || !this.usuario.perfil.comercio.telefono || !this.usuario.perfil.comercio.rubro || !this.usuario.perfil.comercio.domicilio)) {
      this.snack.open('Por favor completa los campos del comercio', 'Aceptar', {
        duration: 3000,
        verticalPosition: 'top',
        horizontalPosition: 'right'
      });
      return;
    }

    // Verificar si el nombre de usuario ya existe
    this.userService.verifyExistingUser(this.usuario.username).subscribe(
      (usuarioExiste) => {
        if (usuarioExiste) {
          this.snack.open('El nombre de usuario ya está en uso', 'Aceptar', {
            duration: 3000,
            verticalPosition: 'top',
            horizontalPosition: 'right'
          });
        } else {
          // Verificar si el correo electrónico ya existe
          this.userService.verifyExistingEmail(this.usuario.email).subscribe(
            (emailExiste) => {
              if (emailExiste) {
                this.snack.open('El correo electrónico ya está en uso', 'Aceptar', {
                  duration: 3000,
                  verticalPosition: 'top',
                  horizontalPosition: 'right'
                });
              } else {
                // Llamar al servicio para registrar el usuario
                this.userService.signup(this.usuario).subscribe(
                  (response: any) => {
                    console.log(response);
                    Swal.fire({
                      icon: 'success',
                      title: 'Registro Exitoso',
                      text: response.message,
                      confirmButtonText: 'Aceptar'
                    });
                    this.limpiarFormulario();
                  },
                  (error: HttpErrorResponse) => {
                    console.error(error);
                    this.snack.open('Ha ocurrido un error en el registro', 'Aceptar', {
                      duration: 3000,
                      verticalPosition: 'top',
                      horizontalPosition: 'right'
                    });
                  }
                );
              }
            },
            (error: HttpErrorResponse) => {
              console.error(error);
              this.snack.open('Error al verificar el correo electrónico', 'Aceptar', {
                duration: 3000,
                verticalPosition: 'top',
                horizontalPosition: 'right'
              });
            }
          );
        }
      },
      (error: HttpErrorResponse) => {
        console.error(error);
        this.snack.open('Error al verificar el nombre de usuario', 'Aceptar', {
          duration: 3000,
          verticalPosition: 'top',
          horizontalPosition: 'right'
        });
      }
    );
  }

  limpiarFormulario() {
    this.usuario = {
      username: '',
      password: '',
      email: '',
      tipoUsuario: '',
      perfil: {
        nombre: '',
        apellido: '',
        empresa: {
          nombre: '',
          rubro: '',
          telefono: '',
          domicilio: ''
        },
        comercio: {
          nombre: '',
          rubro: '',
          telefono: '',
          domicilio: ''
        }
      }
    };
  }

  desplegarTipoUsuario(event: any) {
    console.log('Tipo de usuario seleccionado:', event);
  }
}


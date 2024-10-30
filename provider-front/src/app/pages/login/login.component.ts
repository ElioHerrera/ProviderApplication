import { Component, OnInit, ChangeDetectionStrategy, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterOutlet } from '@angular/router';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { jwtDecode } from 'jwt-decode';




@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    MatSnackBarModule,
    NavbarComponent,
    HttpClientModule,
    MatIconModule
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',

})

export class LoginComponent implements OnInit {

  credentials = {
    username: '',
    password: ''
  };

  hide = signal(true);
  loading = signal(true);

  constructor(private userService: UserService, private router: Router, private snackBar: MatSnackBar) { }


  ngOnInit(): void {

    const token = localStorage.getItem('token');
    const currentUser = localStorage.getItem('currentUser');
    const redireccionUrl = localStorage.getItem('redireccionUrl');

    // Si hay un token, un usuario y una URL de redirección, redirigir
    if (token && currentUser && redireccionUrl) {
      try {
        console.log('Token:', token);
        console.log('Datos', localStorage.getItem('currentUser'));

        const decodedToken: any = jwtDecode(token);

        // Verifica si decodedToken.exp está definido antes de usarlo
        if (decodedToken && decodedToken.exp) {
          const expirationDate = decodedToken.exp * 1000;
          // Verifica si el token no ha expirado
          if (Date.now() < expirationDate) {
            this.router.navigate([redireccionUrl]);
            console.log('redireccion: ', redireccionUrl);
          } else {
            console.log('Token expirado');
            localStorage.removeItem('token');
            localStorage.removeItem('currentUser');
            localStorage.removeItem('redireccionUrl');
          }
        } else {
          console.log('No se pudo obtener la fecha de expiración del token');
        }
      } catch (error) {
        console.error('Error al decodificar el token:', error);
      }
    }


    setTimeout(() => {
      this.loading.set(false); // Ocultar pantalla de carga después de 1 segundo
    }, 1000);

    this.userService.star().subscribe(response => {
      console.log('Server ready:', response);
    }, error => {
      console.error('Error:', error);
    });
  }

  //METODO ANTERIOS QUE SI FUNCIONABA ANTES DE AGREGAR EL TOKEN
  // login() {    
  //   this.userService.login(this.credentials).subscribe(
  //     (response: any) => {
  //       const user = response.user;
  //       const username = user.username;
  //       localStorage.setItem('currentUser', JSON.stringify(user));
  //       localStorage.setItem('username', username)
  //       this.snackBar.open('Inicio de sesión exitoso', 'Cerrar', { duration: 3000 });        
  //       this.router.navigate([response.redirectUrl]);      
  //     },
  //     (error) => {
  //       this.snackBar.open('Error en el inicio de sesión', 'Cerrar', { duration: 3000 });
  //       console.error('Error de inicio de sesión:', error);        
  //     }
  //   );
  // }

  // login(){
  //   this.userService.login(this.credentials).subscribe({
  //     next: (response) => {
  //       // Verificar si la respuesta tiene la propiedad 'username'
  //       if (response && response.username) {
  //         this.snackBar.open(`Bienvenido ${response.username}`, 'Cerrar', {
  //           duration: 3000,
  //         });
  //         // Aquí puedes continuar con la lógica de redirección o guardado del token
  //       } else {
  //         this.snackBar.open('Error en la autenticación. Usuario no encontrado.', 'Cerrar', {
  //           duration: 3000,
  //         });
  //       }
  //     },
  //     error: (err) => {
  //       console.error('Error de autenticación:', err);
  //       this.snackBar.open('Error al iniciar sesión', 'Cerrar', {
  //         duration: 3000,
  //       });
  //     }
  //   });
  // }

  // login(){
  //   this.userService.login(this.credentials).subscribe({
  //     next: (response) => {
  //       const usuario = response.usuario;
  //       if (usuario && usuario.username) {
  //         this.snackBar.open(`Bienvenido ${usuario.username}`, 'Cerrar', {
  //           duration: 3000,
  //         });
  //         // Guardar el token y usuario en localStorage
  //         localStorage.setItem('token', response.token);
  //         localStorage.setItem('currentUser', JSON.stringify(usuario));
  //         // Redireccionar a la URL adecuada
  //         this.router.navigate([response.redireccionUrl]);
  //       } else {
  //         this.snackBar.open('Error en la autenticación. Usuario no encontrado.', 'Cerrar', {
  //           duration: 3000,
  //         });
  //       }
  //     },
  //     error: (err) => {
  //       console.error('Error de autenticación:', err);
  //       this.snackBar.open('Error al iniciar sesión', 'Cerrar', {
  //         duration: 3000,
  //       });
  //     }
  //   });
  // }

  login() {
    this.userService.login(this.credentials).subscribe({
      next: (response) => {
        if (response && response.token && response.usuario) {
          // Almacenar token y usuario en localStorage
          localStorage.setItem('token', response.token);
          localStorage.setItem('currentUser', JSON.stringify(response.usuario));
          localStorage.setItem('redireccionUrl', response.redireccionUrl);

          // Mostrar notificación de éxito
          this.snackBar.open(`Bienvenido ${response.usuario.username}`, 'Cerrar', {
            duration: 3000,
          });

          // Redirigir a la URL asignada
          this.router.navigate([response.redireccionUrl]);
        } else {
          this.snackBar.open('Error en la autenticación. Usuario no encontrado.', 'Cerrar', {
            duration: 3000,
          });
        }
      },
      error: (err) => {
        console.error('Error de autenticación:', err);
        this.snackBar.open('Error al iniciar sesión', 'Cerrar', {
          duration: 3000,
        });
      }
    });
  }




  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.preventDefault();  // Previene el comportamiento por defecto del evento
    event.stopPropagation();  // Esto evita que el click en el botón ejecute el evento de submit del formulario
  }

}
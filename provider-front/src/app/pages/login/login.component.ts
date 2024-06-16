import { Component, OnInit } from '@angular/core';
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

import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
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
    
   
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})



export class LoginComponent implements OnInit {

  credentials = {
    username: '',
    password: ''
  };

  constructor(private userService: UserService, private router: Router, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  
  login() {
    this.userService.login(this.credentials).subscribe(
      (response: any) => {
        const user = response.user;
        const username = user.username;
        localStorage.setItem('currentUser', JSON.stringify(user));
        localStorage.setItem('username', username)
        this.snackBar.open('Inicio de sesión exitoso', 'Cerrar', { duration: 3000 });

         // Redirigir a la URL enviada por el backend
        this.router.navigate([response.redirectUrl]);
      
      },
      (error) => {
        this.snackBar.open('Error en el inicio de sesión', 'Cerrar', { duration: 3000 });
        console.error('Error de inicio de sesión:', error);
      }
    );
  }

}
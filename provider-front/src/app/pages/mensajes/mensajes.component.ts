import { Component, OnInit } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component';
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { CommonModule } from '@angular/common';


import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Usuario } from '../../usuario.model';





@Component({
  selector: 'app-mensajes',
  standalone: true,
  imports: [
    NavhomeComponent,
    NavprovComponent,
    CommonModule,
    
  ],
  templateUrl: './mensajes.component.html',
  styleUrl: './mensajes.component.css'
})
export class MensajesComponent implements OnInit {
  user: any;
  esDealer: boolean = false;
  esProvider: boolean = false;


  constructor(
    private usuarioService: UserService,
    private router: Router) { } // Inyectamos el servicio UserService y Router en el constructor

  ngOnInit(): void {

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
  
}

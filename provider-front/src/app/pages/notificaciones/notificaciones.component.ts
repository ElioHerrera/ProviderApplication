import { Component, OnInit } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component';
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';


@Component({
  selector: 'app-notificaciones',
  standalone: true,
  imports: [
    NavhomeComponent,
    NavprovComponent,
    CommonModule],
  templateUrl: './notificaciones.component.html',
  styleUrl: './notificaciones.component.css'
})

export class NotificacionesComponent implements OnInit {

  usuario: any;
  esDealer: boolean = false;
  esProvider: boolean = false;


  constructor(
    private router: Router,
    private usuarioService: UserService
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
      }
    }
  }
  
  
import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';

import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { Usuario } from '../../usuario.model';



@Component({
  selector: 'app-navprov',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatIconModule,
    RouterLink
  ],
  templateUrl: './navprov.component.html',
  styleUrl: './navprov.component.css'
})
export class NavprovComponent implements OnInit {
  user: any;
  

  constructor(private userService: UserService, private router: Router) {

  }


  logout() {
    this.userService.logout();
    this.router.navigate(['/login']);
  }

  ngOnInit(): void {
    // Recuperar los datos del usuario del almacenamiento local
    const storedUser: string | null = localStorage.getItem('currentUser');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
      console.log('Username:', this.user.username  ); // Imprimimos los datos del usuario por consola
    }

  }

}
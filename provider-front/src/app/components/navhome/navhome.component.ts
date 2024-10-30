import { Component, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { SolicitudService } from '../../services/solicitud.service';
import { LazyLoadImageModule } from 'ng-lazyload-image'; 
import baseUrl from '../../services/helper';

@Component({
  selector: 'app-navhome',
  standalone: true,
  imports: [
    RouterLink,
    CommonModule,
    LazyLoadImageModule,
   ],
  templateUrl: './navhome.component.html',
  styleUrl: './navhome.component.css'
})
export class NavhomeComponent implements OnInit {
  usuario: any;
  iconActivo: number = 0;

  menuOpen = false;
  
  

  constructor(
    private userService: UserService,
    private router: Router,
    private solicitudService: SolicitudService
  ) {}

  ngOnInit(): void {

    // Recuperar los datos del usuario del almacenamiento local
    const storedUser: string | null = localStorage.getItem('currentUser');
    if (storedUser) {
      this.usuario = JSON.parse(storedUser);
    }

    this.router.events.subscribe(() => {
      this.updateActiveIcon();
    });

    this.updateActiveIcon();
  }

  updateActiveIcon(): void {
    const currentRoute = this.router.url;
    if (currentRoute.includes('/home/mensajes')) {
      this.iconActivo = 1;
    } else if (currentRoute.includes('/home/notificaciones')) {
      this.iconActivo = 4;
    } else if (currentRoute.includes('/home/clientes') || currentRoute.includes('/home/proveedores')) {
      this.iconActivo = 2;
    } else if (currentRoute.includes('/home/productos') || currentRoute.includes('/home/pedidos')) {
      this.iconActivo = 3;
    } else if (currentRoute.includes('/config')) {
      this.iconActivo = 5;
    } else {
      this.iconActivo = 0;
    }
    this.solicitudService.setIconActivo(this.iconActivo);
  }

  marcarIconoActivo(indice: number, ruta: string) {
    this.iconActivo = indice;
    this.solicitudService.setIconActivo(indice);
    this.router.navigate([ruta]);
  }

  logout() {
    this.iconActivo = 0;
    this.solicitudService.setIconActivo(this.iconActivo);
    this.userService.logout();
    this.router.navigate(['/login']);
  }

   // Obtener Imagenes
   obtenerImagenPerfil(userId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${userId}/${encodeURIComponent(fileName)}`;
    return url;
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }
  
}
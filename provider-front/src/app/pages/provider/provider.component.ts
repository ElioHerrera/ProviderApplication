import { Component, OnInit } from '@angular/core';
import { NavhomeComponent } from '../../components/navhome/navhome.component'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { SolicitudService } from '../../services/solicitud.service';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { PerfilRelacion, Usuario, Pedido, Item } from '../../usuario.model';
import { FormsModule } from '@angular/forms';
import { LazyLoadImageModule } from 'ng-lazyload-image';
import baseUrl from '../../services/helper';
import { MatDialog } from '@angular/material/dialog';
import { ResumenPedidoComponent } from '../../components/resumen-pedido/resumen-pedido.component';

@Component({
  selector: 'app-provider',
  standalone: true,
  imports: [
    NavhomeComponent,
    CommonModule,
    MatTabsModule,
    MatToolbarModule,
    MatIconModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    FormsModule,
    MatListModule,
    LazyLoadImageModule
  ],
  templateUrl: './provider.component.html',
  styleUrl: './provider.component.css'
})
export class ProviderComponent implements OnInit {

  usuario: any;
  proveedores: PerfilRelacion[] = [];
  proveedoresFiltrados: PerfilRelacion[] = [];
  filtroProveedor: string = '';
  solicitudesEnviadas: Map<number, boolean> = new Map<number, boolean>();
  solicitudesPendientes: any[] = [];
  relacionesComerciales: any[] = [];
  productosProveedor: any[] = [];
  busquedaProveedor: string = '';
  proveedoresEncontrados: PerfilRelacion[] = [];
  productosSeleccionados: any[] = [];

  //Efectos
  hoveredProveedorId: number | null = null;
  clickedProveedorId: number | null = null;
  proveedorSeleccionado: number = 0;
  isSidebarActive = false;

  constructor(
    private usuarioService: UserService,
    private solicitudService: SolicitudService,
    private router: Router,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {

    /* Recuperar los datos del usuario */

    const storedUser: string | null = localStorage.getItem('currentUser');
    if (storedUser) {
      this.usuario = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.usuario);
    } else {
      console.error('No se encontraron datos del usuario en localStorage.');
    }

    /* Verificar si usuario y su perfil están definidos antes de proceder */

    if (this.usuario && this.usuario.perfil && this.usuario.perfil.idPerfil) {
      console.log('Perfil del usuario:', this.usuario.perfil.idPerfil);
      this.obtenerRelacionesComerciales(this.usuario.perfil.idPerfil);
      this.obtenerSolicitudesPendientes(this.usuario.perfil.idPerfil);
      // this.proveedoresFiltrados = this.proveedores;
    } else {
      console.error('ID del perfil del usuario no está definido:', this.usuario);
    }

    /* Sidebar */
    const menuButton = document.querySelector('.menu-button') as HTMLElement;
    const sidebar = document.querySelector('.sidebar') as HTMLElement;

    menuButton.addEventListener('click', () => {
      const isActive = menuButton.classList.toggle('active'); // Cambia el estado activo del botón
      sidebar.classList.toggle('active', isActive); // Sincroniza el estado del sidebar

      /* Mueve el botón según el estado activo */
      if (isActive) {
        sidebar.style.left = '0'; // Despliega el sidebar
      } else {
        sidebar.style.left = '-500px'; // Oculta el sidebar
      }
    });

    /* Manejar el cambio de tamaño de la ventana */
    window.addEventListener('resize', () => {
      if (window.innerWidth > 1040) {
        menuButton.classList.remove('active'); // Desactiva el botón
        sidebar.classList.remove('active'); // Asegúrate de que el sidebar esté oculto
        sidebar.style.left = '-500px'; // Ocultar el sidebar
      }
    });
  }

  toggleSidebar() {
    this.isSidebarActive = !this.isSidebarActive;
  }

  buscarProveedor(): void {
    // Llama al servicio para buscar proveedores por nombre
    if (this.busquedaProveedor.trim()) { // Solo buscar si hay texto
      this.usuarioService.buscarProveedores(this.busquedaProveedor).subscribe(
        (data: PerfilRelacion[]) => {
          this.proveedoresEncontrados = data; // Actualiza la lista con los resultados
          this.proveedoresEncontrados.forEach(proveedor => {
            this.verificarSolicitudEnviada(proveedor.id); // Verifica si se ha enviado solicitud a cada proveedor
          });
        },
        error => {
          console.error('Error al buscar proveedores:', error);
          this.proveedoresEncontrados = []; // Limpia la lista en caso de error
        }
      );
    } else {
      this.proveedoresEncontrados = []; // Limpia la lista si el input está vacío
    }
  }

  /* Obtener Imagen de Perfil */
  obtenerImagenPerfil(userId: number, fileName: string): string {
    const url = `${baseUrl}/api/img/uploads/${userId}/${encodeURIComponent(fileName)}`;

    return url;
  }
  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/img/uploads/default/default.png`;
  }

  /* Solicitudes de atención */
  verificarSolicitudEnviada(proveedorId: number): void {

    if (this.usuario && this.usuario.perfil && this.usuario.perfil.idPerfil && proveedorId) {
      console.log('ID del perfil del usuario:', this.usuario.perfil.idPerfil);
      this.solicitudService.verificarSolicitudEnviada(this.usuario.perfil.idPerfil, proveedorId).subscribe(
        (response) => {
          if (response.exists) {
            this.solicitudesEnviadas.set(proveedorId, true);
          } else {
            this.solicitudesEnviadas.set(proveedorId, false);
          }
        },
        (error) => {
          console.error('Error al verificar la solicitud:', error);
        }
      );
    } else {
      console.error('Usuario no autenticado.');
    }
  }
  enviarSolicitud(perfilProveedorId: number): void {
    if (this.usuario && this.usuario.perfil && this.usuario.perfil.idPerfil) {
      // Imprimir la solicitud en la consola antes de enviarla
      const solicitud = {
        solicitanteId: this.usuario.perfil.idPerfil,
        solicitadoId: perfilProveedorId,
        fechaSolicitud: new Date(),
        aceptada: false
      };
      console.log('Solicitud a enviar:', solicitud);

      this.solicitudService.enviarSolicitudAProveedor(this.usuario.perfil.idPerfil, perfilProveedorId).subscribe(
        (response) => {
          console.log('Solicitud enviada:', response.mensaje);
          this.solicitudesEnviadas.set(perfilProveedorId, true);
          this.obtenerSolicitudesPendientes(this.usuario.perfil.idPerfil);

        },
        (error) => {
          console.error('Error al enviar la solicitud:', error);
        }
      );
    } else {
      console.error('Usuario no autenticado.');
    }
  }
  obtenerSolicitudesPendientes(perfilId: number): void {
    this.usuarioService.obtenerUsuariosConSolicitudesPendientes(perfilId).subscribe(
      (solicitudesPendientes) => {
        console.log('Solicitudes pendientes:', solicitudesPendientes);
        this.solicitudesPendientes = solicitudesPendientes;
      },
      (error) => {
        console.error('Error al obtener los usuarios con solicitudes pendientes:', error);
      }
    );
  }
  obtenerRelacionesComerciales(perfilId: number): void {
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

  /* Productos del proveedor */
  obtenerProductosProveedor(proveedorId: number): void {
    if (this.usuario) {
      this.usuarioService.obtenerProductosProveedor(this.usuario.id, proveedorId).subscribe(
        (productos) => {
          console.log('Productos del proveedor:', productos);


          this.productosProveedor = productos;
          this.clickedProveedorId = proveedorId;
          this.proveedorSeleccionado = this.clickedProveedorId;
        },
        (error) => {
          console.error('Error al obtener los productos del proveedor:', error);
        }
      );
    } else {
      console.error('Usuario no autenticado.');
    }
  }
  hoverProveedor(proveedorId: number): void {
    this.hoveredProveedorId = proveedorId;

  }
  leaveProveedor(proveedorId: number): void {
    this.hoveredProveedorId = null;
  }
  obtenerImagenProducto(userId: number, fileName: string): string {
    return `${baseUrl}/api/img/product/uploads/${userId}/${encodeURIComponent(fileName)}`;
  }

  /* Realizar Pedido a un proveedor */

  verPedido() {
    const items: Item[] = this.productosProveedor
      .filter(producto => producto.cantidad > 0)
      .map(producto => ({
        idItem: 0,
        idProducto: producto.idProducto,
        producto: producto,
        cantidad: producto.cantidad,
        precioUnitario: producto.precio,
        subtotal: parseFloat((producto.precio * producto.cantidad).toFixed(2))
      }));

    if (items.length === 0) {
      alert("Debe seleccionar al menos un producto con una cantidad válida.");
      return;
    }

    const total = this.totalPedido; // Usa el método para calcular el total

    const dialogRef = this.dialog.open(ResumenPedidoComponent, {
      data: {
        items: items,
        total: total,
        clienteId: this.usuario.id,
        proveedorId: this.proveedorSeleccionado
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      // Manejar lo que sucede después de cerrar el diálogo si es necesario
    });
  }
  get totalPedido(): number {
    return this.productosProveedor
      .filter(producto => producto.cantidad > 0)
      .reduce((acc, producto) => acc + producto.precio * producto.cantidad, 0);
  }
  aumentarCantidad(producto: any) {
    if (!producto.cantidad) {
      producto.cantidad = 0;
    }
    producto.cantidad++;
  }
  reducirCantidad(producto: any) {
    if (producto.cantidad > 0) {
      producto.cantidad--;
    }
  }
  get hayProductosSeleccionados(): boolean {
    return this.productosProveedor.some(producto => producto.cantidad > 0);
  }
}


/*




PRODUCTO NO DISNIBLE: sin stock

OFERTAS... por cantidad

SECCION DE OFERTAS.....




// realizarPedido() {
  //   // Crear la lista de items a partir de los productos seleccionados
  //   const items: Item[] = this.productosProveedor
  //     .filter(producto => producto.cantidad > 0) // Filtrar solo por cantidad > 0
  //     .map(producto => ({
  //       idItem: 0,
  //       idProducto: producto.idProducto,
  //       producto: producto,
  //       cantidad: producto.cantidad,
  //       precioUnitario: producto.precio,
  //       subtotal: parseFloat((producto.precio * producto.cantidad).toFixed(2))
  //     }));
  
  //   // Validar que haya al menos un producto con cantidad mayor a 0
  //   if (items.length === 0) {
  //     alert("Debe seleccionar al menos un producto con una cantidad válida.");
  //     return;
  //   }
  
  //   console.log("Items antes de enviar:", items);
  
  //   // Calcular subtotal y total, redondeados a dos decimales
  //   const subtotalPedido = parseFloat(items.reduce((acc, item) => acc + item.subtotal, 0).toFixed(2));
  //   const descuento = 0;
  //   const total = parseFloat((subtotalPedido - descuento).toFixed(2));
  //   const idPedido = 0;
  
  //   const pedido: Pedido = {
  //     idPedido,
  //     clienteId: this.usuario.id,
  //     proveedorId: this.proveedorSeleccionado,
  //     estado: 'NUEVO',
  //     fecha: new Date(),
  //     subtotalPedido,
  //     descuento,
  //     total,
  //     items
  //   };
  
  //   console.log("Pedido completo antes de enviar:", pedido);
  
  //   // Enviar el pedido al backend
  //   this.solicitudService.realizarPedido(pedido).subscribe(
  //     response => {
  //       console.log("Pedido realizado con éxito:", response);
  //       alert("Pedido realizado con éxito");
  //     },
  //     error => {
  //       console.error("Error al realizar el pedido:", error);
  //       alert("Error al realizar el pedido");
  //     }
  //   );
  // }



  // realizarPedido() {
  //   // Crear la lista de items a partir de los productos seleccionados
  //   const items: Item[] = this.productosProveedor
  //     .filter(producto => producto.seleccionado && producto.cantidad > 0)
  //     .map(producto => ({
  //       idItem: 0, // o asignar un ID si es necesario
  //       idProducto: producto.idProducto,
  //       producto: producto, // o ajustar según lo que se necesite
  //       cantidad: producto.cantidad,
  //       precioUnitario: producto.precio,     
   
  //       subtotal: parseFloat((producto.precio * producto.cantidad).toFixed(2)) // Cálculo correcto del subtotal
  //     }));

      

  //   if (items.length === 0) {
  //     alert("Debe seleccionar al menos un producto con una cantidad válida.");
  //     return;
  //   }

  //   console.log("Items antes de enviar:", items);


  //   // Calcular subtotal y total, redondeados a dos decimales
  //   const subtotalPedido = parseFloat(items.reduce((acc, item) => acc + item.subtotal, 0).toFixed(2));
  //   const descuento = 0; // Ajusta si necesitas un cálculo de descuento
  //   const total = parseFloat((subtotalPedido - descuento).toFixed(2));
  //   const idPedido = 0;

  //   const pedido: Pedido = {

  //     idPedido,
  //     clienteId: this.usuario.id, // ID del comercio (cliente)
  //     proveedorId: this.proveedorSeleccionado,// ID de la empresa (proveedor)
  //     estado: 'NUEVO',
  //     fecha: new Date(),
  //     subtotalPedido,
  //     descuento,
  //     total,
  //     items
  //   };

  //   console.log("Pedido completo antes de enviar:", pedido);


  //   // Enviar el pedido al backend
  //   this.solicitudService.realizarPedido(pedido).subscribe(
  //     response => {
  //       console.log("Pedido realizado con éxito:", response);
  //       alert("Pedido realizado con éxito");
  //     },
  //     error => {
  //       console.error("Error al realizar el pedido:", error);
  //       alert("Error al realizar el pedido");
  //     }
  //   );
  // }



*/ 
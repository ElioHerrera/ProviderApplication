import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { SolicitudService } from '../../services/solicitud.service';
import { Pedido } from '../../usuario.model';
import { Item } from '../../usuario.model';

import { CommonModule } from '@angular/common';




// Define el tipo para los datos que se pasan al diálogo
interface ResumenPedidoData {
  items: Item[];
  total: number;
  clienteId: number;  // Agregar clienteId aquí
  proveedorId: number; // Agregar proveedorId aquí
}


@Component({
  selector: 'app-recumen-pedido',
  standalone: true,
  imports: [

    CommonModule
  ],
  templateUrl: './resumen-pedido.component.html',
  styleUrl: './resumen-pedido.component.css'
})

export class ResumenPedidoComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: ResumenPedidoData, private solicitudService: SolicitudService) {}

  realizarPedido() {
    const pedido: Pedido = {
      idPedido: 0, // o generar un ID si es necesario
      clienteId: this.data.clienteId, // Usar el clienteId del diálogo
      proveedorId: this.data.proveedorId, // Usar el proveedorId del diálogo
      estado: 'NUEVO',
      fecha: new Date(),
      subtotalPedido: this.data.total, // El subtotal se puede calcular en el componente principal o aquí
      descuento: 0, // Ajustar si se necesita un descuento
      total: this.data.total,
      items: this.data.items
    };

    console.log("Pedido completo antes de enviar:", pedido);

    this.solicitudService.realizarPedido(pedido).subscribe(
      response => {
        console.log("Pedido realizado con éxito:", response);
        Swal.fire({
          title: 'Pedido realizado',
          text: 'Tu pedido ha sido realizado con éxito!',
          icon: 'success',
          confirmButtonText: 'Aceptar'
        });
      },
      error => {
        console.error("Error al realizar el pedido:", error);
        Swal.fire({
          title: 'Error',
          text: 'No se pudo realizar el pedido.',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    );
  }
}

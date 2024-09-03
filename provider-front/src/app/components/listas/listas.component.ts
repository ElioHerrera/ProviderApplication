import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';

import { ListaPrecios } from '../../usuario.model';
import { MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-listas',
  standalone: true,
  imports: [
    CommonModule, MatButtonModule, MatListModule, MatDialogModule
    
  ],
  templateUrl: './listas.component.html',
  styleUrl: './listas.component.css'
})
export class ListasComponent {

  listaSeleccionada: ListaPrecios | null = null;
  
  constructor(
    public dialogRef: MatDialogRef<ListasComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { clienteId: number, listasDePrecio: ListaPrecios[] }
  ) {}


  cancelar(): void {
    console.log('Cancelado');
    this.dialogRef.close();
  }

  confirmarSeleccion(): void {
    console.log('Lista seleccionada:', this.listaSeleccionada);
    this.dialogRef.close(this.listaSeleccionada);
  }


  // Método para cerrar el diálogo y devolver la lista de precios seleccionada
  seleccionarLista(lista: ListaPrecios): void {
    console.log('Lista seleccionada:', lista);
    this.listaSeleccionada = lista;
    this.dialogRef.close(lista);
  }
}
import { Component, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { NavhomeComponent } from '../../components/navhome/navhome.component';
import { NavprovComponent } from '../../components/navprov/navprov.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { Producto, Usuario } from '../../usuario.model';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRadioModule } from '@angular/material/radio';
import baseUrl from '../../services/helper';
import Swal from 'sweetalert2';

@Component({

  selector: 'app-productos',
  standalone: true,
  imports: [
    NavhomeComponent, NavprovComponent, CommonModule, FormsModule, MatButtonModule, MatIconModule, MatSlideToggleModule, MatRadioModule
  ],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.css'
})
export class ProductosComponent {

  usuario: any;
  esDealer: boolean = false;
  esProvider: boolean = false;
  productos: Producto[] = [];

  constructor(
    private usuarioService: UserService,
    private router: Router) { }

  ngOnInit(): void {
    // Recuperar los datos del usuario del almacenamiento local
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.usuario = JSON.parse(storedUser);
      console.log('Datos del usuario:', this.usuario); // Imprimimos los datos del usuario por consola
      const roles = this.usuario.roles.map((role: any) => role.roleEnum);
      this.esDealer = roles.includes('CLIENT');
      this.esProvider = roles.includes('PROVIDER');
      this.obtenerMisProductos(this.usuario.id);

    }
  }

  obtenerMisProductos(userId: number): void {
    this.usuarioService.obtenerProductosPorUsuarioId(userId).subscribe(
      (response) => {
        console.log('Lista de productos:', response);
        this.productos = response; // Asignar la respuesta al array de productos
      },
      (error) => {
        console.error('Error al obtener los productos:', error);
      }
    );
  }

  crearProducto(): void {
    this.usuarioService.crearProducto(this.usuario.id).subscribe(
      (response) => {
        console.log('Producto creado:', response);
        this.productos.push(response);
        this.obtenerMisProductos
      },
      (error) => {
        console.error('Error al crear el producto:', error);
        Swal.fire(
          'Error',
          'Hubo un problema al crear el producto.',
          'error'
        );
      }
    );
  }
  obtenerImagenProducto(userId: number, fileName: string): string {
    return `${baseUrl}/api/producto/uploads/${userId}/${encodeURIComponent(fileName)}`;
  }
  handleImgError(event: any): void {
    event.target.src = `${baseUrl}/api/producto/uploads/default/defaultProduct.jpg`;
  }
  agregarListaDePrecio() { 

  }

  editarProducto(index: number) {
    this.productos[index].editable = true;
    this.productos[index].editableImagen = true; // Habilitar edición de imagen también
  }
  actualizarProducto(index: number) {
    this.productos[index].editable = false;
    this.productos[index].editableImagen = false; // Deshabilitar edición de imagen
    const productoActualizado = this.productos[index];
    this.usuarioService.actualizarProducto(productoActualizado).subscribe(response => {
      console.log('Producto actualizado:', response);
    }, error => {
      console.error('Error al actualizar el producto:', error);
    });
  }
  editarImagenProducto(producto: Producto): void {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';
    input.onchange = (event: any) => {
      const file: File = event.target.files[0];
      if (file) {
        this.usuarioService.guardarImagenProducto(file, producto.idUsuario, producto.idProducto).subscribe(response => {
          console.log('Imagen actualizada:', response);
          producto.fotoProducto = response.fileName; // Actualiza el nombre de la foto en el producto
        }, error => {
          console.error('Error al actualizar la imagen:', error);
          Swal.fire(
            'Error',
            'Hubo un problema al actualizar la imagen del producto.',
            'error'
          );
        });
      }
    };
    input.click();
  }
  onFileSelected(event: any, producto: Producto) {
    const file: File = event.target.files[0];
    if (file) {
      this.usuarioService.guardarImagenProducto(file, producto.idUsuario, producto.idProducto).subscribe(response => {
        console.log('Imagen subida:', response);
        producto.fotoProducto = response.fileName;
      }, error => {
        console.error('Error al subir la imagen:', error);
        Swal.fire(
          'Error',
          'Hubo un problema al subir la imagen del producto.',
          'error'
        );
      });
    }
  }
  eliminarProducto(index: number) {
    const productoId = this.productos[index].idProducto;

    Swal.fire({
      title: '¿Está seguro?',
      text: "¡No podrá revertir esto!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.usuarioService.eliminarProducto(productoId).subscribe(response => {
          console.log('Producto eliminado:', response);
          this.productos.splice(index, 1);
          Swal.fire(
            '¡Eliminado!',
            'El producto ha sido eliminado.',
            'success'
          );
        }, error => {
          console.error('Error al eliminar el producto:', error);
          Swal.fire(
            'Error',
            'Hubo un problema al eliminar el producto.',
            'error'
          );
        });
      }
    });
  }

  alternarInterruptor(id: number, isEnabled: boolean): void {
    this.usuarioService.alternarHabilitado(id, isEnabled).subscribe(
      (data) => {
        console.log('Producto actualizado:', data);
      },
      (error) => {
        console.error('Error al actualizar el producto:', error);
      }
    );
  }


}
import { Component, Inject, ElementRef, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-imagen-perfil',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule],
  templateUrl: './imagen-perfil.component.html',
  styleUrl: './imagen-perfil.component.css'
})

export class ImagenPerfilComponent {

  @ViewChild('imagePreview')
  imagePreview!: ElementRef<HTMLImageElement>;

  selectedFile: File | null = null;
  previewUrl: string | ArrayBuffer | null = null;
  imageLoaded = false;
  isDragging = false;

  zoom: number = 1;
  posX: number = 0;
  posY: number = 0;

  constructor(
    public dialogRef: MatDialogRef<ImagenPerfilComponent>,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public data: { usuario: any }
  ) { }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;

      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewUrl = e.target?.result || null; // Asegura que tenga un valor o null
      };
      reader.readAsDataURL(file);
    }
  }

  getImageTransform(): string {
    return `scale(${this.zoom}) translate(${this.posX}px, ${this.posY}px)`;
  }

  guardarImagen(): void {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      formData.append('userId', this.data.usuario.id);

      this.userService.subirImagenPerfil(formData).subscribe(
        (response: any) => {
          Swal.fire('Éxito', 'Imagen de perfil actualizada', 'success');
          this.dialogRef.close({ fotoPerfilActualizada: response.fileName });
        },
        (error) => {
          Swal.fire('Error', 'No se pudo actualizar la imagen de perfil', 'error');
        }
      );
    }
  }
}
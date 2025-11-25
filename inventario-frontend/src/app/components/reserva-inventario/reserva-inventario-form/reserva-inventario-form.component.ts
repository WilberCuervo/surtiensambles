import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { ReservaService } from '../../../core/services/reserva-inventario.service';
import { ProductoService } from '../../../core/services/producto.service';
import { BodegaService } from '../../../core/services/bodega.service';
import { Producto } from '../../../core/models/producto.model';
import { Bodega } from '../../../core/models/bodega.model';

@Component({
  selector: 'app-reserva-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, 
    MatSelectModule, MatButtonModule, MatCardModule, MatDatepickerModule, MatNativeDateModule
  ],
  templateUrl: './reserva-inventario-form.component.html',
  styleUrls: ['./reserva-inventario-form.component.css']
})
export class ReservaInventarioFormComponent implements OnInit {

  reservaForm!: FormGroup;
  productos: Producto[] = [];
  bodegas: Bodega[] = [];
  saving = false;

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private reservaSvc = inject(ReservaService);
  private productoSvc = inject(ProductoService);
  private bodegaSvc = inject(BodegaService);

  ngOnInit(): void {
    this.initForm();
    this.loadCatalogos();
  }

  initForm() {
    this.reservaForm = this.fb.group({
      productoId: [null, Validators.required],
      bodegaId: [null, Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      nota: ['', Validators.required], // Obligamos a poner el nombre del cliente
      fechaExpiracion: [null],
      usuario: ['Admin'] 
    });
  }

  loadCatalogos() {
    this.bodegaSvc.getAll().subscribe(b => this.bodegas = b);
    // Carga inicial de productos (idealmente usarías autocomplete si son muchos)
    this.productoSvc.list({page: 0, size: 100}).subscribe(res => {
      this.productos = res.content;
    });
  }

  submit() {
    if (this.reservaForm.invalid) return;
    this.saving = true;

    this.reservaSvc.create(this.reservaForm.value).subscribe({
      next: () => {
        this.saving = false;
        this.router.navigate(['/reservas']);
      },
      error: (err) => {
        console.error(err);
        alert('Error: ' + (err.error?.message || 'No se pudo crear la reserva'));
        this.saving = false;
      }
    });
  }

  cancel() {
    this.router.navigate(['/reservas']);
  }
}
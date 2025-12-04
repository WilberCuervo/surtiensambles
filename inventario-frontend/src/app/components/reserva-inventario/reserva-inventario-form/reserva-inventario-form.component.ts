import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';

import { debounceTime, distinctUntilChanged, switchMap, filter, finalize } from 'rxjs/operators';

import { ReservaService } from '../../../core/services/reserva-inventario.service';
import { ProductoService } from '../../../core/services/producto.service';
import { BodegaService } from '../../../core/services/bodega.service';
import { Producto } from '../../../core/models/producto.model';
import { Bodega } from '../../../core/models/bodega.model';

@Component({
  selector: 'app-reserva-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, 
    MatCardModule, MatDatepickerModule, MatNativeDateModule,
    MatAutocompleteModule, MatIconModule
  ],
  templateUrl: './reserva-inventario-form.component.html',
  styleUrls: ['./reserva-inventario-form.component.css']
})
export class ReservaInventarioFormComponent implements OnInit {

  reservaForm!: FormGroup;
  
  productosFiltrados: Producto[] = [];
  bodegasFiltradas: Bodega[] = []; // Lista para el buscador de bodegas
  
  saving = false;
  loadingProductos = false;
  loadingBodegas = false;

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private reservaSvc = inject(ReservaService);
  private productoSvc = inject(ProductoService);
  private bodegaSvc = inject(BodegaService);

  ngOnInit(): void {
    this.initForm();
    this.setupAutocompleteProducto();
    this.setupAutocompleteBodega();
  }

  initForm() {
    this.reservaForm = this.fb.group({
      productoId: [null, Validators.required], // Guardará objeto Producto temporalmente
      bodegaId: [null, Validators.required],   // Guardará objeto Bodega temporalmente
      cantidad: [1, [Validators.required, Validators.min(1)]],
      nota: ['', Validators.required],
      fechaExpiracion: [null],
      usuario: ['Admin'] 
    });
  }

  setupAutocompleteProducto() {
    this.reservaForm.get('productoId')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(valor => typeof valor === 'string'),
        switchMap(valor => {
          this.loadingProductos = true;
          return this.productoSvc.list({ 
            page: 0, 
            size: 10, 
            search: valor,
            activo: true 
          }).pipe(finalize(() => this.loadingProductos = false));
        })
      )
      .subscribe({
        next: (res) => this.productosFiltrados = res.content,
        error: () => this.productosFiltrados = []
      });
  }

  setupAutocompleteBodega() {
    this.reservaForm.get('bodegaId')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(valor => typeof valor === 'string'),
        switchMap(valor => {
          this.loadingBodegas = true;
          // Usamos el método list del servicio de bodegas
          return this.bodegaSvc.list({ 
            page: 0, 
            size: 10, 
            search: valor,
            activo: true 
          }).pipe(finalize(() => this.loadingBodegas = false));
        })
      )
      .subscribe({
        next: (res) => this.bodegasFiltradas = res.content,
        error: () => this.bodegasFiltradas = []
      });
  }

  displayProducto(producto: any): string {
    if (!producto) return '';
    return producto.nombre ? `${producto.sku} - ${producto.nombre}` : '';
  }

  displayBodega(bodega: any): string {
    if (!bodega) return '';
    return bodega.nombre ? `${bodega.nombre}` : '';
  }

  submit() {
    if (this.reservaForm.invalid) {
      this.reservaForm.markAllAsTouched();
      return;
    }

    const prodVal = this.reservaForm.get('productoId')?.value;
    const bodegaVal = this.reservaForm.get('bodegaId')?.value;

    // Validar que seleccionaron objetos reales y no solo texto
    if (typeof prodVal === 'string' || !prodVal?.id) {
      alert("Selecciona un producto válido de la lista.");
      return;
    }
    if (typeof bodegaVal === 'string' || !bodegaVal?.id) {
      alert("Selecciona una bodega válida de la lista.");
      return;
    }

    this.saving = true;

    // Extraer solo los IDs para enviar al backend
    const formValue = { ...this.reservaForm.value };
    formValue.productoId = prodVal.id;
    formValue.bodegaId = bodegaVal.id;

    this.reservaSvc.create(formValue).subscribe({
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
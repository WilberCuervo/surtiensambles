import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';

import { debounceTime, distinctUntilChanged, switchMap, filter, finalize } from 'rxjs/operators';

import { MovimientoInventarioService } from '../../../core/services/movimiento-inventario.service';
import { ProductoService } from '../../../core/services/producto.service';
import { BodegaService } from '../../../core/services/bodega.service';

import { Producto } from '../../../core/models/producto.model';
import { Bodega } from '../../../core/models/bodega.model';

@Component({
  selector: 'app-movimiento-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, 
    MatCardModule, MatAutocompleteModule, MatIconModule
  ],
  templateUrl: './movimiento-inventario-form.component.html',
  styleUrls: ['./movimiento-inventario-form.component.css']
}) 
export class MovimientoInventarioFormComponent implements OnInit {

  movForm!: FormGroup;
  
  // Listas filtradas para los autocompletados
  productosFiltrados: Producto[] = []; 
  bodegasFiltradas: Bodega[] = [];        // Para Bodega normal
  bodegasOrigenFiltradas: Bodega[] = [];  // Para Origen (Transferencia)
  bodegasDestinoFiltradas: Bodega[] = []; // Para Destino (Transferencia)
  
  saving = false;
  loadingProductos = false;
  
  // Spinners independientes para cada campo de bodega
  loadingBodegas = false;
  loadingBodegasOrigen = false;
  loadingBodegasDestino = false;

  esTransferencia = false;
  fechaActual = new Date();

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private movimientoSvc = inject(MovimientoInventarioService);
  private productoSvc = inject(ProductoService);
  private bodegaSvc = inject(BodegaService);

  ngOnInit(): void {
    this.initForm();
    this.setupAutocompleteProducto();
    this.setupAutocompleteBodegas(); // Configurar los 3 buscadores de bodegas
  }

  initForm() {
    this.movForm = this.fb.group({
      tipo: ['ENTRADA', Validators.required],
      productoId: [null, Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      
      // Campos condicionales (guardan objeto Bodega temporalmente)
      bodegaId: [null], 
      bodegaOrigenId: [null],
      bodegaDestinoId: [null],

      nota: [''],
      referenciaTipo: [''],
      referenciaId: [''],
      usuario: ['Admin']
    });
    
    this.onTipoChange();
  }

  setupAutocompleteProducto() {
    this.movForm.get('productoId')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(valor => typeof valor === 'string'),
        switchMap(valor => {
          this.loadingProductos = true;
          return this.productoSvc.list({ page: 0, size: 10, search: valor, activo: true })
            .pipe(finalize(() => this.loadingProductos = false));
        })
      )
      .subscribe({
        next: (res) => this.productosFiltrados = res.content,
        error: () => this.productosFiltrados = []
      });
  }

  /**
   * Configura los escuchadores para los 3 posibles campos de bodega
   */
  setupAutocompleteBodegas() {
    // 1. Bodega Simple (Entrada/Salida)
    this.movForm.get('bodegaId')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(valor => typeof valor === 'string'),
        switchMap(valor => {
          this.loadingBodegas = true;
          return this.bodegaSvc.list({ page: 0, size: 10, search: valor, activo: true })
            .pipe(finalize(() => this.loadingBodegas = false));
        })
      ).subscribe({
        next: (res) => this.bodegasFiltradas = res.content,
        error: () => this.bodegasFiltradas = []
      });

    // 2. Bodega Origen (Transferencia)
    this.movForm.get('bodegaOrigenId')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(valor => typeof valor === 'string'),
        switchMap(valor => {
          this.loadingBodegasOrigen = true;
          return this.bodegaSvc.list({ page: 0, size: 10, search: valor, activo: true })
            .pipe(finalize(() => this.loadingBodegasOrigen = false));
        })
      ).subscribe({
        next: (res) => this.bodegasOrigenFiltradas = res.content,
        error: () => this.bodegasOrigenFiltradas = []
      });

    // 3. Bodega Destino (Transferencia)
    this.movForm.get('bodegaDestinoId')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(valor => typeof valor === 'string'),
        switchMap(valor => {
          this.loadingBodegasDestino = true;
          return this.bodegaSvc.list({ page: 0, size: 10, search: valor, activo: true })
            .pipe(finalize(() => this.loadingBodegasDestino = false));
        })
      ).subscribe({
        next: (res) => this.bodegasDestinoFiltradas = res.content,
        error: () => this.bodegasDestinoFiltradas = []
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

  onTipoChange() {
    const tipo = this.movForm.get('tipo')?.value;
    this.esTransferencia = (tipo === 'TRANSFERENCIA');

    const bodegaCtrl = this.movForm.get('bodegaId');
    const origenCtrl = this.movForm.get('bodegaOrigenId');
    const destinoCtrl = this.movForm.get('bodegaDestinoId');

    // Limpiar valores y validadores al cambiar de tipo
    if (this.esTransferencia) {
      bodegaCtrl?.clearValidators();
      bodegaCtrl?.setValue(null);
      
      origenCtrl?.setValidators(Validators.required);
      destinoCtrl?.setValidators(Validators.required);
    } else {
      bodegaCtrl?.setValidators(Validators.required);
      
      origenCtrl?.clearValidators();
      origenCtrl?.setValue(null);
      destinoCtrl?.clearValidators();
      destinoCtrl?.setValue(null);
    }

    bodegaCtrl?.updateValueAndValidity();
    origenCtrl?.updateValueAndValidity();
    destinoCtrl?.updateValueAndValidity();
  }

  submit() {
    if (this.movForm.invalid) {
      this.movForm.markAllAsTouched();
      return;
    }

    // Validaciones de selección de objetos
    const prodVal = this.movForm.get('productoId')?.value;
    if (typeof prodVal === 'string' || !prodVal?.id) {
      alert("Selecciona un producto válido de la lista.");
      return;
    }

    // Validación específica de bodegas según el tipo
    if (this.esTransferencia) {
      const origen = this.movForm.get('bodegaOrigenId')?.value;
      const destino = this.movForm.get('bodegaDestinoId')?.value;
      
      if (typeof origen === 'string' || !origen?.id) { alert("Selecciona una Bodega Origen válida."); return; }
      if (typeof destino === 'string' || !destino?.id) { alert("Selecciona una Bodega Destino válida."); return; }
      if (origen.id === destino.id) { alert("La bodega de origen y destino no pueden ser la misma."); return; }

    } else {
      const bodega = this.movForm.get('bodegaId')?.value;
      if (typeof bodega === 'string' || !bodega?.id) { alert("Selecciona una bodega válida."); return; }
    }

    this.saving = true;

    // Clonar y extraer IDs
    const formValue = { ...this.movForm.value };
    formValue.productoId = prodVal.id;

    if (this.esTransferencia) {
      formValue.bodegaOrigenId = formValue.bodegaOrigenId.id;
      formValue.bodegaDestinoId = formValue.bodegaDestinoId.id;
      delete formValue.bodegaId; // Limpiar campo no usado
    } else {
      formValue.bodegaId = formValue.bodegaId.id;
      delete formValue.bodegaOrigenId;
      delete formValue.bodegaDestinoId;
    }

    this.movimientoSvc.create(formValue).subscribe({
      next: () => {
        this.saving = false;
        this.router.navigate(['/movimientos']);
      },
      error: (err) => {
        console.error('Error registrando movimiento', err);
        alert('Error: ' + (err.error?.message || 'No se pudo registrar'));
        this.saving = false;
      }
    });
  }

  cancel() {
    this.router.navigate(['/movimientos']);
  }
}
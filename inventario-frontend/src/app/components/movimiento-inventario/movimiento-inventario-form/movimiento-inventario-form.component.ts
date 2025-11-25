import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

import { MovimientoInventarioService } from '../../../core/services/movimiento-inventario.service';
import { ProductoService } from '../../../core/services/producto.service';
// import { BodegaService } from '../../services/bodega.service'; // Asegúrate de tener este

// Modelos
import { Producto } from '../../../core/models/producto.model';
import { Bodega } from '../../../core/models/bodega.model';

@Component({
  selector: 'app-movimiento-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatCardModule
  ],
  templateUrl: './movimiento-inventario-form.component.html',
  styleUrls: ['./movimiento-inventario-form.component.css']
}) 
export class MovimientoInventarioFormComponent implements OnInit {

  movForm!: FormGroup;
  productos: Producto[] = [];
  bodegas: Bodega[] = []; // Asegúrate de cargar esto desde tu BodegaService
  
  saving = false;
  esTransferencia = false;
  fechaActual = new Date();

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private movimientoSvc = inject(MovimientoInventarioService);
  private productoSvc = inject(ProductoService);
  // private bodegaSvc = inject(BodegaService); // Inyéctalo cuando lo tengas

  ngOnInit(): void {
    this.initForm();
    this.loadCatalogos();
  }

  initForm() {
    this.movForm = this.fb.group({
      tipo: ['ENTRADA', Validators.required],
      productoId: [null, Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      
      // Campos condicionales (se validan según el tipo)
      bodegaId: [null], 
      bodegaOrigenId: [null],
      bodegaDestinoId: [null],

      nota: [''],
      referenciaTipo: [''],
      referenciaId: [''],
      usuario: ['Admin'] // Aquí podrías poner el usuario logueado
    });
    
    // Configurar validaciones iniciales
    this.onTipoChange();
  }

  loadCatalogos() {
    // 1. Cargar Productos
    this.productoSvc.list({page:0, size: 100}).subscribe(res => {
        // Ojo: Aquí cargo los primeros 100. En prod deberías usar autocomplete.
        this.productos = res.content;
    });

    // 2. Cargar Bodegas (Simulado si no tienes el servicio aún)
    // this.bodegaSvc.getAll().subscribe(b => this.bodegas = b);
    
    // MOCK TEMPORAL PARA QUE PRUEBES EL FRONT SI NO TIENES BODEGA SERVICE
    this.bodegas = [
      { id: 1, nombre: 'Bodega Central', codigo: 'B01', ubicacion: 'Centro', responsable: 'Juan' },
      { id: 2, nombre: 'Bodega Norte', codigo: 'B02', ubicacion: 'Norte', responsable: 'Ana' }
    ] as any;
  }

  /**
   * Lógica reactiva: Cuando cambia el tipo, ajustamos los validadores
   */
  onTipoChange() {
    const tipo = this.movForm.get('tipo')?.value;
    this.esTransferencia = (tipo === 'TRANSFERENCIA');

    const bodegaCtrl = this.movForm.get('bodegaId');
    const origenCtrl = this.movForm.get('bodegaOrigenId');
    const destinoCtrl = this.movForm.get('bodegaDestinoId');

    if (this.esTransferencia) {
      // Si es transferencia, requerimos origen y destino, limpiamos bodega simple
      bodegaCtrl?.clearValidators();
      bodegaCtrl?.setValue(null);
      
      origenCtrl?.setValidators(Validators.required);
      destinoCtrl?.setValidators(Validators.required);
    } else {
      // Si es Entrada/Salida/Ajuste, requerimos bodega simple
      bodegaCtrl?.setValidators(Validators.required);
      
      origenCtrl?.clearValidators();
      origenCtrl?.setValue(null);
      destinoCtrl?.clearValidators();
      destinoCtrl?.setValue(null);
    }

    // Actualizar estado de validación
    bodegaCtrl?.updateValueAndValidity();
    origenCtrl?.updateValueAndValidity();
    destinoCtrl?.updateValueAndValidity();
  }

  submit() {
    if (this.movForm.invalid) {
      this.movForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.movimientoSvc.create(this.movForm.value).subscribe({
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
  
  getSelectedProductoLabel(): string {
      const id = this.movForm.get('productoId')?.value;
      if(!id) return '';
      const p = this.productos.find(prod => prod.id === id);
      return p ? `${p.sku} - ${p.nombre}` : '';
  }
}
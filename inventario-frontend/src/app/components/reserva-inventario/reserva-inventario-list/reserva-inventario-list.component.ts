import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTooltipModule } from '@angular/material/tooltip';

import { ReservaService } from '../../../core/services/reserva-inventario.service';
import { BodegaService } from '../../../core/services/bodega.service';
import { ReservaInventario } from '../../../core/models/reserva-inventario.model';
import { Bodega } from '../../../core/models/bodega.model';

@Component({
  selector: 'app-reserva-list',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatTableModule, 
    MatButtonModule, MatIconModule, MatSelectModule, MatFormFieldModule, MatTooltipModule
  ],
  templateUrl: './reserva-inventario-list.component.html',
  styleUrls: ['./reserva-inventario-list.component.css']
})
export class ReservaInventarioListComponent implements OnInit {

  displayedColumns: string[] = ['referencia', 'fecha', 'producto', 'bodega', 'cantidad', 'nota', 'acciones'];
  reservas: ReservaInventario[] = [];
  bodegas: Bodega[] = [];
  bodegaFilterControl = new FormControl<number | null>(null);

  private reservaSvc = inject(ReservaService);
  private bodegaSvc = inject(BodegaService);
  private router = inject(Router);

  ngOnInit(): void {
    this.loadCatalogos();
    this.loadReservas();

    // Recargar cuando cambie el filtro de bodega
    this.bodegaFilterControl.valueChanges.subscribe(() => this.loadReservas());
  }

  loadCatalogos() {
    this.bodegaSvc.getAll().subscribe(b => this.bodegas = b);
  }

  loadReservas() {
    const bodegaId = this.bodegaFilterControl.value || undefined;
    this.reservaSvc.getActivas(bodegaId).subscribe(data => {
      this.reservas = data;
    });
  }

  new() {
    this.router.navigate(['/reservas/nueva']);
  }

  confirmar(r: ReservaInventario) {
    if (confirm(`¿El cliente pagó y se lleva los ${r.cantidad} items? (Esto descontará inventario)`)) {
      this.reservaSvc.confirmar(r.id).subscribe(() => {
        this.loadReservas(); // Recargar tabla
      });
    }
  }

  cancelar(r: ReservaInventario) {
    if (confirm(`¿Desea cancelar la reserva? (Los items volverán a estar disponibles)`)) {
      this.reservaSvc.cancelar(r.id).subscribe(() => {
        this.loadReservas(); // Recargar tabla
      });
    }
  }
}
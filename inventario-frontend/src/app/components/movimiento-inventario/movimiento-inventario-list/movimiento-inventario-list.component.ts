import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatPaginatorModule } from '@angular/material/paginator';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

// Importaciones correctas de tu servicio y modelo
import { MovimientoInventarioService, MovimientoSearchParams } from '../../../core/services/movimiento-inventario.service';
import { MovimientoInventario } from '../../../core/models/movimiento-inventario.model';

@Component({
  selector: 'app-movimiento-list',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, FormsModule, RouterModule,
    MatTableModule, MatButtonModule, MatIconModule, MatInputModule,
    MatFormFieldModule, MatPaginatorModule, MatDatepickerModule, MatNativeDateModule
  ],
  templateUrl: './movimiento-inventario-list.component.html',
  styleUrls: ['./movimiento-inventario-list.component.css']
})
export class MovimientoInventarioListComponent implements OnInit {

  displayedColumns: string[] = ['fecha', 'producto', 'bodega', 'tipo', 'cantidad', 'referencia'];
  movimientos: MovimientoInventario[] = [];
  
  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;
  loading = false;

  searchControl = new FormControl('');
  
  rangeForm = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  private movimientoSvc = inject(MovimientoInventarioService);
  private router = inject(Router);

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => this.resetPaginationAndLoad());

    this.rangeForm.valueChanges
      .pipe(debounceTime(500))
      .subscribe(() => this.resetPaginationAndLoad());

    this.loadData();
  }

  // --- AQUÍ ESTÁ EL MÉTODO QUE FALTABA ---
  resetPaginationAndLoad() {
    this.pageIndex = 0;
    this.loadData();
  }
  // ---------------------------------------

  loadData() {
    this.loading = true;
    const start = this.rangeForm.value.start || null;
    const end = this.rangeForm.value.end || null;
    const search = this.searchControl.value || '';

    // Usamos la interfaz correcta MovimientoSearchParams
    const params: MovimientoSearchParams = {
      page: this.pageIndex,
      size: this.pageSize,
      search: search,
      fechaInicio: start,
      fechaFin: end,
      sortBy: 'fecha',
      sortDirection: 'desc'
    };

    this.movimientoSvc.list(params).subscribe({
      next: res => {
        this.movimientos = res.content;
        this.totalItems = res.totalElements;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  onPageChange(event: any) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  new() {
    this.router.navigate(['/movimientos/nuevo']);
  }

  clearFilters() {
    this.searchControl.setValue('');
    this.rangeForm.reset();
    this.resetPaginationAndLoad();
  }

  getTipoClass(tipo: string): string {
    switch (tipo) {
      case 'ENTRADA': return 'badge-success';
      case 'SALIDA': return 'badge-danger';
      case 'TRANSFERENCIA': return 'badge-info';
      case 'AJUSTE': return 'badge-warning';
      default: return '';
    }
  }
}
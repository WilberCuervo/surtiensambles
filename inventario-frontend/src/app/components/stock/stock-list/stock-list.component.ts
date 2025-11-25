import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule, Router } from '@angular/router';
import { StockService, StockSearchParams } from '../../../core/services/stock.service';
import { Stock } from '../../../core/models/stock.model'; 
import { MatIconModule } from "@angular/material/icon";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-stock-list',
  standalone: true,
  imports: [
    CommonModule, 
    MatTableModule, 
    MatButtonModule, 
    RouterModule, 
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatPaginatorModule,
    ReactiveFormsModule
  ],
  templateUrl: './stock-list.component.html',
  styleUrls: ['./stock-list.component.css']
})
export class StockListComponent implements OnInit {
  stockRecords: Stock[] = [];
  displayedColumns = ['producto_sku', 'producto_nombre', 'bodega_nombre', 'cantidad_disponible', 'cantidad_reservada', 'ultima_actualizacion'];
  
  loading = false;

  searchControl = new FormControl('');
  
  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;
  // Añadimos las opciones de tamaño de página como un arreglo de números
  pageSizeOptions: number[] = [5, 10, 25, 50]; 

  sortField = 'id';
  sortDir: 'asc' | 'desc' = 'asc';

  private svc = inject(StockService);
  private router = inject(Router); 

  /**
     * Determina la clase CSS según el nivel de stock
     */
    getStockStatusClass(stock: any): string {
      const cantidad = stock.cantidadDisponible;
      // Si el nivelReorden es null o 0, asumimos 0 para evitar errores
      const minimo = stock.producto.nivelReorden || 0; 
      
      // 1. ROJO: Crítico (Menor o igual al mínimo)
      if (cantidad <= minimo) {
        return 'stock-critical'; 
      }
      
      // 2. AMARILLO: Advertencia (Cerca del mínimo, ej: hasta un 20% más)
      // Ejemplo: Si el mínimo es 10, advertimos si tiene entre 11 y 12.
      const margenAdvertencia = Math.ceil(minimo * 1.20); 
      
      // Si el mínimo es muy bajo (ej: 0), damos un margen fijo de 5 unidades
      const umbralAlerta = (minimo === 0) ? 5 : margenAdvertencia;

      if (cantidad <= umbralAlerta) {
        return 'stock-warning';
      }

      // 3. VERDE: Bien
      return 'stock-ok';
    }

  ngOnInit(): void { 
    this.searchControl.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => this.resetPaginationAndLoad());

    this.loadData(); 
  }
  
  private resetPaginationAndLoad(): void {
    this.pageIndex = 0;
    this.loadData();
  }

  loadData() {
    this.loading = true;

    const params: StockSearchParams = {
      page: this.pageIndex,
      size: this.pageSize,
      search: this.searchControl.value || '',
      sortBy: this.sortField,
      sortDirection: this.sortDir
    };
    
    this.svc.list(params).subscribe({ 
      next: res => { 
        this.stockRecords = res.content; 
        this.totalItems = res.totalElements; 
        this.loading = false; 
      }, 
      error: () => { this.loading = false; } 
    });
  }
  
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  onSort(col: string) {
    if (this.sortField === col) {
      this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = col;
      this.sortDir = 'asc';
    }
    this.loadData();
  }
  
  clearFilters() {
    this.searchControl.setValue('');
    this.pageIndex = 0;
    this.loadData();
  }


}

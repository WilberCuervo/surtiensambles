import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { ReactiveFormsModule, FormControl, FormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { ProductoService, ProductoSearchParams } from '../../../core/services/producto.service';
import { CategoriaService } from '../../../core/services/categoria.service';
import { Producto } from '../../../core/models/producto.model';
import { Categoria } from '../../../core/models/categoria.model';

@Component({
  selector: 'app-producto-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatPaginatorModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './producto-list.component.html',
  styleUrls: ['./producto-list.component.css']
})
export class ProductoListComponent implements OnInit {

  // Añadimos 'expand' a las columnas mostradas si quieres un botón específico para la expansión
  // displayedColumns: string[] = [..., 'descripcion', ..., 'acciones', 'expand']; 

  displayedColumns: string[] = [
    'sku',
    'nombre',
    'descripcion',
    'categoria',
    'unidad_medida',
    'precio_referencia',
    'nivel_reorden',
    'estado',
    'acciones'
  ];

  productos: Producto[] = [];
  categorias: Categoria[] = [];

  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;
  sortField = 'id';
  sortDir: 'asc' | 'desc' = 'asc';
  
  // Nuevo: Almacena el ID del producto cuya descripción está expandida. 
  // Null si ninguna está expandida, o si queremos manejarlo por fila.
  expandedDescriptionId: number | null = null; 
  

  searchControl = new FormControl('');
  categoriaFilterControl = new FormControl<number | null>(null);
  estadoFilterControl = new FormControl<boolean | null>(null);

  loading = false;

  private productoSvc = inject(ProductoService);
  private categoriaSvc = inject(CategoriaService);
  private router = inject(Router);

  ngOnInit(): void {
    this.categoriaSvc.getAll().subscribe(c => this.categorias = c);

    // Filtros
    this.estadoFilterControl.valueChanges.subscribe(() => this.resetPaginationAndLoad());
    this.categoriaFilterControl.valueChanges.subscribe(() => this.resetPaginationAndLoad());
    
    // Búsqueda con debounce
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
    // Reiniciamos la descripción expandida al cargar nuevos datos
    this.expandedDescriptionId = null; 

    const params: ProductoSearchParams = {
      page: this.pageIndex,
      size: this.pageSize,
      search: this.searchControl.value || '',
      categoriaId: this.categoriaFilterControl.value,
      activo: this.estadoFilterControl.value,
      sortBy: this.sortField, 
      sortDirection: this.sortDir
    };

    this.productoSvc.list(params).subscribe({
      next: res => {
        this.productos = res.content;
        this.totalItems = res.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  new() {
    this.router.navigate(['/productos/nuevo']);
  }

  edit(id?: number) {
    if (id) this.router.navigate(['/productos/editar', id]);
  }

  delete(id?: number) {
    if (!id) return;

    if (confirm('¿Desea eliminar este producto?')) {
      this.productoSvc.delete(id).subscribe(() => this.loadData());
    }
  }
  
  onPageChange(event: any) {
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
    this.categoriaFilterControl.setValue(null);
    this.estadoFilterControl.setValue(null);
    this.pageIndex = 0;
    this.loadData();
  }
  
  // Nuevo método para alternar la expansión de la descripción
  toggleDescription(productoId: number) {
    if (this.expandedDescriptionId === productoId) {
      this.expandedDescriptionId = null; // Contraer si ya estaba expandido
    } else {
      this.expandedDescriptionId = productoId; // Expandir este producto específico
    }
  }

  // Helper para el HTML/CSS
  isExpanded(productoId: number): boolean {
    return this.expandedDescriptionId === productoId;
  }
}

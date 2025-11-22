import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { ReactiveFormsModule, FormControl, FormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { ProductoService } from '../../../core/services/producto.service';
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
    MatPaginator,
    ReactiveFormsModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './producto-list.component.html',
  styleUrls: ['./producto-list.component.css']
})
export class ProductoListComponent implements OnInit {

  // Campos visibles en tabla
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

  // Paginación y búsqueda
  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;

  sortField = 'id';
  sortDir: 'asc' | 'desc' = 'asc';

  private svc = inject(ProductoService);
  private categoriaSvc = inject(CategoriaService);
  private router = inject(Router);

  searchControl = new FormControl('');
  selectedCategoria: number | null = null;
  statusFilterControl = new FormControl(null);

  loading = false;

  ngOnInit(): void {
    // Cargar categorías para filtros
    this.categoriaSvc.getAll().subscribe(c => this.categorias = c);

    this.statusFilterControl.valueChanges
      .pipe(distinctUntilChanged())
      .subscribe(() => {
        this.pageIndex = 0;
        this.loadData();
      });


    this.searchControl.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => {
        this.pageIndex = 0;
        this.loadData();
      });

    this.loadData();
  }

  loadData() {
    this.loading = true;

    const search = this.searchControl.value || '';
    // const categoria = this.selectedCategoria.value || null;
    const estado = this.statusFilterControl.value;
    const sort = `${this.sortField},${this.sortDir}`;

    this.svc.list(
      this.pageIndex,
      this.pageSize,
      search,
      // categoria,
      // sort,
      estado
    ).subscribe({
      next: res => {
        this.categorias = res.content;
        this.totalItems = res.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }


  // Acciones
  new() {
    this.router.navigate(['/productos/nuevo']);
  }

  edit(id?: number) {
    if (id) this.router.navigate(['/productos/editar', id]);
  }

  delete(id?: number) {
    if (!id) return;

    if (confirm('¿Desea eliminar este producto?')) {
      this.svc.delete(id).subscribe(() => this.loadData());
    }
  }

  // Paginación
  onPageChange(event: any) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  // Sort
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
    this.selectedCategoria = null;
    this.statusFilterControl.reset(null);
    this.pageIndex = 0;
    this.loadData();
  }
}

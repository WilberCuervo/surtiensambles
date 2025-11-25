import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginator } from '@angular/material/paginator';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { CategoriaService } from '../../../core/services/categoria.service';
import { Categoria } from '../../../core/models/categoria.model';

@Component({
  selector: 'app-categoria-list',
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
    RouterModule
  ],
  templateUrl: './categoria-list.component.html',
  styleUrls: ['./categoria-list.component.css']
})
export class CategoriaListComponent implements OnInit {

  displayedColumns: string[] = ['nombre', 'descripcion', 'estado', 'acciones'];

  categorias: Categoria[] = [];
  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;

  sortField = 'id';
  sortDir: 'asc' | 'desc' = 'asc';

  searchControl = new FormControl('');
  estadoFilterControl = new FormControl(null);

  loading = false;

  private svc = inject(CategoriaService);
  private router = inject(Router);

  ngOnInit(): void {
    // Filtro estado
    this.estadoFilterControl.valueChanges.subscribe(() => {
      this.pageIndex = 0;
      this.loadData();
    });

    // Búsqueda con debounce
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
    const activo = this.estadoFilterControl.value;
    const sort = `${this.sortField},${this.sortDir}`;

    this.svc.list(this.pageIndex, this.pageSize, search, sort, activo).subscribe({
      next: res => {
        this.categorias = res.content;
        this.totalItems = res.totalElements;
        this.loading = false;
      },
      error: () => (this.loading = false)
    });
  }

  new() {
    this.router.navigate(['/categorias/nueva']);
  }

  edit(id: number) {
    this.router.navigate(['/categorias/editar', id]);
  }

  delete(id: number) {
    if (confirm('¿Eliminar categoría?')) {
      this.svc.delete(id).subscribe(() => this.loadData());
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
    this.estadoFilterControl.setValue(null);
    this.pageIndex = 0;
    this.loadData();
  }
}

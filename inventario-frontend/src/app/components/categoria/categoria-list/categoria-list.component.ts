import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { RouterModule, Router } from '@angular/router';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { CategoriaService } from '../../../core/services/categoria.service';
import { Categoria } from '../../../core/models/categoria.model';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-categoria-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatOption,
    MatSelectModule,
    ReactiveFormsModule,
    RouterModule
  ],
  templateUrl: './categoria-list.component.html',
  styleUrls: ['./categoria-list.component.css']
})
export class CategoriaListComponent implements OnInit {

  displayedColumns = ['id', 'nombre', 'descripcion', 'estado', 'acciones'];

  categorias: Categoria[] = [];

  searchControl = new FormControl<string>('', { nonNullable: true });
  statusFilterControl = new FormControl(null);

  // PAGINACIÓN
  totalItems = 0;
  pageSize = 10;
  pageIndex = 0;

  sortField = 'id';
  sortDir: 'asc' | 'desc' = 'asc';

  loading = false;

  private svc = inject(CategoriaService);
  private router = inject(Router);

  ngOnInit(): void {

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
    const estado = this.statusFilterControl.value;
    const sort = `${this.sortField},${this.sortDir}`;

    this.svc.list(
      this.pageIndex,
      this.pageSize,
      search,
      sort,
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

  new() {
    this.router.navigate(['/categorias/nueva']);
  }

  edit(id: number) {
    this.router.navigate(['/categorias/editar/' + id]);
  }

  delete(id: number) {
    if (confirm('¿Eliminar categoría?')) {
      this.svc.delete(id).subscribe(() => this.loadData());
    }
  }

  clearFilters() {
    this.searchControl.setValue('');
    this.statusFilterControl.reset(null);
    this.pageIndex = 0;
    this.loadData();
  }
}

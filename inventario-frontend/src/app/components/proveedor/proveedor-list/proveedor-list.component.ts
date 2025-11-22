import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { ProveedorService } from '../../../core/services/proveedor.service';
import { Proveedor } from '../../../core/models/proveedor.model';

@Component({
  selector: 'app-proveedor-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatOption,
    MatSelectModule,
    ReactiveFormsModule
  ],
  templateUrl: './proveedor-list.component.html',
  styleUrls: ['./proveedor-list.component.css']
})
export class ProveedorListComponent implements OnInit {

  displayedColumns = [
    'nombre',
    'nit',
    'telefono',
    'email',
    'direccion',
    'estado',
    'acciones'
  ];

  proveedores: Proveedor[] = [];

  searchControl = new FormControl('');
  statusFilterControl = new FormControl(null);
  
  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;

  sortField = 'id';
  sortDir: 'asc' | 'desc' = 'asc';

  loading = false;

  private svc = inject(ProveedorService);
  private router = inject(Router);

  ngOnInit(): void {

    this.statusFilterControl.valueChanges
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
        this.proveedores = res.content;
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
    this.router.navigate(['/proveedores/nuevo']);
  }

  edit(id?: number) {
    if (id) this.router.navigate(['/proveedores/editar', id]);
  }

  delete(id?: number) {
    if (!id) return;

    if (confirm('¿Desea eliminar este proveedor?')) {
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

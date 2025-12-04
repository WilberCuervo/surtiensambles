import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule, Router } from '@angular/router';
import { BodegaService, BodegaSearchParams } from '../../../core/services/bodega.service';
import { Bodega } from '../../../core/models/bodega.model';
import { MatIconModule } from "@angular/material/icon";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-bodega-list',
  standalone: true,
  imports: [
    CommonModule, 
    MatTableModule, 
    MatButtonModule, 
    RouterModule, 
    MatIconModule,
    MatInputModule,       
    MatFormFieldModule,   
    MatSelectModule,     
    MatPaginatorModule,  
    ReactiveFormsModule  
  ],
  templateUrl: './bodega-list.component.html',
  styleUrls: ['./bodega-list.component.css']
})
export class BodegaListComponent implements OnInit {
  bodegas: Bodega[] = [];
  
  displayedColumns = ['codigo','nombre','ubicacion','responsable','estado','acciones'];
  
  loading = false;

  searchControl = new FormControl('');
  statusFilterControl = new FormControl<boolean | null>(null);
  
  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;

  sortField = 'id';
  sortDir: 'asc' | 'desc' = 'asc';

  private svc = inject(BodegaService);
  private router = inject(Router);

  ngOnInit(): void { 
    this.statusFilterControl.valueChanges
     .subscribe(() => this.resetPaginationAndLoad());
    
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

    const params: BodegaSearchParams = {
      page: this.pageIndex,
      size: this.pageSize,
      search: this.searchControl.value || '',
      activo: this.statusFilterControl.value,
      sortBy: this.sortField,
      sortDirection: this.sortDir
    };
    
    this.svc.list(params).subscribe({ 
      next: res => { 
        this.bodegas = res.content; 
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
    this.statusFilterControl.setValue(null);
    this.pageIndex = 0;
    this.loadData();
  }


  nuevo() { this.router.navigate(['/bodegas/nueva']); }
  editar(id?: number) { if (id) this.router.navigate(['/bodegas/editar', id]); }
  eliminar(id?: number) {
    if (!id) return;
    if (!confirm('Confirmar eliminar')) return;
    this.svc.delete(id).subscribe(() => this.loadData());
  }
}

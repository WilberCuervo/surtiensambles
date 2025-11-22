import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule, Router } from '@angular/router';
import { BodegaService } from '../../../core/services/bodega.service';
import { Bodega } from '../../../core/models/bodega.model';
import { MatIconModule } from "@angular/material/icon";

@Component({
  selector: 'app-bodega-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, RouterModule, MatIconModule],
  templateUrl: './bodega-list.component.html',
  styleUrls: ['./bodega-list.component.css']
})
export class BodegaListComponent implements OnInit {
  bodegas: Bodega[] = [];
  displayed = ['codigo','nombre','ubicacion','responsable','acciones'];
  loading = false;

  constructor(private svc: BodegaService, private router: Router) {}

  ngOnInit(): void { this.load(); }
  load() {
    this.loading = true;
    this.svc.list().subscribe({ next: d => { this.bodegas = d; this.loading = false; }, error: () => { this.loading = false; } });
  }
  nuevo() { this.router.navigate(['/bodegas/nueva']); }
  editar(id?: number) { if (id) this.router.navigate(['/bodegas/editar', id]); }
  eliminar(id?: number) {
    if (!id) return;
    if (!confirm('Confirmar eliminar')) return;
    this.svc.delete(id).subscribe(() => this.load());
  }
}

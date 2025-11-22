import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';

import { ProductoService } from '../../../core/services/producto.service';
import { CategoriaService } from '../../../core/services/categoria.service';
import { Producto } from '../../../core/models/producto.model';
import { Categoria } from '../../../core/models/categoria.model';

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule, MatCardModule],
  templateUrl: './producto-form.component.html',
  styleUrls: ['./producto-form.component.css']
})
export class ProductoFormComponent implements OnInit {

  producto: Producto = {
    sku: '',
    nombre: '',
    descripcion: '',
    unidad_medida: '',
    precio_referencia: 0,
    nivel_reorden: 0,
    categoria: null,
    activo: true
  };

  categorias: Categoria[] = [];
  productoId?: number;
  saving = false;

  constructor(
    private svc: ProductoService,
    private categoriaSvc: CategoriaService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Cargar categorías
    this.categoriaSvc.getAll().subscribe(data => this.categorias = data);

    // Revisar si estamos editando
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.productoId = +id;
      this.svc.get(this.productoId).subscribe(p => this.producto = p);
    }
  }

  submit() {
    this.saving = true;

    // Crear un payload compatible con el backend
    const payload: Producto = {
      ...this.producto,
      categoria: this.producto.categoria ? { id: this.producto.categoria.id } : null
    };

    if (this.productoId) {
      this.svc.update(this.productoId, payload).subscribe({
        next: () => { this.saving = false; this.router.navigate(['/productos']); },
        error: () => { this.saving = false; }
      });
    } else {
      this.svc.create(payload).subscribe({
        next: () => { this.saving = false; this.router.navigate(['/productos']); },
        error: () => { this.saving = false; }
      });
    }
  }

  cancel() {
    this.router.navigate(['/productos']);
  }
}

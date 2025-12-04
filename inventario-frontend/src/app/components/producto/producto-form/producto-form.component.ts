import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ProductoService } from '../../../core/services/producto.service';
import { CategoriaService } from '../../../core/services/categoria.service';
import { Producto } from '../../../core/models/producto.model';
import { Categoria } from '../../../core/models/categoria.model';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule,
    RouterModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatSelectModule, 
    MatCardModule,
    MatCheckboxModule
  ],
  templateUrl: './producto-form.component.html',
  styleUrls: ['./producto-form.component.css']
})
export class ProductoFormComponent implements OnInit {

  // Definimos nuestro FormGroup
  productoForm!: FormGroup;
  categorias: Categoria[] = [];
  productoId?: number;
  saving = false;

  private svc = inject(ProductoService);
  private categoriaSvc = inject(CategoriaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  ngOnInit(): void {
    this.initForm();
    
    // Cargar categorías primero
    this.categoriaSvc.getAll().subscribe(data => this.categorias = data);

    // Cargar datos del producto si es edición
    this.route.params.pipe(
      switchMap(params => {
        if (params['id']) {
          this.productoId = +params['id'];
          return this.svc.get(this.productoId);
        }
        return of(null);
      })
    ).subscribe(producto => {
      if (producto) {
        // Rellenamos el formulario con los datos recibidos
        this.productoForm.patchValue({
          ...producto,
          categoriaId: producto.categoria?.id || null 
        });
      }
    });
  }

  // Método para inicializar la estructura del formulario con validaciones
  initForm(): void {
    this.productoForm = this.fb.group({
      sku: ['', Validators.required],
      nombre: ['', Validators.required],
      descripcion: [''],
      unidadMedida: [''],
      precioReferencia: [0, [Validators.required, Validators.min(0)]],
      nivelReorden: [0, [Validators.required, Validators.min(0)]],
      activo: [true],
      categoriaId: [null, Validators.required]
    });
  }

  submit() {
    if (this.productoForm.invalid) {
      this.productoForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    const formValue = this.productoForm.value;

    const payload: Producto = {
      ...formValue,
      categoria: formValue.categoriaId ? { id: formValue.categoriaId } : null,
      categoriaId: undefined 
    };

    if (this.productoId) {
      this.svc.update(this.productoId, payload).subscribe({
        next: () => { this.saving = false; this.router.navigate(['/productos']); },
        error: (err) => { console.error(err); this.saving = false; }
      });
    } else {
      this.svc.create(payload).subscribe({
        next: () => { this.saving = false; this.router.navigate(['/productos']); },
        error: (err) => { console.error(err); this.saving = false; }
      });
    }
  }

  cancel() {
    this.router.navigate(['/productos']);
  }
}

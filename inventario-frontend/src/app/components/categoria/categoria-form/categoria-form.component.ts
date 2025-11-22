import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule,Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoriaService } from '../../../core/services/categoria.service';
import { Categoria } from '../../../core/models/categoria.model';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-categoria-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatOption,
    MatSelectModule
],
  templateUrl: './categoria-form.component.html',
  styleUrls: ['./categoria-form.component.css']
})
export class CategoriaFormComponent implements OnInit {

  form!: FormGroup;
  id?: number;
  nombreDisponible: boolean = true;
  loadingNombre = false;
  saving = false;

  private svc = inject(CategoriaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  ngOnInit(): void {
      this.initForm();
  
      const id = this.route.snapshot.params['id'];
      if (id) {
        this.id = +id;
        this.svc.get(this.id).subscribe(p => {
          this.form.patchValue(p);
        });
      }
  
      // Validación de Nombre en tiempo real
      this.form.get('nombre')?.valueChanges
        .pipe(debounceTime(400))
        .subscribe(nombre => {
          if (!nombre) return;
          this.verificarNombre(nombre);
        });
    }

  initForm() {
    this.form = new FormGroup({
      nombre: new FormControl('', [Validators.required, Validators.minLength(2)]),
      descripcion: new FormControl('', [Validators.required, Validators.minLength(2)]),
      activo: new FormControl(true, Validators.required)
    });
  }

  verificarNombre(nombre: string) {
    if (this.id) return; // No validar si estamos editando

    this.loadingNombre = true;

    this.svc.existeNombre(nombre).subscribe(existe => {
      this.nombreDisponible = !existe;
      this.loadingNombre = false;

      if (existe) {
        this.form.get('nombre')?.setErrors({ nombreDuplicado: true });
      }
    });
  }

  submit() {
      if (this.form.invalid) return;
  
      this.saving = true;
      const data: Categoria = this.form.value;
  
      if (this.id) {
        this.svc.update(this.id, data).subscribe({
          next: () => this.finalizar(),
          error: () => this.saving = false
        });
      } else {
        this.svc.create(data).subscribe({
          next: () => this.finalizar(),
          error: () => this.saving = false
        });
      }
    }

    finalizar() {
    this.saving = false;
    this.router.navigate(['/categorias']);
  }

  cancelar() {
    this.router.navigate(['/categorias']);
  }
}

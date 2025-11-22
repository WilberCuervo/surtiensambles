import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { ProveedorService } from '../../../core/services/proveedor.service';
import { Proveedor } from '../../../core/models/proveedor.model';

@Component({
  selector: 'app-proveedor-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './proveedor-form.component.html',
  styleUrls: ['./proveedor-form.component.css']
})
export class ProveedorFormComponent implements OnInit {

  form!: FormGroup;
  proveedorId?: number;
  nitDisponible: boolean = true;
  loadingNit = false;
  saving = false;

  private svc = inject(ProveedorService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  ngOnInit(): void {
    this.initForm();

    const id = this.route.snapshot.params['id'];
    if (id) {
      this.proveedorId = +id;
      this.svc.get(this.proveedorId).subscribe(p => {
        this.form.patchValue(p);
      });
    }

    // Validación de NIT en tiempo real
    this.form.get('nit')?.valueChanges
      .pipe(debounceTime(400))
      .subscribe(nit => {
        if (!nit) return;
        this.verificarNit(nit);
      });
  }

  initForm() {
    this.form = new FormGroup({
      nombre: new FormControl('', [Validators.required, Validators.minLength(2)]),
      nit: new FormControl('', [Validators.required]),
      telefono: new FormControl(''),
      email: new FormControl('', [Validators.email]),
      direccion: new FormControl(''),
      estado: new FormControl(true, Validators.required)
    });
  }

  verificarNit(nit: string) {
    if (this.proveedorId) return; // No validar si estamos editando

    this.loadingNit = true;

    this.svc.existeNit(nit).subscribe(existe => {
      this.nitDisponible = !existe;
      this.loadingNit = false;

      if (existe) {
        this.form.get('nit')?.setErrors({ nitDuplicado: true });
      }
    });
  }

  submit() {
    if (this.form.invalid) return;

    this.saving = true;
    const data: Proveedor = this.form.value;

    if (this.proveedorId) {
      this.svc.update(this.proveedorId, data).subscribe({
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
    this.router.navigate(['/proveedores']);
  }

  cancelar() {
    this.router.navigate(['/proveedores']);
  }
}

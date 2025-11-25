import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms'; 

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { BodegaService } from '../../../core/services/bodega.service';
import { Bodega } from '../../../core/models/bodega.model';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-bodega-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule,
    RouterModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule
  ],
  templateUrl: './bodega-form.component.html',
  styleUrls: ['./bodega-form.component.css']
})
export class BodegaFormComponent implements OnInit {

  form!: FormGroup
  editingId?: number;
  saving = false;
  codigoDisponible = true;
  loadingCodigo = false;


  private svc = inject(BodegaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  ngOnInit(): void {
    this.initForm();
    
    const id = this.route.snapshot.params['id'];
    if (id) { 
      this.editingId = +id; 
      this.svc.get(this.editingId).subscribe(b => {
        this.form.patchValue(b); 
      }); 
    }

    // Validación de código en tiempo real
    this.form.get('codigo')?.valueChanges
      .pipe(debounceTime(400))
      .subscribe(codigo => {
        if (!codigo || this.editingId) return; 
        this.verificarCodigo(codigo);
      });
  }

  // Método para inicializar la estructura del formulario con validaciones
  initForm() {
    this.form = this.fb.group({
      codigo: ['', [Validators.required]],
      nombre: ['', [Validators.required]],
      ubicacion: [''],
      responsable: [''],
      activo: [true]
    });
  }
  
  verificarCodigo(codigo: string) {
    this.loadingCodigo = true;
    this.svc.existeCodigo(codigo).subscribe(existe => {
      this.codigoDisponible = !existe;
      this.loadingCodigo = false;

      if (existe) {
        this.form.get('codigo')?.setErrors({ codigoDuplicado: true });
      } else {
        if (this.form.get('codigo')?.hasError('codigoDuplicado')) {
           this.form.get('codigo')?.updateValueAndValidity();
        }
      }
    });
  }


  submit() {
    if (this.form.invalid) {
        this.form.markAllAsTouched();
        return;
    }

    this.saving = true;
    const data: Bodega = this.form.value;

    if (this.editingId) {
      this.svc.update(this.editingId, data).subscribe({ 
        next: () => this.router.navigate(['/bodegas']), 
        error: (err) => { console.error(err); this.saving = false; }
      });
    } else {
      this.svc.create(data).subscribe({ 
        next: () => this.router.navigate(['/bodegas']), 
        error: (err) => { console.error(err); this.saving = false; }
      });
    }
  }

  cancel() { this.router.navigate(['/bodegas']); }
}

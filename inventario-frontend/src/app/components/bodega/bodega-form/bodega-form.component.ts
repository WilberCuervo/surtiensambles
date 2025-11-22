import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { BodegaService } from '../../../core/services/bodega.service';
import { Bodega } from '../../../core/models/bodega.model';
@Component({
  selector: 'app-bodega-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './bodega-form.component.html',
  styleUrls: ['./bodega-form.component.css']
})
export class BodegaFormComponent implements OnInit {
  bodega: Bodega = { codigo: '', nombre: '' };
  editingId?: number;
  saving = false;

  constructor(private svc: BodegaService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) { this.editingId = +id; this.svc.get(this.editingId).subscribe(b => this.bodega = b); }
  }

  submit() {
    this.saving = true;
    if (this.editingId) {
      this.svc.update(this.editingId, this.bodega).subscribe({ next: () => this.router.navigate(['/bodegas']), error: () => this.saving = false });
    } else {
      this.svc.create(this.bodega).subscribe({ next: () => this.router.navigate(['/bodegas']), error: () => this.saving = false });
    }
  }

  cancel() { this.router.navigate(['/bodegas']); }
}

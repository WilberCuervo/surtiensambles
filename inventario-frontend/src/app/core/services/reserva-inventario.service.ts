import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/enviroments';
import { ReservaInventario, ReservaRequest } from '../models/reserva-inventario.model';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private apiUrl = `${environment.apiUrl}/reservas`;
  private http = inject(HttpClient);

  /**
   * Obtener reservas ACTIVAS (Opcional: filtrar por bodega)
   */
  getActivas(bodegaId?: number): Observable<ReservaInventario[]> {
    let params = new HttpParams();
    if (bodegaId) {
      params = params.set('bodegaId', bodegaId.toString());
    }
    return this.http.get<ReservaInventario[]>(`${this.apiUrl}/activas`, { params });
  }

  create(reserva: ReservaRequest): Observable<ReservaInventario> {
    return this.http.post<ReservaInventario>(this.apiUrl, reserva);
  }

  // Convertir Reserva en Venta (Salida de stock)
  confirmar(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/confirmar`, {});
  }

  // Cancelar Reserva (Devolver al stock)
  cancelar(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/cancelar`, {});
  }
}
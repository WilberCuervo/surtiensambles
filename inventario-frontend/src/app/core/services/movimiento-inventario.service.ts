import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MovimientoInventario, MovimientoRequest } from '../models/movimiento-inventario.model';
import { APP_CONFIG } from '../../config/app.config';

// Asegúrate de tener una interfaz genérica para la respuesta paginada de Spring.
// Si no la tienes en un archivo compartido, puedes definirla aquí o usar 'any'.
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number; 
  size: number;
  number: number;
}

export interface MovimientoSearchParams {
  page: number;
  size: number;
  search?: string;
  fechaInicio?: Date | null;
  fechaFin?: Date | null;
  sortBy?: string;
  sortDirection?: string;
}

@Injectable({
  providedIn: 'root'
})
export class MovimientoInventarioService {

  private apiUrl = `${APP_CONFIG.apiBaseUrl}/movimientos`;
  private http = inject(HttpClient);

  /**
   * Obtener el historial (Kardex) con paginación y filtros de fecha
   */
  list(params: MovimientoSearchParams): Observable<PageResponse<MovimientoInventario>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString())
      .set('sortBy', params.sortBy || 'fecha')
      .set('sortDirection', params.sortDirection || 'desc');

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }

    // Convertimos el objeto Date de JS al formato YYYY-MM-DD para Java
    if (params.fechaInicio) {
      httpParams = httpParams.set('fechaInicio', this.formatDate(params.fechaInicio));
    }

    if (params.fechaFin) {
      httpParams = httpParams.set('fechaFin', this.formatDate(params.fechaFin));
    }

    return this.http.get<PageResponse<MovimientoInventario>>(this.apiUrl, { params: httpParams });
  }

  /**
   * Registrar un nuevo movimiento (Entrada, Salida, Transferencia, etc.)
   */
  create(movimiento: MovimientoRequest): Observable<MovimientoInventario> {
    return this.http.post<MovimientoInventario>(this.apiUrl, movimiento);
  }

  // --- Helpers ---

  /**
   * Formatea un objeto Date a string "YYYY-MM-DD"
   */
  private formatDate(date: Date): string {
    // toISOString devuelve algo como "2023-11-24T18:00:00.000Z"
    // split('T')[0] nos deja con "2023-11-24"
    return date.toISOString().split('T')[0];
  }
}
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app.config';
import { Stock } from '../models/stock.model';

export interface StockSearchParams {
  page?: number;
  size?: number;
  search?: string;
  sortBy?: string;
  sortDirection?: string;
}

@Injectable({
  providedIn: 'root'
})
export class StockService {

  private base = `${APP_CONFIG.apiBaseUrl}/stock`;

  constructor(private http: HttpClient) { }

  /**
   * Búsqueda avanzada con paginación, filtros y sort usando un objeto de opciones.
   * Llama al endpoint GET /api/stock
   */
  list(params: StockSearchParams): Observable<any> {
    let httpParams = new HttpParams();

    // Asignar valores por defecto si no se proporcionan
    httpParams = httpParams.set('page', params.page ?? 0);
    httpParams = httpParams.set('size', params.size ?? 10);
    httpParams = httpParams.set('search', params.search ?? '');
    httpParams = httpParams.set('sortBy', params.sortBy ?? 'id');
    httpParams = httpParams.set('sortDirection', params.sortDirection ?? 'asc');

    return this.http.get<any>(this.base, { params: httpParams });
  }

  get(id: number): Observable<Stock> {
    return this.http.get<Stock>(`${this.base}/${id}`);
  }
  
  // Nota: No hay métodos create, update o delete aquí, ya que el stock se gestiona mediante Movimientos.
}

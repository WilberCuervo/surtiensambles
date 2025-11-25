import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app.config';
import { Bodega } from '../models/bodega.model';

export interface BodegaSearchParams {
  page?: number;
  size?: number;
  search?: string;
  activo?: boolean | null;
  sortBy?: string;
  sortDirection?: string;
}

@Injectable({
  providedIn: 'root'
})
export class BodegaService {

  private base = `${APP_CONFIG.apiBaseUrl}/bodegas`;

  constructor(private http: HttpClient) { }

  /**
   * Búsqueda avanzada con paginación, filtros y sort usando un objeto de opciones.
   * Llama al endpoint GET /api/bodegas
   */
  list(params: BodegaSearchParams): Observable<any> {
    let httpParams = new HttpParams();

    // Asignar valores por defecto si no se proporcionan
    httpParams = httpParams.set('page', params.page ?? 0);
    httpParams = httpParams.set('size', params.size ?? 10);
    httpParams = httpParams.set('search', params.search ?? '');
    httpParams = httpParams.set('sortBy', params.sortBy ?? 'id');
    httpParams = httpParams.set('sortDirection', params.sortDirection ?? 'asc');

    if (params.activo !== null && params.activo !== undefined) {
      httpParams = httpParams.set('activo', params.activo.toString());
    }

    return this.http.get<any>(this.base, { params: httpParams });
  }
  

  getAllSimpleList(activo?: boolean | null): Observable<Bodega[]> {
    let params = new HttpParams();
    if (activo !== null && activo !== undefined) {
      params = params.set('activo', activo.toString());
    }
    return this.http.get<Bodega[]>(`${this.base}/all`, { params });
  }

  get(id: number): Observable<Bodega> {
    return this.http.get<Bodega>(`${this.base}/${id}`);
  }

  create(data: Bodega): Observable<Bodega> {
    return this.http.post<Bodega>(this.base, data);
  }

  update(id: number, data: Bodega): Observable<Bodega> {
    return this.http.put<Bodega>(`${this.base}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
  
  existeCodigo(codigo: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.base}/existeCodigo`, {
      params: { codigo }
    });
  }
}

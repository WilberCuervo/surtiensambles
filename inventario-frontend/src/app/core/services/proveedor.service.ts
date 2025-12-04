import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app.config';
import { Proveedor } from '../models/proveedor.model';

export interface ProveedorSearchParams {
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
export class ProveedorService {

  private base = `${APP_CONFIG.apiBaseUrl}/proveedores`;

  constructor(private http: HttpClient) { }

  /**
   * Búsqueda avanzada con paginación, filtros y sort usando un objeto de opciones.
   * Llama al endpoint GET /api/proveedores
   */
  list(params: ProveedorSearchParams): Observable<any> {
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
  
  getAllSimpleList(activo?: boolean | null): Observable<Proveedor[]> {
    let params = new HttpParams();
    if (activo !== null && activo !== undefined) {
      params = params.set('activo', activo.toString());
    }
    return this.http.get<Proveedor[]>(`${this.base}/all`, { params });
  }

  get(id: number): Observable<Proveedor> {
    return this.http.get<Proveedor>(`${this.base}/${id}`);
  }

  create(data: Proveedor): Observable<Proveedor> {
    return this.http.post<Proveedor>(this.base, data);
  }

  update(id: number, data: Proveedor): Observable<Proveedor> {
    return this.http.put<Proveedor>(`${this.base}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  existeNit(nit: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.base}/existe`, {
      params: { nit }
    });
  }
}

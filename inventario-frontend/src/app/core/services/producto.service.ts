import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto.model';
import { APP_CONFIG } from '../../config/app.config';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  private base = `${APP_CONFIG.apiBaseUrl}/productos`;

  constructor(private http: HttpClient) {}

  /**
   * Búsqueda avanzada con paginación, filtros y sort
   */
  list(
    page: number = 0,
    size: number = 10,
    search: string = '',
    categoriaId: number | null = null,
    activo: boolean | null = null,
    sort: string = 'id,asc'
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('search', search)
      .set('sort', sort);

    if (search.trim().length > 0) {
      params = params.set('search', search.trim());
    }

    if (categoriaId !== null) {
      params = params.set('categoria', categoriaId);
    }

   if (activo !== null && activo !== undefined) {
    params = params.set('activo', activo.toString());
  }

    return this.http.get<any>(`${this.base}`, { params });
  }

  get(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.base}/${id}`);
  }

  create(p: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.base, p);
  }

  update(id: number, p: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.base}/${id}`, p);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}

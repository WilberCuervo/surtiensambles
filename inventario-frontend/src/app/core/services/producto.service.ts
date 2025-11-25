import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto.model';
import { APP_CONFIG } from '../../config/app.config';

// Define una interfaz para los parámetros de búsqueda, similar a nuestro DTO de Java
export interface ProductoSearchParams {
  page?: number;
  size?: number;
  search?: string;
  categoriaId?: number | null; 
  activo?: boolean | null;
  sortBy?: string;        
  sortDirection?: string; 
}

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  private base = `${APP_CONFIG.apiBaseUrl}/productos`;

  constructor(private http: HttpClient) {}

  /**
   * Búsqueda avanzada con paginación, filtros y sort usando un objeto de opciones.
   */
  list(params: ProductoSearchParams): Observable<any> {
    let httpParams = new HttpParams();

    // Asignar valores por defecto si no se proporcionan
    httpParams = httpParams.set('page', params.page ?? 0);
    httpParams = httpParams.set('size', params.size ?? 10);
    httpParams = httpParams.set('search', params.search ?? '');
    httpParams = httpParams.set('sortBy', params.sortBy ?? 'id');
    httpParams = httpParams.set('sortDirection', params.sortDirection ?? 'asc');


    if (params.search && params.search.trim().length > 0) {
      httpParams = httpParams.set('search', params.search.trim());
    }
    
    
    if (params.categoriaId !== null && params.categoriaId !== undefined) {
      httpParams = httpParams.set('categoriaId', params.categoriaId.toString());
    }

    if (params.activo !== null && params.activo !== undefined) {
      httpParams = httpParams.set('activo', params.activo.toString());
    }

    
    return this.http.get<any>(`${this.base}`, { params: httpParams });
  }
  
  /**
   * Nuevo método para obtener todos los productos sin paginar (para dropdowns)
   * Llama al endpoint /api/productos/all
   */
  getAllSimpleList(activo?: boolean | null): Observable<Producto[]> {
    let params = new HttpParams();
    if (activo !== null && activo !== undefined) {
      params = params.set('activo', activo.toString());
    }
    return this.http.get<Producto[]>(`${this.base}/all`, { params });
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

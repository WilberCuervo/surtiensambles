import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app.config';
import { Categoria } from '../models/categoria.model';

export interface CategoriaPage {
  content: Categoria[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  private base = `${APP_CONFIG.apiBaseUrl}/categorias`;

  constructor(private http: HttpClient) {}

  
  //Obtener TODAS las categorías sin paginar (para dropdown de productos)
   
  getAll(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.base}/all`);
  }

  
   //Listar categorías con paginación + búsqueda + ordenamiento
   
  list(page = 0, size = 10, search = '', sort = 'id,asc', activo: boolean | null = null) {
  let params = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('search', search)
    .set('sort', sort);

  if (activo !== null) {
    params = params.set('activo', activo);
  }

  return this.http.get<any>(`${this.base}`, { params });
}

  get(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.base}/${id}`);
  }

  create(categoria: Categoria): Observable<Categoria> {
    return this.http.post<Categoria>(this.base, categoria);
  }

  update(id: number, categoria: Categoria): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.base}/${id}`, categoria);
  }


  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  existeNombre(nombre: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.base}/existe`, {
      params: { nombre }
    });
  }
}

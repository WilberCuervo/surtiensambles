import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app.config';
import { Proveedor } from '../models/proveedor.model';

export interface ProveedorPage {
  content: Proveedor[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProveedorService {

  private base = `${APP_CONFIG.apiBaseUrl}/proveedores`;

  constructor(private http: HttpClient) { }

  list(
    page: number,
    size: number,
    search: string = '',
    sort: string = 'id,asc',
    activo?: boolean | null
  ): Observable<ProveedorPage> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('search', search)
      .set('sort', sort);

    if (activo !== null && activo !== undefined) {
      params = params.set('activo', activo.toString());
    }
    return this.http.get<ProveedorPage>(this.base, { params });
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

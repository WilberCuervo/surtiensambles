import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bodega } from '../models/bodega.model';
import { APP_CONFIG } from '../../config/app.config';

@Injectable({ providedIn: 'root' })
export class BodegaService {
  private base = `${APP_CONFIG.apiBaseUrl}/bodegas`;
  constructor(private http: HttpClient) {}

  list(): Observable<Bodega[]> { return this.http.get<Bodega[]>(this.base); }
  get(id: number): Observable<Bodega> { return this.http.get<Bodega>(`${this.base}/${id}`); }
  create(b: Bodega): Observable<Bodega> { return this.http.post<Bodega>(this.base, b); }
  update(id: number, b: Bodega): Observable<Bodega> { return this.http.put<Bodega>(`${this.base}/${id}`, b); }
  delete(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/${id}`); }
}

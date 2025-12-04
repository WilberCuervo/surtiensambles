import { Producto } from './producto.model';
import { Bodega } from './bodega.model'; // Opcional si solo usas el ID en la respuesta

export interface MovimientoInventario {
  id: number;
  producto: Producto;
  bodega: Bodega; 
  tipo: 'ENTRADA' | 'SALIDA' | 'AJUSTE' | 'TRANSFERENCIA';
  cantidad: number;
  fecha: string; // ISO String
  usuario?: string;
  nota?: string;
  referenciaTipo?: string;
  referenciaId?: string;
}

export interface MovimientoRequest {
  productoId: number;
  
  // Para operaciones simples
  bodegaId?: number; 
  
  // Para transferencias
  bodegaOrigenId?: number;
  bodegaDestinoId?: number;
  
  tipo: string;
  cantidad: number;
  nota?: string;
  usuario?: string;
  referenciaTipo?: string;
  referenciaId?: string;
}
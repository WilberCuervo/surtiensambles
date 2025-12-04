import { Producto } from './producto.model';
import { Bodega } from './bodega.model';

export type EstadoReserva = 'ACTIVA' | 'CONSUMIDA' | 'EXPIRADA' | 'CANCELADA';

// Lo que recibimos del Backend (Para la tabla)
export interface ReservaInventario {
  id: number;
  referenciaReserva: string;
  producto: Producto;
  bodega: Bodega;
  cantidad: number;
  fechaReserva: string;
  fechaExpiracion?: string;
  estado: EstadoReserva;
  usuario?: string;
  nota?: string;
}

// Lo que enviamos al Backend (Para crear)
export interface ReservaRequest {
  productoId: number;
  bodegaId: number;
  cantidad: number;
  nota?: string;
  usuario?: string;
  fechaExpiracion?: string; // Opcional, formato YYYY-MM-DD
}
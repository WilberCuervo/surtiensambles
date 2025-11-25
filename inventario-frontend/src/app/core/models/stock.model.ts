import { Producto } from './producto.model';
import { Bodega } from './bodega.model';

export interface Stock {
  id?: number;
  
  producto: Producto;
  bodega: Bodega;

  cantidadDisponible: number;
  cantidadReservada: number;
  ultimaActualizacion?: string;
}

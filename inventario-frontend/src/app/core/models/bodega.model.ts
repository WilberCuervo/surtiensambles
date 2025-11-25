export interface Bodega {
  id?: number;
  codigo: string;
  nombre: string;
  ubicacion?: string | null;
  responsable?: string | null;
  activo?: boolean;
}

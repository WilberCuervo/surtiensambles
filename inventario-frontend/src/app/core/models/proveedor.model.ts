export interface Proveedor {
  id?: number;
  nombre: string;
  nit: string;
  telefono?: string | null;
  email?: string | null;
  direccion?: string | null;
  activo?: boolean;
}

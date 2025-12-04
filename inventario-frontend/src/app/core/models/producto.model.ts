import { Categoria } from "./categoria.model";

export interface Producto {
  id?: number;
  sku: string;
  nombre: string;
  descripcion?: string;
  categoria?: Partial<Categoria> | null;
  unidadMedida?: string;
  precioReferencia?: number;
  nivelReorden?: number;
  activo?: boolean;
  created_at?: string | null;
  updated_at?: string | null;
}

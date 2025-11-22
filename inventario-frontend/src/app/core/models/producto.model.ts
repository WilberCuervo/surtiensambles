import { Categoria } from "./categoria.model";

export interface Producto {
  id?: number;
  sku: string;
  nombre: string;
  descripcion?: string;
  categoria?: Partial<Categoria> | null;
  unidad_medida?: string;
  precio_referencia?: number;
  nivel_reorden?: number;
  activo?: boolean;
  created_at?: string | null;
  updated_at?: string | null;
}

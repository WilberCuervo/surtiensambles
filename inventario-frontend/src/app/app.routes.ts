import { Routes } from '@angular/router';

// Importaciones de los componentes standalone
import { CategoriaListComponent } from './components/categoria/categoria-list/categoria-list.component';
import { CategoriaFormComponent } from './components/categoria/categoria-form/categoria-form.component';

import { ProveedorListComponent } from './components/proveedor/proveedor-list/proveedor-list.component';
import { ProveedorFormComponent } from './components/proveedor/proveedor-form/proveedor-form.component';

import { ProductoListComponent } from './components/producto/producto-list/producto-list.component';
import { ProductoFormComponent } from './components/producto/producto-form/producto-form.component';

import { BodegaListComponent } from './components/bodega/bodega-list/bodega-list.component';
import { BodegaFormComponent } from './components/bodega/bodega-form/bodega-form.component';

import { StockListComponent } from './components/stock/stock-list/stock-list.component';
import { StockFormComponent } from './components/stock/stock-form/stock-form.component';

import { MovimientoInventarioListComponent } from './components/movimiento-inventario/movimiento-inventario-list/movimiento-inventario-list.component';
import { MovimientoInventarioFormComponent } from './components/movimiento-inventario/movimiento-inventario-form/movimiento-inventario-form.component';

import { ReservaInventarioListComponent } from './components/reserva-inventario/reserva-inventario-list/reserva-inventario-list.component';
import { ReservaInventarioFormComponent } from './components/reserva-inventario/reserva-inventario-form/reserva-inventario-form.component';

export const routes: Routes = [
  { path: '', redirectTo: '/productos', pathMatch: 'full' },

  // 🟨 Productos
  { path: 'productos', component: ProductoListComponent },
  { path: 'productos/nuevo', component: ProductoFormComponent },
  { path: 'productos/editar/:id', component: ProductoFormComponent },

  // 🟩 Categorías
  { path: 'categorias', component: CategoriaListComponent },
  { path: 'categorias/nueva', component: CategoriaFormComponent },
  { path: 'categorias/editar/:id', component: CategoriaFormComponent },

  // 🟦 Proveedores
  { path: 'proveedores', component: ProveedorListComponent },
  { path: 'proveedores/nuevo', component: ProveedorFormComponent },
  { path: 'proveedores/editar/:id', component: ProveedorFormComponent },

  // 🟧 Bodegas
  { path: 'bodegas', component: BodegaListComponent },
  { path: 'bodegas/nueva', component: BodegaFormComponent },
  { path: 'bodegas/editar/:id', component: BodegaFormComponent },

  // 🟪 Stock
  { path: 'stock', component: StockListComponent },
  { path: 'stock/nuevo', component: StockFormComponent },
  { path: 'stock/editar/:id', component: StockFormComponent },

  // 🟥 Movimiento Inventario
  { path: 'movimientos', component: MovimientoInventarioListComponent },
  { path: 'movimientos/nuevo', component: MovimientoInventarioFormComponent },
  { path: 'movimientos/editar/:id', component: MovimientoInventarioFormComponent },

  // ⬜ Reserva Inventario
  { path: 'reservas', component: ReservaInventarioListComponent },
  { path: 'reservas/nueva', component: ReservaInventarioFormComponent },
  { path: 'reservas/editar/:id', component: ReservaInventarioFormComponent },

  // Ruta por defecto
  { path: '**', redirectTo: '/productos' }
];

package com.surtiensambles.producto.entity;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.surtiensambes.commos.producto.model.Clasificacion;
import com.surtiensambes.commos.producto.model.Componentes;
import com.surtiensambes.commos.producto.model.DatosSistema;
import com.surtiensambes.commos.producto.model.Otros;
import com.surtiensambes.commos.producto.model.PreciosSucursal;
import com.surtiensambes.commos.producto.model.Presentacion;
import com.surtiensambes.commos.producto.model.ProductoFacturacion;
import com.surtiensambes.commos.producto.model.ProductoUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto_detalle")
public class ProductoDetalle extends ProductoUtil{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getId() {
		return super.getId();
	}
	
	@JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "clasificacion", columnDefinition = "jsonb")
    public Clasificacion getClasificacion() {
        return super.getClasificacion();
    }
    
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonInclude(Include.NON_NULL)    
    @Column(name = "producto_facturacion", columnDefinition = "jsonb")
    public ProductoFacturacion getProductoFacturacion() {
    	return super.getProductoFacturacion();
    	
    }
    
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonInclude(Include.NON_NULL)    
    @Column(name = "componentes", columnDefinition = "jsonb")
    public Componentes getComponentes() {
    	return super.getComponentes();
    	
    }
    
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonInclude(Include.NON_NULL)    
    @Column(name = "otros", columnDefinition = "jsonb")
    public Otros getOtros() {
    	return super.getOtros();
    	
    }
    
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonInclude(Include.NON_NULL)    
    @Column(name = "datos_sistema", columnDefinition = "jsonb")
    public DatosSistema getDatosSistema() {
    	return super.getDatosSistema();
    	
    }
    
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonInclude(Include.NON_NULL)    
    @Column(name = "presentacion", columnDefinition = "jsonb")
    public List<Presentacion> getPresentaciones() {
    	return super.getPresentaciones();
    	
    }
    
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonInclude(Include.NON_NULL)    
    @Column(name = "precio_sucursal", columnDefinition = "jsonb")
    public List<PreciosSucursal> getPreciosSucursal() {
    	return super.getPreciosSucursal();
    	
    }

}

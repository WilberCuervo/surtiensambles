package com.surtiensambles.producto.entity;

import com.surtiensambes.commos.producto.model.ProductoInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos")
@Builder
public class Producto extends ProductoInfo {
	
	private ProductoDetalle productoDetalle;
	
	@Schema(description = "Detalle completo del producto")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "producto_detalle_id", referencedColumnName = "id")
	public ProductoDetalle getProductoDetalle() {
	    return productoDetalle;
	}

 

    public void setProductoDetalle(ProductoDetalle productoDetalle) {
        this.productoDetalle = productoDetalle;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
    public Long getId() {
    	return super.getId();
    }
    
    @Column(nullable = false, unique = true)
    public String getCodigo() {
    	return super.getCodigo();
    }
    
    @Column
    public String getDescripcion() {
    	return super.getDescripcion();
    }
    
    @Column
    public String getMarca() {
    	return super.getMarca();
    }
    @Column
    public String getModelo() {
    	return super.getModelo();
    }
    @Column(name = "id_sucursal_cliente")
    public Long getIdSucursalCliente() {
    	return super.getIdSucursalCliente();
    }
    @Column(name = "datos_tecnicos")
    public String getDatosTecnicos() {
    	return super.getDatosTecnicos();
    }   
    
    @Column
    public String getOrigen() {
    	return super.getOrigen();
    }
    
    @Column
    public String getDestino() {
    	return super.getDestino();
    }
    
    @Column
    public String getEstado() {
    	return super.getEstado();
    }
    
    
    
}


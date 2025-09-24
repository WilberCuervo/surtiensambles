package com.surtiensambles.producto.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surtiensambles.producto.entity.Producto;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}

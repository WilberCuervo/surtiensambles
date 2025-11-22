package com.surtiensambles.inventario.repository;

import com.surtiensambles.inventario.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySku(String codigo);

    @Query("SELECT p FROM Producto p " +
           "WHERE (:search IS NULL OR :search = '' " +
           "OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:activo IS NULL OR p.activo = :activo)")
    Page<Producto> search(
            @Param("search") String search,
            @Param("categoriaId") Long categoriaId,
            @Param("activo") Boolean activo,
            Pageable pageable);
}

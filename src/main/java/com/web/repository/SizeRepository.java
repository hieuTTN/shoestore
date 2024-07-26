package com.web.repository;

import com.web.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {

    @Query("select s from Size s where s.product.id = ?1")
    List<Size> findByProduct(Long productId);
}

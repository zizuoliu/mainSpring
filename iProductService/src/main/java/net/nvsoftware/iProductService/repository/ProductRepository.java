package net.nvsoftware.iProductService.repository;

import net.nvsoftware.iProductService.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}

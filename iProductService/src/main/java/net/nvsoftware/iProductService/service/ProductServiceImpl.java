package net.nvsoftware.iProductService.service;

import net.nvsoftware.iProductService.entity.ProductEntity;
import net.nvsoftware.iProductService.model.ProductRequest;
import net.nvsoftware.iProductService.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long createProduct(ProductRequest productRequest) {
        ProductEntity productEntity = ProductEntity.builder()
                .productName(productRequest.getProductName())
                .productPrice(productRequest.getProductPrice())
                .productQuantity(productRequest.getProductQuantity())
                .build();

        productRepository.save(productEntity);

        return productEntity.getProductId();
    }
}

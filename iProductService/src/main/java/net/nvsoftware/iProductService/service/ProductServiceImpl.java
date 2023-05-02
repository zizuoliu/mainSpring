package net.nvsoftware.iProductService.service;

import lombok.extern.log4j.Log4j2;
import net.nvsoftware.iProductService.entity.ProductEntity;
import net.nvsoftware.iProductService.model.ProductRequest;
import net.nvsoftware.iProductService.model.ProductResponse;
import net.nvsoftware.iProductService.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long createProduct(ProductRequest productRequest) {
        log.info("ProductService createProduct start");
        ProductEntity productEntity = ProductEntity.builder()
                .productName(productRequest.getProductName())
                .productPrice(productRequest.getProductPrice())
                .productQuantity(productRequest.getProductQuantity())
                .build();

        productRepository.save(productEntity);
        log.info("ProductService createProduct done with productId: " + productEntity.getProductId());
        return productEntity.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("ProductService getProductById: " + productId);

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ProductService getProductById NOT FOUND with id: " + productId));

        ProductResponse productResponse = new ProductResponse();

        BeanUtils.copyProperties(productEntity, productResponse);

        log.info("ProductService getProductById done with productId:  " + productId);

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("ProductService reduceQuantity: " + quantity + "for productId: " + productId + " Start");

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ProductService reduceQuantity productId NOT FOUND"));

        if (productEntity.getProductQuantity() < quantity) {
            throw new RuntimeException("ProductService reduceQuantity quantity too big");
        }

        productEntity.setProductQuantity(productEntity.getProductQuantity() - quantity);
        productRepository.save(productEntity);
        log.info("ProductService reduceQuantity: " + quantity + "for productId: " + productId + " Done");
    }
}

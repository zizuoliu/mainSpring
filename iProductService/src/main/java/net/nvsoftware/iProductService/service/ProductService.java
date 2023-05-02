package net.nvsoftware.iProductService.service;

import net.nvsoftware.iProductService.model.ProductRequest;
import net.nvsoftware.iProductService.model.ProductResponse;

public interface ProductService {
    long createProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}

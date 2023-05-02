package net.nvsoftware.iProductService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private long productId;
    private String productName;
    private long productPrice;
    private long productQuantity;
}

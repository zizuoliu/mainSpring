package net.nvsoftware.iOrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private long productId;
    private long orderQuantity;
    private long totalAmount;
    private PaymentMode paymentMode;
}

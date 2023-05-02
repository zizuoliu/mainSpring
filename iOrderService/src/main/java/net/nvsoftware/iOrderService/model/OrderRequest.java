package net.nvsoftware.iOrderService.model;

import lombok.Data;

@Data
public class OrderRequest {
    private long productId;
    private long orderQuantity;
    private long totalAmount;
    private PaymentMode paymentMode;
}

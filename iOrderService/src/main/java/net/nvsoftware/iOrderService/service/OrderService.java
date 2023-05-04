package net.nvsoftware.iOrderService.service;

import net.nvsoftware.iOrderService.model.OrderRequest;
import net.nvsoftware.iOrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetailByOrderId(long orderId);
}

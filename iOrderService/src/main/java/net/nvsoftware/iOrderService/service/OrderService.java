package net.nvsoftware.iOrderService.service;

import net.nvsoftware.iOrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}

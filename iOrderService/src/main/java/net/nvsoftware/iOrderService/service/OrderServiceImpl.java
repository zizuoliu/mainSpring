package net.nvsoftware.iOrderService.service;

import com.netflix.discovery.converters.Auto;
import lombok.extern.log4j.Log4j2;
import net.nvsoftware.iOrderService.entity.OrderEntity;
import net.nvsoftware.iOrderService.external.client.ProductServiceFeignClient;
import net.nvsoftware.iOrderService.model.OrderRequest;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductServiceFeignClient productServiceFeignClient;

    @Override
    public long placeOrder(OrderRequest orderRequest) {//TODO: make this method as transaction
        // Call OrderService(this) to create order entity with status CREATED, save to orderdb
        log.info("OrderService placeOrder - save to orderdb start");
        OrderEntity orderEntity = OrderEntity.builder()
                .productId(orderRequest.getProductId())
                .orderQuantity(orderRequest.getOrderQuantity())
                .totalAmount(orderRequest.getTotalAmount())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .paymentMode(orderRequest.getPaymentMode().name())
                .build();
        orderEntity = orderRepository.save(orderEntity);
        log.info("OrderService placeOrder - save to orderdb done");

        // Call ProductService to check quantity and reduceQuantity if ok
        log.info("ProductServiceFeignClient reduceQuantity start");
        productServiceFeignClient.reduceQuantity(orderRequest.getProductId(), orderRequest.getOrderQuantity());
        log.info("ProductServiceFeignClient reduceQuantity done");
        // Call PaymentService to charge paymentMode, mark order COMPLETED if success, otherwise mark CANCELLED



        return orderEntity.getOrderId();
    }
}

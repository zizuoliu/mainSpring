package net.nvsoftware.iOrderService.service;

import net.nvsoftware.iOrderService.entity.OrderEntity;
import net.nvsoftware.iOrderService.external.client.PaymentServiceFeignClient;
import net.nvsoftware.iOrderService.external.client.ProductServiceFeignClient;
import net.nvsoftware.iOrderService.model.OrderResponse;
import net.nvsoftware.iOrderService.model.PaymentMode;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {
    @Mock
    RestTemplate restTemplate;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductServiceFeignClient productServiceFeignClient;

    @Mock
    private PaymentServiceFeignClient paymentServiceFeignClient;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @DisplayName("Get Order Detail - SUCCESS")
    @Test
    void testWhenGetOrderSuccess() {
        // Mock Part
        OrderEntity orderEntity = getMockOrderEntity();
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(orderEntity));

        Mockito.when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + orderEntity.getProductId(),
                OrderResponse.ProductResponse.class
        )).thenReturn(getMockProductResponse());

        Mockito.when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payments/" + orderEntity.getOrderId(),
                OrderResponse.PaymentResponse.class
        )).thenReturn(getMockPaymentResponse());

        // Actual Call
        OrderResponse orderResponse = orderService.getOrderDetailByOrderId(1);

        // Verify Call
        Mockito.verify(orderRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(
                "http://PRODUCT-SERVICE/products/" + orderEntity.getProductId(),
                OrderResponse.ProductResponse.class
        );
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(
                "http://PAYMENT-SERVICE/payments/" + orderEntity.getOrderId(),
                OrderResponse.PaymentResponse.class
        );

        // Assert Response
        Assertions.assertNotNull(orderResponse);
        Assertions.assertEquals(orderEntity.getOrderId(), orderResponse.getOrderId());
    }

    private OrderEntity getMockOrderEntity() {
        return OrderEntity.builder()
                .orderId(1)
                .productId(5)
                .orderQuantity(1)
                .totalAmount(1299)
                .orderDate(Instant.now())
                .orderStatus("PLACED")
                .build();
    }

    private OrderResponse.PaymentResponse getMockPaymentResponse() {
        return OrderResponse.PaymentResponse.builder()
                .orderId(1)
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .paymentStatus("SUCCESS")
                .totalAmount(2599)
                .build();
    }

    private OrderResponse.ProductResponse getMockProductResponse() {
        return OrderResponse.ProductResponse.builder()
                .productId(5)
                .productName("MacMini")
                .productQuantity(2)
                .productPrice(1299)
                .build();
    }
}